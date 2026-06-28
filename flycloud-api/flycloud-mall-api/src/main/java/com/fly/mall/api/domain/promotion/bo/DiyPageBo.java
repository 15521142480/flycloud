package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 装修页面 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DiyPageBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "templateId")
    private Long templateId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "previewPicUrls")
    private List<String> previewPicUrls;

    @Schema(description = "property")
    private String property;

}
