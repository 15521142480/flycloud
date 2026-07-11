package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import com.fly.mall.api.trade.domain.TradeOrderItem;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 交易订单项 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeOrderItemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long userId;

    @JsonLongId
    private Long orderId;

    @JsonLongId
    private Long cartId;

    @JsonLongId
    private Long spuId;

    private String spuName;

    @JsonLongId
    private Long skuId;

    private List<TradeOrderItem.Property> properties;

    private String picUrl;

    private Integer count;

    private Boolean commentStatus;

    private Integer price;

    private Integer discountPrice;

    private Integer deliveryPrice;

    private Integer adjustPrice;

    private Integer payPrice;

    private Integer couponPrice;

    private Integer pointPrice;

    private Integer usePoint;

    private Integer givePoint;

    private Integer vipPrice;

    @JsonLongId
    private Long afterSaleId;

    private Integer afterSaleStatus;

    @JsonLongId
    private Long propertyId;

    private String propertyName;

    @JsonLongId
    private Long valueId;

    private String valueName;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
