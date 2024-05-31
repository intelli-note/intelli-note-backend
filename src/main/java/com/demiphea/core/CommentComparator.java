package com.demiphea.core;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.demiphea.model.vo.comment.CommentVo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;

/**
 * CommentComparator
 * 评论的排序算法
 *
 * @author demiphea
 * @since 17.0.9
 */
public class CommentComparator implements Comparator<CommentVo> {
    /**
     * 点赞量权重
     */
    private final BigDecimal agreeWeight = BigDecimal.valueOf(0.8);
    /**
     * 回复量权重
     */
    private final BigDecimal replyWeight = BigDecimal.valueOf(0.2);

    /**
     * 标准正态分布的分位数（95%的置信区间）
     */
    private final BigDecimal z = BigDecimal.valueOf(1.96);

    /**
     * 衰减率（一个正数，决定了评论随时间衰减的速度）
     */
    private final BigDecimal lambda = BigDecimal.valueOf(0.05);

    /**
     * 运算精度、四舍五入容器
     */
    private final MathContext context = new MathContext(100);

    private final BigDecimal[] number = new BigDecimal[]{
            // 0
            BigDecimal.ZERO,
            // 1
            BigDecimal.ONE,
            // 2
            BigDecimal.valueOf(2),
            // 3
            BigDecimal.valueOf(3),
            // 4
            BigDecimal.valueOf(4),
            // 5
            BigDecimal.valueOf(5),
            // 6
            BigDecimal.valueOf(6),
            // 7
            BigDecimal.valueOf(7),
            // 8
            BigDecimal.valueOf(8),
            // 9
            BigDecimal.valueOf(9),
            // 10
            BigDecimal.TEN
    };

    /**
     * 计算威尔逊得分
     *
     * @param p 好评率
     * @param z 正态分布的分位数
     * @param n 好评数和差评数之和
     * @return {@link BigDecimal} 威尔逊得分
     * @author demiphea
     */
    private BigDecimal calcWilsonScore(BigDecimal p, BigDecimal z, BigDecimal n) {
        if (n.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return p.add(z
                        .pow(2, context)
                        .divide(number[2].multiply(n, context), context))
                .subtract(z
                        .multiply(BigDecimalMath.sqrt(
                                number[4]
                                        .multiply(n, context)
                                        .multiply(number[1].subtract(p, context), context)
                                        .multiply(p, context)
                                        .add(z.pow(2, context)),
                                context
                        ), context)
                        .divide(number[2].multiply(n, context), context), context)
                .divide(number[1]
                        .add(z.pow(2, context).divide(n, context)), context);
    }

    /**
     * 评论的新鲜度衰减
     *
     * @param lambda 衰减率（一个正数，决定了评论随时间衰减的速度）
     * @param t      当前时间(s)
     * @param t0     评论的发表时间(s)
     * @return {@link BigDecimal} 衰减因子
     * @author demiphea
     */
    private BigDecimal calcDecayFactor(BigDecimal lambda, BigDecimal t, BigDecimal t0) {
        return BigDecimalMath.exp(
                lambda
                        .multiply(t
                                .subtract(t0, context)
                                .divide(BigDecimal.valueOf(60), context), context)
                        .negate(context),
                context
        );
    }


    /**
     * 计算得分值
     *
     * @param commentVo 评论VO
     * @return {@link BigDecimal} 得分
     * @author demiphea
     */
    private BigDecimal calcScore(CommentVo commentVo) {
        BigDecimal p = number[1];
        BigDecimal z = this.z;
        BigDecimal n = BigDecimal.valueOf(commentVo.getAgreeNumber()).multiply(agreeWeight, context)
                .add(BigDecimal.valueOf(commentVo.getReplyNumber()).multiply(replyWeight, context));
        BigDecimal wilsonScore = calcWilsonScore(p, z, n);

        BigDecimal lambda = this.lambda;
        BigDecimal t = BigDecimal.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        BigDecimal t0 = BigDecimal.valueOf(commentVo.getCreateTime().toEpochSecond(ZoneOffset.UTC));
        BigDecimal decayFactor = calcDecayFactor(lambda, t, t0);
        return wilsonScore.multiply(decayFactor, context);
    }

    @Override
    public int compare(CommentVo a, CommentVo b) {
        BigDecimal bScore = calcScore(b);
        BigDecimal aScore = calcScore(a);
        System.out.println("aScore(" + a + ") = " + aScore);
        System.out.println("bScore(" + b + ") = " + bScore);
        return bScore.compareTo(aScore);
    }
}
