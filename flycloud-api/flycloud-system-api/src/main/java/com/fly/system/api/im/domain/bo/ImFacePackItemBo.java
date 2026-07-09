package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 表情包明细业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 表情包明细业务对象")
@Data
@Accessors(chain = true)
public class ImFacePackItemBo {

    @Schema(description = "表情编号", example = "1001")
    private Long id;

    @Schema(description = "表情包编号", example = "1001")
    private Long packId;

    @Schema(description = "表情图 URL")
    private String url;

    @Schema(description = "表情名")
    private String name;

    @Schema(description = "渲染宽度")
    private Integer width;

    @Schema(description = "渲染高度")
    private Integer height;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;

}
