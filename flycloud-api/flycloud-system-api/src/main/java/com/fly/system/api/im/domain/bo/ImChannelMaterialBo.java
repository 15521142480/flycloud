package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 频道素材业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 频道素材业务对象")
@Data
@Accessors(chain = true)
public class ImChannelMaterialBo {

    @Schema(description = "素材编号", example = "1001")
    private Long id;

    @Schema(description = "频道编号", example = "1001")
    private Long channelId;

    @Schema(description = "素材类型")
    private Integer type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "封面地址")
    private String coverUrl;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "跳转地址")
    private String url;

}
