package com.fly.system.api.im.domain.bo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * IM 群业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 群业务对象")
@Data
@Accessors(chain = true)
public class ImGroupBo {

    @Schema(description = "群编号", example = "1003")
    private Long id;

    @Schema(description = "群编号", example = "1024")
    private Long groupId;

    @Schema(description = "用户编号", example = "2048")
    private Long userId;

    @Schema(description = "目标用户编号列表", example = "[101, 102]")
    private List<Long> userIds;

    @Schema(description = "成员用户编号列表", example = "[1, 2, 3]")
    private List<Long> memberUserIds;

    @Schema(description = "新群主用户编号", example = "202")
    private Long newOwnerUserId;

    @Schema(description = "群名称", example = "flycloud技术交流群")
    private String name;

    @Schema(description = "群主用户编号", example = "31460")
    private Long ownerUserId;

    @Schema(description = "群头像")
    private String avatar;

    @Schema(description = "群公告")
    private String notice;

    @Schema(description = "进群是否需群主 / 管理员审批", example = "true")
    private Boolean joinApproval;

    @Schema(description = "是否全群禁言", example = "true")
    private Boolean mutedAll;

    @Schema(description = "禁言时长（秒），0 表示永久禁言", example = "600")
    private Integer mutedSeconds;

    @Schema(description = "封禁原因", example = "违规内容")
    private String reason;

    @Schema(description = "申请理由", example = "我是张三")
    private String applyContent;

    @Schema(description = "加入来源", example = "1")
    private Integer addSource;

    @AssertTrue(message = "群名称不能为空")
    @JsonIgnore
    public boolean isNameValid() {
        return name == null || StrUtil.isNotBlank(name);
    }

    @AssertTrue(message = "群头像不能为空")
    @JsonIgnore
    public boolean isAvatarValid() {
        return avatar == null || StrUtil.isNotBlank(avatar);
    }

}
