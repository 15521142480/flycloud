package com.fly.im.controller.admin.group;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.fly.im.framework.enums.CommonStatusEnum;
import com.fly.common.domain.model.R;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.group.vo.request.ImGroupRequestApplyReqVo;
import com.fly.im.controller.admin.group.vo.request.ImGroupRequestRespVo;
import com.fly.im.dal.dataobject.group.ImGroupDO;
import com.fly.im.dal.dataobject.group.ImGroupMemberDO;
import com.fly.im.dal.dataobject.group.ImGroupRequestDO;
import com.fly.im.enums.group.ImGroupMemberRoleEnum;
import com.fly.im.service.group.ImGroupMemberService;
import com.fly.im.service.group.ImGroupRequestService;
import com.fly.im.service.group.ImGroupService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.*;
import static com.fly.common.security.util.UserUtils.getCurUserId;

/**
 * IM 加群申请 Controller
 *
 * @author lxs
 * @date 2026-06-30
 */
@Tag(name = "管理后台 - IM 加群申请")
@RestController
@RequestMapping({"/im/group-request", "/admin/im/group-request"})
@Validated
public class ImGroupRequestController {

    @Resource
    private ImGroupRequestService groupRequestService;
    @Resource
    private ImGroupService groupService;
    @Resource
    private ImGroupMemberService groupMemberService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/apply")
    @Operation(summary = "申请加群")
    public R<Long> applyJoinGroup(@Valid @RequestBody ImGroupRequestApplyReqVo reqVo) {
        ImGroupRequestDO request = groupRequestService.applyJoinGroup(getCurUserId(), reqVo);
        return ok(request != null ? request.getId() : null);
    }

    @PutMapping("/agree")
    @Operation(summary = "同意加群申请（群主或管理员）")
    @Parameter(name = "id", description = "申请编号", required = true, example = "1024")
    public R<Boolean> agreeGroupRequest(
            @RequestParam("id") @NotNull(message = "申请编号不能为空") Long id) {
        groupRequestService.agreeGroupRequest(getCurUserId(), id);
        return ok(true);
    }

    @PutMapping("/refuse")
    @Operation(summary = "拒绝加群申请（群主或管理员）")
    public R<Boolean> refuseGroupRequest(
            @RequestParam("id") @NotNull(message = "申请编号不能为空") Long id,
            @RequestParam(value = "handleContent", required = false)
            @Size(max = 255, message = "处理理由最多 255 个字符") String handleContent) {
        groupRequestService.refuseGroupRequest(getCurUserId(), id, handleContent);
        return ok(true);
    }

    @GetMapping("/unhandled-list")
    @Operation(summary = "查询「我管理的所有群」下的未处理加群申请列表（不分页）；前端 store 据此派生横幅红点 + Drawer 列表")
    public R<List<ImGroupRequestRespVo>> getUnhandledRequestList() {
        List<ImGroupRequestDO> list = groupRequestService.getUnhandledRequestListByOwnerOrAdmin(getCurUserId());
        return ok(buildVOList(list));
    }

    @GetMapping("/list-by-group")
    @Operation(summary = "查询指定群下的全部加群申请（含已处理）；仅群主 / 管理员可查")
    @Parameter(name = "groupId", description = "群编号", required = true, example = "1024")
    public R<List<ImGroupRequestRespVo>> getGroupRequestListByGroupId(
            @RequestParam("groupId") @NotNull(message = "群编号不能为空") Long groupId) {
        List<ImGroupRequestDO> list = groupRequestService.getGroupRequestListByGroupId(getCurUserId(), groupId);
        return ok(buildVOList(list));
    }

    @GetMapping("/get")
    @Operation(summary = "按 id 单查申请记录（带越权过滤；WebSocket 通知到达后用）")
    @Parameter(name = "id", description = "申请记录编号", required = true)
    public R<ImGroupRequestRespVo> getGroupRequest(@RequestParam("id") Long id) {
        ImGroupRequestDO request = groupRequestService.getGroupRequest(id);
        if (request == null) {
            return ok((ImGroupRequestRespVo) null);
        }
        // 越权过滤：申请人 / 邀请人 / 群主 / 管理员之外，当不存在返回 null
        Long currentUserId = getCurUserId();
        boolean canSee = ObjUtil.equal(request.getUserId(), currentUserId)
                || ObjUtil.equal(request.getInviterUserId(), currentUserId)
                || isGroupOwnerOrAdmin(request.getGroupId(), currentUserId);
        if (!canSee) {
            return ok((ImGroupRequestRespVo) null);
        }

        // 转换并返回
        return ok(CollUtil.getFirst(buildVOList(Collections.singletonList(request))));
    }

    /**
     * 当前用户是否该群的有效群主 / 管理员
     */
    private boolean isGroupOwnerOrAdmin(Long groupId, Long userId) {
        ImGroupMemberDO member = groupMemberService.getGroupMember(groupId, userId);
        return member != null
                && !CommonStatusEnum.DISABLE.getStatus().equals(member.getStatus())
                && ImGroupMemberRoleEnum.isOwnerOrAdmin(member.getRole());
    }

    /** 申请记录列表批量转 VO + 关联回填用户 / 群信息 */
    private List<ImGroupRequestRespVo> buildVOList(List<ImGroupRequestDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 1. 聚合 user / inviter 用户信息；convertSetByFlatMap 内部已过滤 null
        Set<Long> userIds = convertSetByFlatMap(list,
                request -> Stream.of(request.getUserId(), request.getInviterUserId()));
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        // 2. 聚合群信息（封禁 / 解散群也要回填，便于前端展示历史）
        Set<Long> groupIds = convertSet(list, ImGroupRequestDO::getGroupId);
        Map<Long, ImGroupDO> groupMap = groupService.getGroupMap(groupIds);
        return convertList(list, request -> {
            ImGroupRequestRespVo vo = BeanUtils.toBean(request, ImGroupRequestRespVo.class);
            MapUtils.findAndThen(userMap, request.getUserId(), user ->
                    vo.setUserNickname(user.getName()).setUserAvatar(user.getAvatar()));
            MapUtils.findAndThen(userMap, request.getInviterUserId(), user ->
                    vo.setInviterNickname(user.getName()).setInviterAvatar(user.getAvatar()));
            MapUtils.findAndThen(groupMap, request.getGroupId(), group ->
                    vo.setGroupName(group.getName()).setGroupAvatar(group.getAvatar()));
            return vo;
        });
    }

}
