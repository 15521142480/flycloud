package com.fly.mall.api.promotion.domain.bo;

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

    @Schema(description = "装修模板编号")
    private Long id;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "是否使用")
    private Boolean used;

    @Schema(description = "使用时间")
    private LocalDateTime usedTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "预览图地址列表")
    private List<String> previewPicUrls;

    @Schema(description = "模板属性 JSON")
    private String property;

}
