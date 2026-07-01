package com.fly.system.api.pay.domain.bo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付示例提现单创建请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class PayDemoWithdrawCreateReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提现标题。
     */
    @NotBlank(message = "提现标题不能为空")
    private String subject;

    /**
     * 提现金额，单位：分。
     */
    @NotNull(message = "提现金额不能为空")
    @Min(value = 1, message = "提现金额必须大于 0")
    private Integer price;

    /**
     * 收款账号。
     */
    @NotBlank(message = "收款账号不能为空")
    private String userAccount;

    /**
     * 收款人姓名。
     */
    private String userName;

    /**
     * 提现方式。
     */
    @NotNull(message = "提现方式不能为空")
    private Integer type;

}
