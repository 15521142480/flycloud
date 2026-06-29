package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易订单 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_order", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "交易订单对象", description = "交易订单表")
public class TradeOrder extends BaseEntity {

    @TableId
    private Long id;

    private String no;

    private Integer type;

    private Integer terminal;

    private Long userId;

    private String userIp;

    private String userRemark;

    private Integer status;

    private Integer productCount;

    private LocalDateTime finishTime;

    private LocalDateTime cancelTime;

    private Integer cancelType;

    private String remark;

    private Boolean commentStatus;

    private Long brokerageUserId;

    private Long payOrderId;

    private Boolean payStatus;

    private LocalDateTime payTime;

    private String payChannelCode;

    private Integer totalPrice;

    private Integer discountPrice;

    private Integer deliveryPrice;

    private Integer adjustPrice;

    private Integer payPrice;

    private Integer deliveryType;

    private Long logisticsId;

    private String logisticsNo;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private String receiverName;

    private String receiverMobile;

    private Integer receiverAreaId;

    private String receiverDetailAddress;

    private Long pickUpStoreId;

    private String pickUpVerifyCode;

    private Integer refundStatus;

    private Integer refundPrice;

    private Long couponId;

    private Integer couponPrice;

    private Integer usePoint;

    private Integer pointPrice;

    private Integer givePoint;

    private Integer refundPoint;

    private Integer vipPrice;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<Long, Integer> giveCouponTemplateCounts;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> giveCouponIds;

    private Long seckillActivityId;

    private Long bargainActivityId;

    private Long bargainRecordId;

    private Long combinationActivityId;

    private Long combinationHeadId;

    private Long combinationRecordId;

    private Long pointActivityId;

}
