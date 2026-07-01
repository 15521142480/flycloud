package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 交易订单 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeOrderBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "no")
    private String no;

    @Schema(description = "type")
    private Integer type;

    @Schema(description = "terminal")
    private Integer terminal;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "userIp")
    private String userIp;

    @Schema(description = "userRemark")
    private String userRemark;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "productCount")
    private Integer productCount;

    @Schema(description = "finishTime")
    private LocalDateTime finishTime;

    @Schema(description = "cancelTime")
    private LocalDateTime cancelTime;

    @Schema(description = "cancelType")
    private Integer cancelType;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "commentStatus")
    private Boolean commentStatus;

    @Schema(description = "brokerageUserId")
    private Long brokerageUserId;

    @Schema(description = "payOrderId")
    private Long payOrderId;

    @Schema(description = "payStatus")
    private Boolean payStatus;

    @Schema(description = "payTime")
    private LocalDateTime payTime;

    @Schema(description = "payChannelCode")
    private String payChannelCode;

    @Schema(description = "totalPrice")
    private Integer totalPrice;

    @Schema(description = "discountPrice")
    private Integer discountPrice;

    @Schema(description = "deliveryPrice")
    private Integer deliveryPrice;

    @Schema(description = "adjustPrice")
    private Integer adjustPrice;

    @Schema(description = "payPrice")
    private Integer payPrice;

    @Schema(description = "deliveryType")
    private Integer deliveryType;

    @Schema(description = "logisticsId")
    private Long logisticsId;

    @Schema(description = "logisticsNo")
    private String logisticsNo;

    @Schema(description = "deliveryTime")
    private LocalDateTime deliveryTime;

    @Schema(description = "receiveTime")
    private LocalDateTime receiveTime;

    @Schema(description = "receiverName")
    private String receiverName;

    @Schema(description = "receiverMobile")
    private String receiverMobile;

    @Schema(description = "receiverAreaId")
    private Integer receiverAreaId;

    @Schema(description = "receiverDetailAddress")
    private String receiverDetailAddress;

    @Schema(description = "pickUpStoreId")
    private Long pickUpStoreId;

    @Schema(description = "pickUpVerifyCode")
    private String pickUpVerifyCode;

    @Schema(description = "refundStatus")
    private Integer refundStatus;

    @Schema(description = "refundPrice")
    private Integer refundPrice;

    @Schema(description = "couponId")
    private Long couponId;

    @Schema(description = "couponPrice")
    private Integer couponPrice;

    @Schema(description = "usePoint")
    private Integer usePoint;

    @Schema(description = "pointPrice")
    private Integer pointPrice;

    @Schema(description = "givePoint")
    private Integer givePoint;

    @Schema(description = "refundPoint")
    private Integer refundPoint;

    @Schema(description = "vipPrice")
    private Integer vipPrice;

    @Schema(description = "giveCouponTemplateCounts")
    private Map<Long, Integer> giveCouponTemplateCounts;

    @Schema(description = "giveCouponIds")
    private List<Long> giveCouponIds;

    @Schema(description = "seckillActivityId")
    private Long seckillActivityId;

    @Schema(description = "bargainActivityId")
    private Long bargainActivityId;

    @Schema(description = "bargainRecordId")
    private Long bargainRecordId;

    @Schema(description = "combinationActivityId")
    private Long combinationActivityId;

    @Schema(description = "combinationHeadId")
    private Long combinationHeadId;

    @Schema(description = "combinationRecordId")
    private Long combinationRecordId;

    @Schema(description = "pointActivityId")
    private Long pointActivityId;

    @Schema(description = "下单购物车编号集合；为空时默认使用当前用户已选中购物车商品")
    private List<Long> cartIds;

}
