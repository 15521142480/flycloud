package com.fly.im.controller.admin.friend.vo.request;

import com.fly.im.framework.validation.InEnum;
import com.fly.im.enums.friend.ImFriendAddSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 好友申请 - 发起 Request VO
 *
 * @author lxs
 * @date 2026-06-30
 */
@Schema(description = "管理后台 - IM 好友申请发起 Request VO")
@Data
@Accessors(chain = true)
public class ImFriendRequestApplyReqVo {

    @Schema(description = "接收方用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "接收方用户编号不能为空")
    private Long toUserId;

    @Schema(description = "申请理由", example = "我是张三")
    @Size(max = 255, message = "申请理由最多 255 个字符")
    private String applyContent;

    @Schema(description = "对接收方的备注（仅自己可见）", example = "老张")
    @Size(max = 16, message = "好友备注最多 16 个字符")
    private String displayName;

    @Schema(description = "添加来源", example = "1")
    @InEnum(ImFriendAddSourceEnum.class)
    private Integer addSource;

}
