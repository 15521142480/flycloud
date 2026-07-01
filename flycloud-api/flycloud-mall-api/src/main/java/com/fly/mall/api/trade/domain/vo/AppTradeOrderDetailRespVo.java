package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 移动端 - 交易订单详情响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppTradeOrderDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String no;

    private Integer type;

    private LocalDateTime createTime;

    private String userRemark;

    private Integer status;

    private Integer productCount;

    private LocalDateTime finishTime;

    private LocalDateTime cancelTime;

    private Boolean commentStatus;

    private Boolean payStatus;

    private Long payOrderId;

    private LocalDateTime payTime;

    private LocalDateTime payExpireTime;

    private String payChannelCode;

    private String payChannelName;

    private Integer totalPrice;

    private Integer discountPrice;

    private Integer deliveryPrice;

    private Integer adjustPrice;

    private Integer payPrice;

    private Integer deliveryType;

    private Long logisticsId;

    private String logisticsName;

    private String logisticsNo;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private String receiverName;

    private String receiverMobile;

    private Integer receiverAreaId;

    private String receiverAreaName;

    private String receiverDetailAddress;

    private Long pickUpStoreId;

    private String pickUpVerifyCode;

    private Integer refundStatus;

    private Integer refundPrice;

    private Long couponId;

    private Integer couponPrice;

    private Integer pointPrice;

    private Integer vipPrice;

    private Long combinationRecordId;

    private List<AppTradeOrderItemRespVo> items;
}
