package com.fly.system.api.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 移动端 - 支付订单提交请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 支付订单提交请求对象")
public class AppPayOrderSubmitReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "支付单编号")
    @NotNull(message = "支付单编号不能为空")
    private Long id;

    @Schema(description = "支付渠道")
    @NotEmpty(message = "支付渠道不能为空")
    private String channelCode;

    @Schema(description = "支付渠道额外参数")
    private Map<String, String> channelExtras;

    @Schema(description = "展示模式")
    private String displayMode;

    @Schema(description = "回跳地址")
    private String returnUrl;

}
