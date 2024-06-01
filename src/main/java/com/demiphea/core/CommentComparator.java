package com.demiphea.core;

import com.demiphea.model.vo.comment.CommentVo;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * CommentComparator
 * 评论的排序算法
 *
 * @author demiphea
 * @since 17.0.9
 */
public class CommentComparator implements Comparator<CommentVo> {
    @Override
    public int compare(CommentVo a, CommentVo b) {
        BigDecimal aScore = CommentSort.calcScore(a.getAgreeNumber(), a.getReplyNumber(), a.getCreateTime());
        BigDecimal bScore = CommentSort.calcScore(b.getAgreeNumber(), b.getReplyNumber(), b.getCreateTime());
        return bScore.compareTo(aScore);
    }
}
