package com.demiphea.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Wallet
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 收入
     */
    private BigDecimal revenue;
}
