package com.fly.mall.api.trade.domain.vo;

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

    private Long id;

    private Long userId;

    private Long orderId;

    private Long cartId;

    private Long spuId;

    private String spuName;

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

    private Long afterSaleId;

    private Integer afterSaleStatus;

    private Long propertyId;

    private String propertyName;

    private Long valueId;

    private String valueName;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
