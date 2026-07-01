package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 装修页面 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DiyPageBo extends BaseEntity {

    @Schema(description = "装修页面编号")
    private Long id;

    @Schema(description = "装修模板编号")
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
