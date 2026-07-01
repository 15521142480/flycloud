package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易订单项 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "trade_order_item", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "交易订单项对象", description = "交易订单项表")
public class TradeOrderItem extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Long orderId;

    private Long cartId;

    private Long spuId;

    private String spuName;

    private Long skuId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Property> properties;

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

//    private Long propertyId;
//
//    private String propertyName;
//
//    private Long valueId;
//
//    private String valueName;

    /**
     * 订单项商品规格属性。
     *
     * @author lxs
     * @date 2026-07-02
     */
    @Data
    public static class Property implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 属性编号。
         */
        private Long propertyId;

        /**
         * 属性名称。
         */
        private String propertyName;

        /**
         * 属性值编号。
         */
        private Long valueId;

        /**
         * 属性值名称。
         */
        private String valueName;
    }

}
