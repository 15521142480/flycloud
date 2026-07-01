package com.fly.im.controller.admin.group;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.common.domain.model.R;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.group.member.ImGroupMemberRespVo;
import com.fly.system.api.im.domain.vo.admin.group.member.ImGroupMemberUpdateReqVo;
import com.fly.system.api.im.domain.group.ImGroupMember;
import com.fly.im.service.group.ImGroupMemberService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.security.util.UserUtils.getCurUserId;
import static com.fly.system.api.im.enums.ErrorCodeConstants.GROUP_MEMBER_NOT_IN_GROUP;

@Tag(name = "管理后台 - 群成员")
@RestController
@RequestMapping({"/im/group-member", "/admin/im/group-member"})
@Validated
public class ImGroupMemberController {

    @Resource
    private ImGroupMemberService groupMemberService;

    @Resource
    private AdminUserApi adminUserApi;

    @PutMapping("/update")
    @Operation(summary = "更新群成员")
    public R<Boolean> updateGroupMember(@Valid @RequestBody ImGroupMemberUpdateReqVo updateReqVo) {
        groupMemberService.updateGroupMember(getCurUserId(), updateReqVo);
        return ok(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得群成员")
    @Parameters({
            @Parameter(name = "id", description = "编号（与 groupId + userId 二选一）", example = "1024"),
            @Parameter(name = "groupId", description = "群编号（与 userId 配合查）", example = "1"),
            @Parameter(name = "userId", description = "用户编号（与 groupId 配合查）", example = "100")
    })
    public R<ImGroupMemberRespVo> getGroupMember(@RequestParam(value = "id", required = false) Long id,
                                                            @RequestParam(value = "groupId", required = false) Long groupId,
                                                            @RequestParam(value = "userId", required = false) Long userId) {
        // 1. 查询群成员
        ImGroupMember member;
        if (id != null) {
            member = groupMemberService.getGroupMember(id);
        } else if (groupId != null && userId != null) {
            member = groupMemberService.getGroupMember(groupId, userId);
        } else {
            // 避免 selectByGroupIdAndUserId 收到 null 参数走全表扫 / 抛 SQL 异常
            throw new IllegalArgumentException("参数缺失：需传 id 或 (groupId, userId)");
        }
        if (member == null) {
            return ok((ImGroupMemberRespVo) null);
        }

        // 2. 校验当前登录用户是该成员所在群的有效成员
        Long loginUserId = getCurUserId();
        groupMemberService.validateMemberInGroup(member.getGroupId(), loginUserId);

        // 3. 转化 VO
        ImGroupMemberRespVo memberVo = BeanUtils.toBean(member, ImGroupMemberRespVo.class);
        SysUserVo user = adminUserApi.getUser(member.getUserId()).getCheckedData();
        if (user != null) {
            memberVo.setNickname(user.getName()).setAvatar(user.getAvatar());
        }
        hidePrivateFieldsIfNotSelf(memberVo, member.getUserId(), loginUserId);
        return ok(memberVo);
    }

    @GetMapping("/list")
    @Operation(summary = "获得指定群的成员列表")
    @Parameter(name = "groupId", description = "群编号", required = true, example = "1024")
    public R<List<ImGroupMemberRespVo>> getGroupMemberList(@RequestParam("groupId") Long groupId) {
        // 1.1 查询群成员列表（包含 DISABLE 已退群的成员，不按时间过滤）
        // 说明：保留已退群成员，是为了前端展示历史消息时，仍能通过该接口拿到已退群成员的昵称 / 头像信息，避免显示为空
        List<ImGroupMember> members = groupMemberService.getGroupMemberListByGroupId(groupId);
        // 1.2 校验当前登录用户是否为群的有效成员，非成员不可查看
        Long loginUserId = getCurUserId();
        if (CollUtil.findOne(members, member -> loginUserId.equals(member.getUserId())
                && CommonStatusEnum.ENABLE.getStatus().equals(member.getStatus())) == null) {
            throw exception(GROUP_MEMBER_NOT_IN_GROUP);
        }

        // 2.批量聚合 AdminUser 信息（昵称 / 头像）
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                convertList(members, ImGroupMember::getUserId));
        return ok(convertList(members, m -> {
            ImGroupMemberRespVo vo = BeanUtils.toBean(m, ImGroupMemberRespVo.class);
            MapUtils.findAndThen(userMap, m.getUserId(), user ->
                    vo.setNickname(user.getName()).setAvatar(user.getAvatar()));
            hidePrivateFieldsIfNotSelf(vo, m.getUserId(), loginUserId);
            return vo;
        }));
    }

    /**
     * 非本人查看时，置空成员的私人设置字段（groupRemark / silent）
     */
    private void hidePrivateFieldsIfNotSelf(ImGroupMemberRespVo vo, Long memberUserId, Long loginUserId) {
        if (ObjUtil.notEqual(loginUserId, memberUserId)) {
            vo.setGroupRemark(null).setSilent(null);
        }
    }

}
