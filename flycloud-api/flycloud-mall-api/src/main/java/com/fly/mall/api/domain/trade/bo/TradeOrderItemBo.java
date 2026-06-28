package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import com.fly.mall.api.domain.trade.TradeOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 交易订单项 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class TradeOrderItemBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "orderId")
    private Long orderId;

    @Schema(description = "cartId")
    private Long cartId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "spuName")
    private String spuName;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "properties")
    private List<TradeOrderItem.Property> properties;

    @Schema(description = "picUrl")
    private String picUrl;

    @Schema(description = "count")
    private Integer count;

    @Schema(description = "commentStatus")
    private Boolean commentStatus;

    @Schema(description = "price")
    private Integer price;

    @Schema(description = "discountPrice")
    private Integer discountPrice;

    @Schema(description = "deliveryPrice")
    private Integer deliveryPrice;

    @Schema(description = "adjustPrice")
    private Integer adjustPrice;

    @Schema(description = "payPrice")
    private Integer payPrice;

    @Schema(description = "couponPrice")
    private Integer couponPrice;

    @Schema(description = "pointPrice")
    private Integer pointPrice;

    @Schema(description = "usePoint")
    private Integer usePoint;

    @Schema(description = "givePoint")
    private Integer givePoint;

    @Schema(description = "vipPrice")
    private Integer vipPrice;

    @Schema(description = "afterSaleId")
    private Long afterSaleId;

    @Schema(description = "afterSaleStatus")
    private Integer afterSaleStatus;

    @Schema(description = "propertyId")
    private Long propertyId;

    @Schema(description = "propertyName")
    private String propertyName;

    @Schema(description = "valueId")
    private Long valueId;

    @Schema(description = "valueName")
    private String valueName;

}
