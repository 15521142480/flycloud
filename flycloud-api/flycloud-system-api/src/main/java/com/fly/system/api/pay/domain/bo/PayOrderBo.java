package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付订单 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayOrderBo extends BaseEntity {

    @Schema(description = "支付订单编号")
    private Long id;

    @Schema(description = "应用编号")
    private Long appId;

    @Schema(description = "渠道编号")
    private Long channelId;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "用户编号")
    private Long userId;

    @Schema(description = "商户订单编号")
    private String merchantOrderId;

    @Schema(description = "商品标题")
    private String subject;

    @Schema(description = "支付状态")
    private Integer status;

    @Schema(description = "支付订单号")
    private String no;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

}
