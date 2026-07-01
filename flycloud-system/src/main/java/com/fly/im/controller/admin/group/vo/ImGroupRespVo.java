package com.fly.im.controller.admin.group.vo;

import com.fly.im.controller.admin.message.vo.group.ImGroupMessageRespVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 群 Response VO")
@Data
@Accessors(chain = true)
public class ImGroupRespVo {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1003")
    private Long id;

    @Schema(description = "群名称", example = "张三")
    private String name;

    @Schema(description = "群主用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "31460")
    private Long ownerUserId;

    @Schema(description = "群头像")
    private String avatar;

    @Schema(description = "群公告")
    private String notice;

    @Schema(description = "是否封禁")
    private Boolean banned;

    @Schema(description = "是否全群禁言")
    private Boolean mutedAll;

    @Schema(description = "进群是否需群主 / 管理员审批", example = "false")
    private Boolean joinApproval;

    @Schema(description = "封禁时间")
    private LocalDateTime bannedTime;

    @Schema(description = "群状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "解散时间")
    private LocalDateTime dissolvedTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "群置顶消息列表，按 pin 顺序（最先置顶的在前）；非该群有效成员时为空")
    private List<ImGroupMessageRespVo> pinnedMessages;

}
