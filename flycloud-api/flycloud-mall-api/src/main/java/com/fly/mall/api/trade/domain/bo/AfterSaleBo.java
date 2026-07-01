package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 售后单 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AfterSaleBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "no")
    private String no;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "way")
    private Integer way;

    @Schema(description = "type")
    private Integer type;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "applyReason")
    private String applyReason;

    @Schema(description = "applyDescription")
    private String applyDescription;

    @Schema(description = "applyPicUrls")
    private List<String> applyPicUrls;

    @Schema(description = "orderId")
    private Long orderId;

    @Schema(description = "orderNo")
    private String orderNo;

    @Schema(description = "orderItemId")
    private Long orderItemId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "spuName")
    private String spuName;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "properties")
    private List<Object> properties;

    @Schema(description = "picUrl")
    private String picUrl;

    @Schema(description = "count")
    private Integer count;

    @Schema(description = "auditTime")
    private LocalDateTime auditTime;

    @Schema(description = "auditUserId")
    private Long auditUserId;

    @Schema(description = "auditReason")
    private String auditReason;

    @Schema(description = "refundPrice")
    private Integer refundPrice;

    @Schema(description = "payRefundId")
    private Long payRefundId;

    @Schema(description = "refundTime")
    private LocalDateTime refundTime;

    @Schema(description = "logisticsId")
    private Long logisticsId;

    @Schema(description = "logisticsNo")
    private String logisticsNo;

    @Schema(description = "deliveryTime")
    private LocalDateTime deliveryTime;

    @Schema(description = "receiveTime")
    private LocalDateTime receiveTime;

    @Schema(description = "receiveReason")
    private String receiveReason;

}
