package com.fly.system.api.im.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * IM 会话读位置 Response VO
 *
 */
@Schema(description = "管理后台 - IM 会话读位置 Response VO")
@Data
public class ImConversationReadVo {

    @Schema(description = "读位置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @JsonLongId
    private Long id;

    @Schema(description = "会话类型", example = "1")
    private Integer conversationType; // 参见 ImConversationTypeEnum 枚举

    @Schema(description = "目标编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @JsonLongId
    private Long targetId;

    @Schema(description = "最大已读消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4096")
    @JsonLongId
    private Long messageId;

    @Schema(description = "最近更新时间（增量拉取游标用）")
    private LocalDateTime updateTime;

}
