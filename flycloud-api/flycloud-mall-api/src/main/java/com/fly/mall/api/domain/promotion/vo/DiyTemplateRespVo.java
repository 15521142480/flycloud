package com.fly.mall.api.domain.promotion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 管理后台 - 装修模板响应对象。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Data
@Schema(name = "DiyTemplateRespVo", description = "管理后台 - 装修模板响应对象")
public class DiyTemplateRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "装修模板编号")
    private Long id;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "预览图地址列表")
    private List<String> previewPicUrls;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "是否使用")
    private Boolean used;

    @Schema(description = "使用时间")
    private LocalDateTime usedTime;

}
