package com.fly.mall.api.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台 - 商品属性值响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "后台 - 商品属性值响应对象")
public class ProductPropertyValueRespVo extends ProductPropertyValueVo {
    private static final long serialVersionUID = 1L;
}
