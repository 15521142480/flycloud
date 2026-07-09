package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 个人表情业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 个人表情业务对象")
@Data
@Accessors(chain = true)
public class ImFaceUserItemBo {

    @Schema(description = "表情图 URL")
    private String url;

    @Schema(description = "表情名")
    private String name;

    @Schema(description = "渲染宽度")
    private Integer width;

    @Schema(description = "渲染高度")
    private Integer height;

}
