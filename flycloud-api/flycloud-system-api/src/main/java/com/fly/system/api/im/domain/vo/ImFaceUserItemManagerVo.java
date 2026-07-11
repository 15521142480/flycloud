package com.fly.system.api.im.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - IM 用户表情 Response VO")
@Data
@Accessors(chain = true)
public class ImFaceUserItemManagerVo {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4096")
    @JsonLongId
    private Long id;

    @Schema(description = "所属用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @JsonLongId
    private Long userId;

    @Schema(description = "所属用户昵称", example = "张三")
    private String userNickname;

    @Schema(description = "表情图 URL", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "https://cdn.example.com/face/user/abc.gif")
    private String url;

    @Schema(description = "表情名", example = "狗头")
    private String name;

    @Schema(description = "渲染宽度（像素）", example = "200")
    private Integer width;

    @Schema(description = "渲染高度（像素）", example = "200")
    private Integer height;

    @Schema(description = "添加时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
