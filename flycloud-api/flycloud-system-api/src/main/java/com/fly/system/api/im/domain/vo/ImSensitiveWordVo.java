package com.fly.system.api.im.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - IM 敏感词 Response VO")
@Data
@Accessors(chain = true)
public class ImSensitiveWordVo {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @JsonLongId
    private Long id;

    @Schema(description = "敏感词", requiredMode = Schema.RequiredMode.REQUIRED, example = "敏感词内容")
    private String word;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status; // 参见 CommonStatusEnum 枚举类

    @Schema(description = "创建人")
    private String creator;
    @Schema(description = "创建人昵称", example = "张三")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
