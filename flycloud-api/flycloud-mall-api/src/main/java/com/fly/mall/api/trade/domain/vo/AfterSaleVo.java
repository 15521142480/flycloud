package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 售后单 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class AfterSaleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String no;

    private Integer status;

    private Integer way;

    private Integer type;

    private Long userId;

    private String applyReason;

    private String applyDescription;

    private List<String> applyPicUrls;

    private Long orderId;

    private String orderNo;

    private Long orderItemId;

    private Long spuId;

    private String spuName;

    private Long skuId;

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

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
