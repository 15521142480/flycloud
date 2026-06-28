package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 装修模板 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DiyTemplateBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "used")
    private Boolean used;

    @Schema(description = "usedTime")
    private LocalDateTime usedTime;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "previewPicUrls")
    private List<String> previewPicUrls;

    @Schema(description = "property")
    private String property;

}
