package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 售后单 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AfterSaleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String no;

    private Integer status;

    private Integer way;

    private Integer type;

    @JsonLongId
    private Long userId;

    private String applyReason;

    private String applyDescription;

    private List<String> applyPicUrls;

    @JsonLongId
    private Long orderId;

    private String orderNo;

    @JsonLongId
    private Long orderItemId;

    @JsonLongId
    private Long spuId;

    private String spuName;

    @JsonLongId
    private Long skuId;

    private List<Object> properties;

    private String picUrl;

    private Integer count;

    private LocalDateTime auditTime;

    @JsonLongId
    private Long auditUserId;

    private String auditReason;

    private Integer refundPrice;

    @JsonLongId
    private Long payRefundId;

    private LocalDateTime refundTime;

    @JsonLongId
    private Long logisticsId;

    private String logisticsNo;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private String receiveReason;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
