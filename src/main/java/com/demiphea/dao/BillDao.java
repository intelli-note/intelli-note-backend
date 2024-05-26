package com.demiphea.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demiphea.entity.Bill;

/**
 * 账单表 Dao接口
 * @author demiphea
 * @since 17.0.9
 */
public interface BillDao extends BaseMapper<Bill> {
    /**
     * 判断是否用户是否购买笔记
     *
     * @param userId 用户ID
     * @param noteId 笔记ID
     * @return 是否购买
     * @author demiphea
     */
    boolean hasBuy(Long userId, Long noteId);
}
