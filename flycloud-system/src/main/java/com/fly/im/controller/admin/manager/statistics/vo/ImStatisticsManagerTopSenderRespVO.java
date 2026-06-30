package com.fly.im.controller.admin.manager.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IM 数据看板 TOP 发送者项 Response VO")
@Data
@Accessors(chain = true)
public class ImStatisticsManagerTopSenderRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long userId;

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "消息数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1500")
    private Long messageCount;

}
