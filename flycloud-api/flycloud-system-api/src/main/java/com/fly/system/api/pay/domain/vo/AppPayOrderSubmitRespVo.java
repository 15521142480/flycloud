package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 支付订单提交返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 支付订单提交返回对象")
public class AppPayOrderSubmitRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "支付状态")
    private Integer status;

    @Schema(description = "展示模式")
    private String displayMode;

    @Schema(description = "展示内容")
    private String displayContent;

}
