package com.demiphea.core.common;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * CommentSort
 * 评论的排序算法
 *
 * @author demiphea
 * @since 17.0.9
 */
public class CommentSort {
    /**
     * 点赞量权重
     */
    private static final BigDecimal agreeWeight = BigDecimal.valueOf(0.8);

    /**
     * 回复量权重
     */
    private static final BigDecimal replyWeight = BigDecimal.valueOf(0.2);

    /**
     * 标准正态分布的分位数（95%的置信区间）
     */
    private static final BigDecimal z = BigDecimal.valueOf(1.96);

    /**
     * 衰减率（一个正数，决定了评论随时间衰减的速度）
     */
    private static final BigDecimal lambda = BigDecimal.valueOf(0.05);

    /**
     * 运算精度、四舍五入容器
     */
    private static final MathContext context = new MathContext(100);

    /**
     * 缓存
     */
    private static final BigDecimal[] number = new BigDecimal[]{
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
     * 默认威尔逊分数
     */
    private static final BigDecimal defaultWilsonScore = BigDecimal.ZERO;

    /**
     * 计算威尔逊得分
     *
     * @param p 好评率
     * @param z 正态分布的分位数
     * @param n 好评数和差评数之和
     * @return {@link BigDecimal} 威尔逊得分
     * @author demiphea
     */
    public static BigDecimal calcWilsonScore(BigDecimal p, BigDecimal z, BigDecimal n) {
        if (n.compareTo(BigDecimal.ZERO) == 0) {
            return defaultWilsonScore;
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
     * 时间衰减
     *
     * @param lambda 衰减率（一个正数，决定了评论随时间衰减的速度）
     * @param t      当前时间(s)
     * @param t0     评论的发表时间(s)
     * @param scale  时间放缩尺度(s)
     * @return {@link BigDecimal} 衰减因子
     * @author demiphea
     */
    public static BigDecimal calcDecayFactor(BigDecimal lambda, BigDecimal t, BigDecimal t0, BigDecimal scale) {
        return BigDecimalMath.exp(
                lambda
                        .multiply(t
                                .subtract(t0, context)
                                .divide(scale, context), context)
                        .negate(context),
                context
        );
    }

    /**
     * 评论的新鲜度衰减（2小时单位）
     *
     * @param lambda 衰减率（一个正数，决定了评论随时间衰减的速度）
     * @param t      当前时间(s)
     * @param t0     评论的发表时间(s)
     * @return {@link BigDecimal} 衰减因子
     * @author demiphea
     */
    public static BigDecimal calcDecayFactor(BigDecimal lambda, BigDecimal t, BigDecimal t0) {
        return calcDecayFactor(lambda, t, t0, BigDecimal.valueOf(7200));
    }

    /**
     * 计算得分值
     *
     * @param wilsonScore 威尔逊分数
     * @param decayFactor 衰减因子
     * @return {@link BigDecimal} 得分值
     * @author demiphea
     */
    public static BigDecimal calcScore(BigDecimal wilsonScore, BigDecimal decayFactor) {
        return wilsonScore.multiply(decayFactor, context);
    }


    /**
     * 计算得分值
     *
     * @param agreeNum   点赞数
     * @param replyNum   评论数
     * @param createTime 评论时间
     * @return {@link BigDecimal} 得分值
     * @author demiphea
     */
    public static BigDecimal calcScore(Long agreeNum, Long replyNum, LocalDateTime createTime) {
        BigDecimal p = number[1];
        BigDecimal z = CommentSort.z;
        BigDecimal n = BigDecimal.valueOf(agreeNum).multiply(agreeWeight, context)
                .add(BigDecimal.valueOf(replyNum).multiply(replyWeight, context));
        BigDecimal wilsonScore = calcWilsonScore(p, z, n);

        BigDecimal lambda = CommentSort.lambda;
        BigDecimal t = BigDecimal.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        BigDecimal t0 = BigDecimal.valueOf(createTime.toEpochSecond(ZoneOffset.UTC));
        BigDecimal decayFactor = calcDecayFactor(lambda, t, t0);

        return CommentSort.calcScore(wilsonScore, decayFactor);
    }
}
