package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 售后单 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_after_sale", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "售后单对象", description = "售后单表")
public class AfterSale extends BaseEntity {

    @TableId
    private Long id;

    private String no;

    private Integer status;

    private Integer way;

    private Integer type;

    private Long userId;

    private String applyReason;

    private String applyDescription;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> applyPicUrls;

    private Long orderId;

    private String orderNo;

    private Long orderItemId;

    private Long spuId;

    private String spuName;

    private Long skuId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Object> properties;

    private String picUrl;

    private Integer count;

    private LocalDateTime auditTime;

    private Long auditUserId;

    private String auditReason;

    private Integer refundPrice;

    private Long payRefundId;

    private LocalDateTime refundTime;

    private Long logisticsId;

    private String logisticsNo;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private String receiveReason;

}
