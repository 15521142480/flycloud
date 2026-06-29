package com.fly.system.api.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付订单创建请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "支付订单创建请求对象")
public class PayOrderCreateReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应用标识")
    private String appKey;

    @Schema(description = "应用编号")
    private Long appId;

    @Schema(description = "用户 IP")
    private String userIp;

    @Schema(description = "用户编号")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "商户订单编号")
    @NotEmpty(message = "商户订单编号不能为空")
    private String merchantOrderId;

    @Schema(description = "商品标题")
    @NotEmpty(message = "商品标题不能为空")
    private String subject;

    @Schema(description = "商品描述")
    private String body;

    @Schema(description = "支付金额，单位：分")
    @NotNull(message = "支付金额不能为空")
    private Integer price;

    @Schema(description = "支付过期时间")
    @NotNull(message = "支付过期时间不能为空")
    private LocalDateTime expireTime;

}
