package com.fly.mall.api.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 商品属性值明细返回对象。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Data
@Schema(description = "移动端 - 商品属性值明细返回对象")
public class AppProductPropertyValueDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "属性编号")
    private Long propertyId;

    @Schema(description = "属性名称")
    private String propertyName;

    @Schema(description = "属性值编号")
    private Long valueId;

    @Schema(description = "属性值名称")
    private String valueName;

}
