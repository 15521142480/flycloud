package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 移动端 - 装修页面属性响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(name = "AppDiyPagePropertyRespVo", description = "移动端 - 装修页面属性响应对象")
public class AppDiyPagePropertyRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "装修页面编号")
    @JsonLongId
    private Long id;

    @Schema(description = "页面名称")
    private String name;

    @JsonRawValue
    @Schema(description = "页面属性 JSON")
    private String property;

}
