package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 表情包业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 表情包业务对象")
@Data
@Accessors(chain = true)
public class ImFacePackBo {

    @Schema(description = "表情包编号", example = "1001")
    private Long id;

    @Schema(description = "表情包名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;

}
