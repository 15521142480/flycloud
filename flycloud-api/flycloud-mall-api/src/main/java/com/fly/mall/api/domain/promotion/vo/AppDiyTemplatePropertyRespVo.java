package com.fly.mall.api.domain.promotion.vo;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 移动端 - 装修模板属性响应对象。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Data
@Schema(name = "AppDiyTemplatePropertyRespVo", description = "移动端 - 装修模板属性响应对象")
public class AppDiyTemplatePropertyRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "装修模板编号")
    private Long id;

    @Schema(description = "模板名称")
    private String name;

    @JsonRawValue
    @Schema(description = "模板属性 JSON")
    private String property;

    @JsonRawValue
    @Schema(description = "首页装修属性 JSON")
    private String home;

    @JsonRawValue
    @Schema(description = "我的页面装修属性 JSON")
    private String user;

}
