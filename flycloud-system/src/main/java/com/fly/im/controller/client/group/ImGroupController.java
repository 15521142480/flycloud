package com.fly.im.controller.client.group;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.bo.*;
import com.fly.system.api.im.domain.bo.ImGroupMemberBo;
import com.fly.system.api.im.domain.bo.ImGroupMemberBo;
import com.fly.system.api.im.domain.vo.ImGroupMessageVo;
import com.fly.system.api.im.domain.vo.ImGroupVo;
import com.fly.system.api.im.domain.ImGroup;
import com.fly.system.api.im.domain.ImGroupMember;
import com.fly.system.api.im.domain.ImGroupMessage;
import com.fly.im.service.group.ImGroupMemberService;
import com.fly.im.service.group.ImGroupService;
import com.fly.im.service.message.ImGroupMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.*;
import static com.fly.common.security.util.UserUtils.getCurUserId;

@Tag(name = "管理后台 - 群")
@RestController
@RequestMapping({"/im/group", "/admin/im/group"})
@Validated
public class ImGroupController {

    @Resource
    private ImGroupService groupService;
    @Resource
    private ImGroupMemberService groupMemberService;
    @Resource
    private ImGroupMessageService groupMessageService;



    // ==================== 群的写操作 ====================

    @PostMapping("/create")
    @Operation(summary = "创建群")
    public R<ImGroupVo> createGroup(@Valid @RequestBody ImGroupBo createReqVo) {
        ImGroup group = groupService.createGroup(createReqVo, getCurUserId());
        // 新建群必无 pinnedMessages，跳过关联回填
        ImGroupVo vo = BeanUtils.toBean(group, ImGroupVo.class);
        vo.setAvatar(vo.getAvatar());
        return ok(vo);
    }

    @PutMapping("/update")
    @Operation(summary = "更新群")
    public R<ImGroupVo> updateGroup(@Valid @RequestBody ImGroupBo updateReqVo) {
        ImGroup group = groupService.updateGroup(updateReqVo, getCurUserId());
        return ok(buildGroupVo(group, getCurUserId()));
    }

    @DeleteMapping("/dissolve")
    @Operation(summary = "解散群")
    @Parameter(name = "id", description = "群编号", required = true)
    public R<Boolean> dissolveGroup(@RequestParam("id") Long id) {
        groupService.dissolveGroup(id, getCurUserId());
        return R.result(true);
    }

    // ==================== 群的读操作 ====================

    @GetMapping("/get")
    @Operation(summary = "获得群")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public R<ImGroupVo> getGroup(@RequestParam("id") Long id) {
        ImGroup group = groupService.getGroup(id);
        return ok(buildGroupVo(group, getCurUserId()));
    }

    @GetMapping("/list")
    @Operation(summary = "获得当前登录用户的群列表")
    public R<List<ImGroupVo>> getMyGroupList() {
        Long loginUserId = getCurUserId();
        List<ImGroup> groups = groupService.getMyGroupList(loginUserId);
        return ok(buildGroupVoList(groups, loginUserId));
    }

    // ==================== 群成员的写操作 ====================

    @PostMapping("/invite")
    @Operation(summary = "邀请用户加入群")
    public R<Boolean> inviteGroupMember(@Valid @RequestBody ImGroupMemberBo inviteReqVo) {
        groupService.inviteGroupMember(getCurUserId(), inviteReqVo);
        return R.result(true);
    }

    @DeleteMapping("/quit")
    @Operation(summary = "退出群")
    @Parameter(name = "groupId", description = "群编号", required = true)
    public R<Boolean> quitGroup(@RequestParam("groupId") Long groupId) {
        groupService.quitGroup(groupId, getCurUserId());
        return R.result(true);
    }

    @DeleteMapping("/kicking")
    @Operation(summary = "移除群成员")
    public R<Boolean> removeGroupMember(@Valid @RequestBody ImGroupMemberBo removeReqVo) {
        groupService.removeGroupMember(getCurUserId(), removeReqVo);
        return R.result(true);
    }

    @PutMapping("/add-admin")
    @Operation(summary = "添加群管理员")
    public R<Boolean> addGroupAdmin(@Valid @RequestBody ImGroupBo reqVo) {
        groupService.addGroupAdmin(getCurUserId(), reqVo);
        return R.result(true);
    }

    @PutMapping("/remove-admin")
    @Operation(summary = "撤销群管理员")
    public R<Boolean> removeGroupAdmin(@Valid @RequestBody ImGroupBo reqVo) {
        groupService.removeGroupAdmin(getCurUserId(), reqVo);
        return R.result(true);
    }

    @PutMapping("/transfer-owner")
    @Operation(summary = "转让群主")
    public R<Boolean> transferGroupOwner(@Valid @RequestBody ImGroupBo transferReqVo) {
        groupService.transferGroupOwner(getCurUserId(), transferReqVo);
        return R.result(true);
    }

    // ==================== 群消息置顶 ====================

    @PutMapping("/pin-message")
    @Operation(summary = "置顶群消息（群主 / 管理员）")
    public R<Boolean> pinGroupMessage(@Valid @RequestBody ImGroupMessageBo reqVo) {
        groupService.pinGroupMessage(getCurUserId(), reqVo.getId(), reqVo.getMessageId());
        return R.result(true);
    }

    @PutMapping("/unpin-message")
    @Operation(summary = "取消置顶群消息（群主 / 管理员）")
    public R<Boolean> unpinGroupMessage(@Valid @RequestBody ImGroupMessageBo reqVo) {
        groupService.unpinGroupMessage(getCurUserId(), reqVo.getId(), reqVo.getMessageId());
        return R.result(true);
    }

    // ==================== 群禁言 ====================

    @PutMapping("/mute-all")
    @Operation(summary = "全群禁言 / 取消（群主 / 管理员）")
    public R<Boolean> muteAll(@Valid @RequestBody ImGroupBo reqVo) {
        groupService.muteAll(getCurUserId(), reqVo);
        return R.result(true);
    }

    @PutMapping("/mute-member")
    @Operation(summary = "禁言成员")
    public R<Boolean> muteMember(@Valid @RequestBody ImGroupBo reqVo) {
        groupService.muteMember(getCurUserId(), reqVo);
        return R.result(true);
    }

    @PutMapping("/cancel-mute-member")
    @Operation(summary = "取消成员禁言")
    public R<Boolean> cancelMuteMember(@Valid @RequestBody ImGroupBo reqVo) {
        groupService.cancelMuteMember(getCurUserId(), reqVo);
        return R.result(true);
    }

    /** 单群转 VO + 关联回填 pinnedMessages（仅当登录用户是该群有效成员） */
    private ImGroupVo buildGroupVo(ImGroup group, Long loginUserId) {
        if (group == null) {
            return null;
        }
        return buildGroupVoList(Collections.singletonList(group), loginUserId).get(0);
    }

    /**
     * 群列表批量转 VO + 关联回填 pinnedMessages
     * <p>
     * 仅当登录用户是某群的有效成员时才回填该群的 pinnedMessages，避免非成员 / 已退群用户越权拿到置顶消息内容
     */
    private List<ImGroupVo> buildGroupVoList(List<ImGroup> groups, Long loginUserId) {
        if (CollUtil.isEmpty(groups)) {
            return Collections.emptyList();
        }
        // 仅当前用户是有效成员的群才允许回填置顶消息
        Set<Long> activeGroupIds = convertSet(
                groupMemberService.getActiveGroupMemberListByUserId(loginUserId), ImGroupMember::getGroupId);
        Set<Long> allMessageIds = convertSetByFlatMap(groups, group -> activeGroupIds.contains(group.getId())
                ? CollUtil.emptyIfNull(group.getPinnedMessageIds()).stream() : Stream.empty());
        Map<Long, ImGroupMessage> messageMap = groupMessageService.getGroupMessageMap(allMessageIds);
        // 转换输出
        return convertList(groups, group -> {
            ImGroupVo vo = BeanUtils.toBean(group, ImGroupVo.class);
            vo.setAvatar(vo.getAvatar());
            if (!activeGroupIds.contains(group.getId()) || CollUtil.isEmpty(group.getPinnedMessageIds())) {
                return vo;
            }
            // 按 pin 顺序输出，已被删除的消息（messageMap 没命中）跳过
            List<ImGroupMessage> pinnedMesages = convertList(group.getPinnedMessageIds(), messageMap::get);
            return vo.setPinnedMessages(BeanUtils.toBean(pinnedMesages, ImGroupMessageVo.class));
        });
    }

}
