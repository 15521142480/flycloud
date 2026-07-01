package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 支付订单拓展。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "pay_order_extension", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "支付订单拓展对象", description = "支付订单拓展")
public class PayOrderExtension extends BaseEntity {

    @TableId
    private Long id;

    private String no;

    private Long orderId;

    private Long channelId;

    private String channelCode;

    private String userIp;

    private Integer status;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> channelExtras;

    private String channelErrorCode;

    private String channelErrorMsg;

    private String channelNotifyData;

}
