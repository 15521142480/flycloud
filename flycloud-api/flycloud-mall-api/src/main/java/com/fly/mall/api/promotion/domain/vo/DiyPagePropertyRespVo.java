package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 管理后台 - 装修页面属性响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(name = "DiyPagePropertyRespVo", description = "管理后台 - 装修页面属性响应对象")
public class DiyPagePropertyRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "装修页面编号")
    @JsonLongId
    private Long id;

    @Schema(description = "装修模板编号")
    @JsonLongId
    private Long templateId;

    @Schema(description = "页面名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "预览图地址列表")
    private List<String> previewPicUrls;

    @Schema(description = "页面属性 JSON")
    private String property;

}
