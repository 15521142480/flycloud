package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import com.fly.mall.api.product.domain.vo.AppProductPropertyValueDetailRespVo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 移动端 - 交易订单项响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppTradeOrderItemRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long orderId;

    @JsonLongId
    private Long spuId;

    private String spuName;

    @JsonLongId
    private Long skuId;

    private List<AppProductPropertyValueDetailRespVo> properties;

    private String picUrl;

    private Integer count;

    private Boolean commentStatus;

    private Integer price;

    private Integer payPrice;

    @JsonLongId
    private Long afterSaleId;

    private Integer afterSaleStatus;
}
