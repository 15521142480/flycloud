package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 频道业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 频道业务对象")
@Data
@Accessors(chain = true)
public class ImChannelBo {

    @Schema(description = "频道编号", example = "1001")
    private Long id;

    @Schema(description = "频道编码", example = "notice")
    private String code;

    @Schema(description = "频道名称", example = "公告")
    private String name;

    @Schema(description = "频道头像")
    private String avatar;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;

}
