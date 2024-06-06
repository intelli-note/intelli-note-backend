package com.demiphea.model.vo.notice;

import com.demiphea.model.vo.user.BillVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BillNotice
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillNotice implements INotice {
    /**
     * 账单信息
     */
    private BillVo bill;
}
