package com.demiphea.model.dto.user;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * WalletUpdateDto
 *
 * @author demiphea
 * @since 17.0.9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletUpdateDto {
    @NotNull(message = "需要传入操作类型")
    private Operate type;
    @NotNull(message = "需要传入操作金额")
    @Digits(integer = 10, fraction = 2, message = "金额格式不正确，需要保留两位小数")
    @DecimalMin(value = "0", message = "金额必须大于或等于0.00")
    private BigDecimal amount;

    public enum Operate {
        // 充值
        IN("deposit"),
        // 提现
        OUT("withdrawal");
        @JsonValue
        public final String type;

        Operate(String type) {
            this.type = type;
        }
    }
}
