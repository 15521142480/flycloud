package com.fly.system.api.im.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * IM 群成员业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 群成员业务对象")
@Data
@Accessors(chain = true)
public class ImGroupMemberBo {

    @Schema(description = "群编号", example = "13279")
    private Long groupId;

    @Schema(description = "用户编号", example = "21730")
    private Long userId;

    @Schema(description = "成员用户编号列表", example = "[1, 2, 3]")
    private List<Long> memberUserIds;

    @Schema(description = "群内昵称", example = "芋头")
    private String displayUserName;

    @Schema(description = "群备注", example = "公司群")
    private String groupRemark;

    @Schema(description = "是否免打扰")
    private Boolean silent;

}
