package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 交易订单 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String no;

    private Integer type;

    private Integer terminal;

    @JsonLongId
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

    @JsonLongId
    private Long brokerageUserId;

    @JsonLongId
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

    @JsonLongId
    private Long logisticsId;

    private String logisticsNo;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private String receiverName;

    private String receiverMobile;

    @JsonLongId
    private Integer receiverAreaId;

    private String receiverDetailAddress;

    @JsonLongId
    private Long pickUpStoreId;

    private String pickUpVerifyCode;

    private Integer refundStatus;

    private Integer refundPrice;

    @JsonLongId
    private Long couponId;

    private Integer couponPrice;

    private Integer usePoint;

    private Integer pointPrice;

    private Integer givePoint;

    private Integer refundPoint;

    private Integer vipPrice;

    private Map<Long, Integer> giveCouponTemplateCounts;

    @JsonLongId
    private List<Long> giveCouponIds;

    @JsonLongId
    private Long seckillActivityId;

    @JsonLongId
    private Long bargainActivityId;

    @JsonLongId
    private Long bargainRecordId;

    @JsonLongId
    private Long combinationActivityId;

    @JsonLongId
    private Long combinationHeadId;

    @JsonLongId
    private Long combinationRecordId;

    @JsonLongId
    private Long pointActivityId;

    private List<TradeOrderItemVo> items;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
