package com.fly.im.service.group;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.im.framework.pojo.PageResult;
import com.fly.file.service.FileUrlService;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupAdminAddReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupAdminRemoveReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupCancelMuteMemberReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupCreateReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupMuteAllReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupMuteMemberReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupTransferOwnerReqVo;
import com.fly.system.api.im.domain.vo.admin.group.ImGroupUpdateReqVo;
import com.fly.system.api.im.domain.vo.admin.group.member.ImGroupMemberInviteReqVo;
import com.fly.system.api.im.domain.vo.admin.group.member.ImGroupMemberRemoveReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.group.ImGroupManagerBanReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.group.ImGroupManagerPageReqVo;
import com.fly.system.api.im.domain.friend.ImFriend;
import com.fly.system.api.im.domain.group.ImGroup;
import com.fly.system.api.im.domain.group.ImGroupMember;
import com.fly.system.api.im.domain.message.ImGroupMessage;
import com.fly.im.dal.mysql.group.ImGroupMapper;
import com.fly.system.api.im.enums.group.ImGroupAddSourceEnum;
import com.fly.im.framework.config.ImProperties;
import com.fly.system.api.im.enums.group.ImGroupMemberRoleEnum;
import com.fly.system.api.im.enums.message.ImMessageStatusEnum;
import com.fly.system.api.im.enums.message.ImMessageTypeEnum;
import com.fly.im.service.friend.ImFriendService;
import com.fly.im.service.message.ImGroupMessageService;
import com.fly.im.service.message.dto.ImGroupMessageSendDTO;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.common.utils.collection.CollectionUtils.*;
import static com.fly.im.dal.redis.RedisKeyConstants.GROUP;
import static com.fly.system.api.im.enums.ErrorCodeConstants.*;

/**
 * 用户群 Service 实现类
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
public class ImGroupServiceImpl implements ImGroupService {

    @Resource
    private ImGroupMapper groupMapper;

    @Resource
    @Lazy // 避免循环依赖
    private ImGroupMemberService groupMemberService;
    @Resource
    @Lazy // 避免循环依赖
    private ImGroupMessageService groupMessageService;
    @Resource
    @Lazy // 避免循环依赖
    private ImGroupRequestService groupRequestService;

    @Resource
    private ImFriendService friendService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private ImProperties imProperties;
    @Resource
    private FileUrlService fileUrlService;

    // ==================== 群的写操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImGroup createGroup(ImGroupCreateReqVo createReqVo, Long userId) {
        // 1.1 处理初始成员列表（去重 + 排除创建者自己）
        Set<Long> initialMemberUserIds = createReqVo.getMemberUserIds() == null
                ? new HashSet<>() : new HashSet<>(createReqVo.getMemberUserIds());
        initialMemberUserIds.remove(userId);
        // 1.2 校验初始成员都是创建者的好友
        if (CollUtil.isNotEmpty(initialMemberUserIds)) {
            List<ImFriend> friends = friendService.getActiveFriendList(userId, initialMemberUserIds);
            Set<Long> friendUserIds = convertSet(friends, ImFriend::getFriendUserId);
            Collection<Long> notFriendUserIds = CollUtil.subtract(initialMemberUserIds, friendUserIds);
            if (CollUtil.isNotEmpty(notFriendUserIds)) {
                throw exception(GROUP_INVITE_NOT_FRIEND, getUserNicknames(notFriendUserIds));
            }
        }
        // 1.3 校验群人数上限（创建者 + 初始成员 ≤ 群成员上限）
        int maxMember = imProperties.getGroup().getMaxMember();
        if (initialMemberUserIds.size() + 1 > maxMember) {
            throw exception(GROUP_MEMBER_EXCEED, maxMember);
        }

        // 2.1 插入群记录
        ImGroup group = BeanUtils.toBean(createReqVo, ImGroup.class)
                .setOwnerUserId(userId).setStatus(CommonStatusEnum.ENABLE.getStatus());
        group.setAvatar(fileUrlService.toPath(group.getAvatar()));
        groupMapper.insert(group);
        // 2.2 创建者作为 OWNER 入群
        groupMemberService.addGroupMember(group.getId(), userId, ImGroupMemberRoleEnum.OWNER.getRole());
        // 2.3 批量添加初始成员；标记 addSource=INVITE / inviter=创建者
        if (CollUtil.isNotEmpty(initialMemberUserIds)) {
            groupMemberService.addGroupMembers(group.getId(), initialMemberUserIds,
                    ImGroupAddSourceEnum.INVITE.getSource(), userId);
        }

        // 3. 推送 GROUP_CREATE 通知给全员（含创建者多端同步 + 初始成员）
        List<Long> allMemberUserIds = CollectionUtils.of(userId, initialMemberUserIds);
        groupMessageService.sendGroupMessage(userId, allMemberUserIds,
                ImGroupMessageSendDTO.ofGroupCreate(group.getId(), userId, allMemberUserIds));
        return group;
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#updateReqVo.id")
    @Transactional(rollbackFor = Exception.class)
    public ImGroup updateGroup(ImGroupUpdateReqVo updateReqVo, Long userId) {
        // 1.1 校验群存在：group 留作老值备份，通知里 oldXXX 字段从这里取
        ImGroup group = validateGroupExists(updateReqVo.getId());
        // 1.2 校验操作人是群主
        if (ObjUtil.notEqual(group.getOwnerUserId(), userId)) {
            throw exception(GROUP_NOT_OWNER);
        }

        // 2. 更新数据库（newGroup 仅含变更字段）
        ImGroup newGroup = BeanUtils.toBean(updateReqVo, ImGroup.class);
        newGroup.setAvatar(fileUrlService.toPath(newGroup.getAvatar()));
        groupMapper.updateById(newGroup);

        // 3. 按变更字段分别推送 GROUP_NAME / NOTICE / INFO_UPDATE 通知；活跃成员只查一次复用，避免 3 次 Redis GET
        // name / avatar 不允许空串（业务上必须非空），notice 允许空串（清空公告也是有效操作）
        Long groupId = group.getId();
        boolean nameChanged = StrUtil.isNotEmpty(updateReqVo.getName());
        boolean noticeChanged = updateReqVo.getNotice() != null;
        boolean avatarChanged = StrUtil.isNotEmpty(updateReqVo.getAvatar());
        if (nameChanged || noticeChanged || avatarChanged) {
            List<Long> memberUserIds = groupMemberService.getActiveGroupMemberUserIdsByGroupId(groupId);
            if (nameChanged) {
                groupMessageService.sendGroupMessage(userId, memberUserIds, ImGroupMessageSendDTO.ofGroupNameUpdate(
                        groupId, userId, group.getName(), updateReqVo.getName()));
            }
            if (noticeChanged) {
                groupMessageService.sendGroupMessage(userId, memberUserIds, ImGroupMessageSendDTO.ofGroupNoticeUpdate(
                        groupId, userId, group.getNotice(), updateReqVo.getNotice()));
            }
            if (avatarChanged) {
                groupMessageService.sendGroupMessage(userId, memberUserIds, ImGroupMessageSendDTO.ofGroupInfoUpdate(
                        groupId, userId, fileUrlService.buildUrl(group.getAvatar()),
                        fileUrlService.buildUrl(newGroup.getAvatar())));
            }
        }

        // 4. 返回合并后的新群信息（updateReqVo 非空字段覆盖 group）
        BeanUtil.copyProperties(updateReqVo, group, CopyOptions.create().ignoreNullValue());
        group.setAvatar(newGroup.getAvatar());
        return group;
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public void dissolveGroup(Long id, Long userId) {
        // 1. 校验群存在 + 当前用户是群主
        ImGroup group = validateGroupNotDissolved(id);
        if (ObjUtil.notEqual(group.getOwnerUserId(), userId)) {
            throw exception(GROUP_NOT_OWNER);
        }

        // 2. 解散群
        dissolveGroup0(id, userId);
    }

    private void dissolveGroup0(Long id, Long userId) {
        // 1. 先发 GROUP_DISSOLVE 通知：放在成员移除前，sendGroupMessage 才能查到全员
        groupMessageService.sendGroupMessage(userId, ImGroupMessageSendDTO.ofGroupDissolve(id, userId));

        // 2.1 更新群状态为已解散
        groupMapper.updateById(new ImGroup().setId(id)
                .setStatus(CommonStatusEnum.DISABLE.getStatus()).setDissolvedTime(LocalDateTime.now()));
        // 2.2 移除全部群成员
        groupMemberService.removeGroupMembersByGroupId(id);
        // 2.3 清理已读缓存
        groupMessageService.deleteReadMaxMessageIdMap(id);
    }

    // ==================== 群成员的写操作 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inviteGroupMember(Long userId, ImGroupMemberInviteReqVo inviteReqVo) {
        Long groupId = inviteReqVo.getGroupId();
        // 1.1 校验群存在 + 当前用户是群成员；同时拿到 role 供下面审批分支判断
        ImGroup group = validateGroupExists(groupId);
        ImGroupMember operator = groupMemberService.validateMemberInGroup(groupId, userId);
        // 1.2 入参去重 + 排除已在群中的用户
        List<ImGroupMember> activeMembers = groupMemberService.getActiveGroupMemberListByGroupId(groupId);
        Set<Long> activeMemberUserIds = convertSet(activeMembers, ImGroupMember::getUserId);
        List<Long> memberUserIds = CollUtil.subtractToList(
                CollUtil.distinct(inviteReqVo.getMemberUserIds()), activeMemberUserIds);
        if (CollUtil.isEmpty(memberUserIds)) {
            return;
        }
        // 1.3 校验被邀请人都是当前用户的好友
        List<ImFriend> friends = friendService.getActiveFriendList(userId, memberUserIds);
        Set<Long> friendUserIds = convertSet(friends, ImFriend::getFriendUserId);
        Collection<Long> notFriendUserIds = CollUtil.subtract(memberUserIds, friendUserIds);
        if (CollUtil.isNotEmpty(notFriendUserIds)) {
            throw exception(GROUP_INVITE_NOT_FRIEND, getUserNicknames(notFriendUserIds));
        }
        // 1.4 校验群人数上限
        int maxMember = imProperties.getGroup().getMaxMember();
        if (activeMembers.size() + memberUserIds.size() > maxMember) {
            throw exception(GROUP_MEMBER_EXCEED, maxMember);
        }

        // 2. 情况一：群开启审批 + 邀请人是普通成员，落 group_request 等群主 / 管理员处理
        // 群主 / 管理员邀请，直接拉人进群
        if (Boolean.TRUE.equals(group.getJoinApproval())
                && !ImGroupMemberRoleEnum.isOwnerOrAdmin(operator.getRole())) {
            groupRequestService.createInviteRequestList(groupId, userId, memberUserIds);
            return;
        }

        // 3. 情况二：未开审批 / 群主 / 管理员邀请，直进；批量添加群成员，写 addSource=INVITE / inviterUserId=操作人 留痕
        groupMemberService.addGroupMembers(groupId, memberUserIds,
                ImGroupAddSourceEnum.INVITE.getSource(), userId);

        // 4. 发 GROUP_MEMBER_INVITE 通知给全员；本地拼 receivers（已查的 active + 新邀请）避免缓存刚 evict 后强制走 DB
        Set<Long> allReceivers = new HashSet<>(memberUserIds);
        allReceivers.addAll(activeMemberUserIds);
        groupMessageService.sendGroupMessage(userId, allReceivers,
                ImGroupMessageSendDTO.ofGroupMemberInvite(groupId, userId, memberUserIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quitGroup(Long groupId, Long userId) {
        // 1.1 校验群存在且未解散
        ImGroup group = getSelf().getGroup(groupId);
        if (group == null) {
            throw exception(GROUP_NOT_EXISTS);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(group.getStatus())) {
            throw exception(GROUP_DISSOLVED);
        }
        // 1.2 群主不可退群
        if (ObjUtil.equal(group.getOwnerUserId(), userId)) {
            throw exception(GROUP_OWNER_CANNOT_QUIT);
        }
        // 1.3 校验当前用户是有效群成员；防止非成员触发广播 + 后续 remove 失败时无法回滚已推送的事件
        groupMemberService.validateMemberInGroup(groupId, userId);

        // 2. 先发广播，后移成员（见类 javadoc）
        groupMessageService.sendGroupMessage(userId, ImGroupMessageSendDTO.ofGroupMemberQuit(groupId, userId));

        // 3.1 移除群成员
        groupMemberService.removeGroupMember(groupId, userId);
        // 3.2 清理已读缓存
        groupMessageService.deleteReadMaxMessageId(groupId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGroupMember(Long userId, ImGroupMemberRemoveReqVo removeReqVo) {
        Long groupId = removeReqVo.getGroupId();
        Set<Long> targetUserIds = new HashSet<>(removeReqVo.getMemberUserIds());
        // 1.1 校验群存在 + 操作者是群主或管理员
        ImGroupMember operator = validateGroupOwnerOrAdmin(groupId, userId);
        // 1.2 不能移除自己
        if (targetUserIds.contains(userId)) {
            throw exception(GROUP_CANNOT_REMOVE_SELF);
        }
        // 1.3 仅保留仍有效的成员；已退群（DISABLE）/ 查无记录的目标直接跳过，只踢有效成员，不让整批失败
        List<ImGroupMember> targets = filterList(groupMemberService.getGroupMembers(groupId, targetUserIds),
                target -> CommonStatusEnum.ENABLE.getStatus().equals(target.getStatus()));
        Set<Long> validTargetUserIds = convertSet(targets, ImGroupMember::getUserId);
        // 1.4 目标全部已不在群：无人可踢，直接返回
        if (CollUtil.isEmpty(validTargetUserIds)) {
            return;
        }
        // 1.5 三档权限校验：群主不可被移出；管理员不能移出管理员
        boolean operatorIsAdmin = ImGroupMemberRoleEnum.isAdmin(operator.getRole());
        for (ImGroupMember target : targets) {
            if (ImGroupMemberRoleEnum.isOwner(target.getRole())) {
                throw exception(GROUP_REMOVE_OWNER_DENIED);
            }
            if (operatorIsAdmin && ImGroupMemberRoleEnum.isAdmin(target.getRole())) {
                throw exception(GROUP_REMOVE_ADMIN_DENIED);
            }
        }

        // 2. 先发 GROUP_MEMBER_KICK 通知：放在被踢者移除前，sendGroupMessage 才能查到全员（含被踢者）
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupMemberKick(groupId, userId, validTargetUserIds));

        // 3.1 批量移除群成员
        groupMemberService.removeGroupMembers(groupId, validTargetUserIds);
        // 3.2 批量清理已读缓存
        groupMessageService.deleteReadMaxMessageIds(groupId, validTargetUserIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupAdmin(Long userId, ImGroupAdminAddReqVo reqVo) {
        Long groupId = reqVo.getId();
        Set<Long> targetUserIds = new HashSet<>(reqVo.getUserIds());
        // 1.1 仅群主可操作
        validateGroupOwnerForUpdate(groupId, userId);
        // 1.2 校验目标都是有效成员且非群主
        Map<Long, ImGroupMember> targetMap = convertMap(
                groupMemberService.getGroupMembers(groupId, targetUserIds), ImGroupMember::getUserId);
        validateAdminTargets(targetUserIds, targetMap);
        // 1.3 幂等过滤：跳过已是 ADMIN
        Set<Long> changedUserIds = convertSet(targetUserIds,
                id -> id,
                id -> !ImGroupMemberRoleEnum.isAdmin(targetMap.get(id).getRole()));
        if (CollUtil.isEmpty(changedUserIds)) {
            return;
        }
        // 1.4 校验上限
        Long existAdminCount = groupMemberService.getGroupMemberCountByRole(
                groupId, ImGroupMemberRoleEnum.ADMIN.getRole());
        int adminMaxCount = imProperties.getGroup().getAdminMaxCount();
        if (existAdminCount + changedUserIds.size() > adminMaxCount) {
            throw exception(GROUP_ADMIN_MAX_LIMIT, adminMaxCount);
        }

        // 2. 批量更新角色
        int affected = groupMemberService.updateGroupMemberRole(groupId, changedUserIds,
                ImGroupMemberRoleEnum.ADMIN.getRole());
        if (affected != changedUserIds.size()) {
            throw exception(GROUP_ADMIN_TARGET_NOT_IN_GROUP);
        }

        // 3. 推送 GROUP_ADMIN_ADD 通知给全员
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupAdminAdd(groupId, userId, changedUserIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGroupAdmin(Long userId, ImGroupAdminRemoveReqVo reqVo) {
        Long groupId = reqVo.getId();
        Set<Long> targetUserIds = new HashSet<>(reqVo.getUserIds());
        // 1.1 仅群主可操作
        validateGroupOwnerForUpdate(groupId, userId);
        // 1.2 校验目标都是有效成员且非群主
        Map<Long, ImGroupMember> targetMap = convertMap(
                groupMemberService.getGroupMembers(groupId, targetUserIds), ImGroupMember::getUserId);
        validateAdminTargets(targetUserIds, targetMap);
        // 1.3 幂等过滤：跳过已是 MEMBER
        Set<Long> changedUserIds = convertSet(targetUserIds,
                id -> id,
                id -> ImGroupMemberRoleEnum.isAdmin(targetMap.get(id).getRole()));
        if (CollUtil.isEmpty(changedUserIds)) {
            return;
        }

        // 2. 批量更新角色
        int affected = groupMemberService.updateGroupMemberRole(groupId, changedUserIds,
                ImGroupMemberRoleEnum.NORMAL.getRole());
        if (affected != changedUserIds.size()) {
            throw exception(GROUP_ADMIN_TARGET_NOT_IN_GROUP);
        }

        // 3. 推送 GROUP_ADMIN_REMOVE 通知给全员
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupAdminRemove(groupId, userId, changedUserIds));
    }

    /**
     * 校验管理员变更目标都是当前群的有效成员（status=ENABLE）且非群主
     */
    private void validateAdminTargets(Set<Long> targetUserIds, Map<Long, ImGroupMember> targetMap) {
        for (Long targetUserId : targetUserIds) {
            ImGroupMember target = targetMap.get(targetUserId);
            if (target == null || CommonStatusEnum.DISABLE.getStatus().equals(target.getStatus())) {
                throw exception(GROUP_ADMIN_TARGET_NOT_IN_GROUP);
            }
            if (ImGroupMemberRoleEnum.isOwner(target.getRole())) {
                throw exception(GROUP_ADMIN_TARGET_IS_OWNER);
            }
        }
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#transferReqVo.id")
    @Transactional(rollbackFor = Exception.class)
    public void transferGroupOwner(Long userId, ImGroupTransferOwnerReqVo transferReqVo) {
        Long groupId = transferReqVo.getId();
        Long newOwnerUserId = transferReqVo.getNewOwnerUserId();
        // 1.1 仅老群主可执行
        validateGroupOwnerForUpdate(groupId, userId);
        // 1.2 不能转让给自己
        if (ObjUtil.equal(userId, newOwnerUserId)) {
            throw exception(GROUP_TRANSFER_OWNER_TO_SELF);
        }
        // 1.3 新群主必须是群的有效成员
        ImGroupMember newOwner = groupMemberService.validateMemberInGroup(groupId, newOwnerUserId);

        // 2.1 更新成员角色
        int newOwnerAffected = groupMemberService.updateGroupMemberRole(groupId, Set.of(newOwner.getUserId()),
                ImGroupMemberRoleEnum.OWNER.getRole());
        if (newOwnerAffected != 1) {
            throw exception(GROUP_MEMBER_NOT_IN_GROUP);
        }
        int oldOwnerAffected = groupMemberService.updateGroupMemberRole(groupId, Set.of(userId),
                ImGroupMemberRoleEnum.NORMAL.getRole());
        if (oldOwnerAffected != 1) {
            throw exception(GROUP_MEMBER_NOT_IN_GROUP);
        }
        // 2.2 更新群主编号
        groupMapper.updateById(new ImGroup().setId(groupId).setOwnerUserId(newOwnerUserId));

        // 3. 推送 GROUP_OWNER_TRANSFER 通知给全员
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupOwnerTransfer(groupId, userId, newOwnerUserId));
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#groupId")
    @Transactional(rollbackFor = Exception.class)
    public void pinGroupMessage(Long userId, Long groupId, Long messageId) {
        // 1.1 校验群主 / 管理员；同时拿到 group 复用，避免再走一次 @Cacheable
        ImGroup group = validateOwnerOrAdminAndGetGroupForUpdate(groupId, userId);
        // 1.2 校验消息属于该群、是普通聊天消息（绕过前端菜单不允许置顶群事件 / 撤回事件）、且未被撤回
        ImGroupMessage message = groupMessageService.getGroupMessage(messageId);
        if (message == null || ObjUtil.notEqual(message.getGroupId(), groupId)) {
            throw exception(MESSAGE_NOT_IN_GROUP);
        }
        if (!ImMessageTypeEnum.validate(message.getType()).isNormal()
                || ImMessageStatusEnum.RECALL.getStatus().equals(message.getStatus())) {
            throw exception(MESSAGE_NOT_IN_GROUP);
        }
        // 1.3 定向消息（receiverUserIds 非空）不允许置顶：置顶会向全群广播，泄露原本仅部分人可见的内容
        if (CollUtil.isNotEmpty(message.getReceiverUserIds())) {
            throw exception(GROUP_MESSAGE_PIN_DIRECTED_DENIED);
        }

        // 2. 幂等 + 上限校验
        List<Long> pinned = new ArrayList<>(CollUtil.emptyIfNull(group.getPinnedMessageIds()));
        if (pinned.contains(messageId)) {
            throw exception(GROUP_MESSAGE_ALREADY_PINNED);
        }
        int pinMaxCount = imProperties.getGroup().getPinMaxCount();
        if (pinned.size() >= pinMaxCount) {
            throw exception(GROUP_MESSAGE_PIN_MAX_LIMIT, pinMaxCount);
        }
        pinned.add(messageId);
        groupMapper.updateById(new ImGroup().setId(groupId).setPinnedMessageIds(pinned));

        // 3. 推送 GROUP_MESSAGE_PIN 通知给全员
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupMessagePin(groupId, userId, message));
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#groupId")
    @Transactional(rollbackFor = Exception.class)
    public void unpinGroupMessage(Long userId, Long groupId, Long messageId) {
        // 1. 校验群主 / 管理员；同时拿到 group 复用，避免再走一次 @Cacheable
        ImGroup group = validateOwnerOrAdminAndGetGroupForUpdate(groupId, userId);
        // 2. 幂等校验
        List<Long> pinned = new ArrayList<>(CollUtil.emptyIfNull(group.getPinnedMessageIds()));
        if (!pinned.contains(messageId)) {
            throw exception(GROUP_MESSAGE_NOT_PINNED);
        }
        pinned.remove(messageId);
        groupMapper.updateById(new ImGroup().setId(groupId).setPinnedMessageIds(pinned));

        // 3. 推送 GROUP_MESSAGE_UNPIN 通知给全员
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupMessageUnpin(groupId, userId, messageId));
    }

    /**
     * 校验登录用户是群主或管理员，同时返回群信息
     */
    private ImGroup validateOwnerOrAdminAndGetGroup(Long groupId, Long userId) {
        ImGroup group = validateGroupExists(groupId);
        ImGroupMember member = groupMemberService.validateMemberInGroup(groupId, userId);
        if (!ImGroupMemberRoleEnum.isOwnerOrAdmin(member.getRole())) {
            throw exception(GROUP_NOT_OWNER_OR_ADMIN);
        }
        return group;
    }

    /**
     * 校验登录用户是群主或管理员，同时锁定群信息
     */
    private ImGroup validateOwnerOrAdminAndGetGroupForUpdate(Long groupId, Long userId) {
        ImGroup group = validateGroupExistsForUpdate(groupId);
        ImGroupMember member = groupMemberService.validateMemberInGroup(groupId, userId);
        if (!ImGroupMemberRoleEnum.isOwnerOrAdmin(member.getRole())) {
            throw exception(GROUP_NOT_OWNER_OR_ADMIN);
        }
        return group;
    }

    // ==================== 群的读操作 ====================

    @Override
    public ImGroup validateGroupExists(Long id) {
        ImGroup group = getSelf().getGroup(id);
        if (group == null) {
            throw exception(GROUP_NOT_EXISTS);
        }
        if (Boolean.TRUE.equals(group.getBanned())) {
            throw exception(GROUP_BANNED);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(group.getStatus())) {
            throw exception(GROUP_DISSOLVED);
        }
        return group;
    }

    @Override
    public void validateMemberCountLimit(Long groupId, int addCount) {
        // 1. 锁定群记录
        validateGroupExistsForUpdate(groupId);
        // 2. 校验成员数量上限
        int activeCount = groupMemberService.getActiveGroupMemberUserIdsByGroupId(groupId).size();
        int maxMember = imProperties.getGroup().getMaxMember();
        if (activeCount + addCount > maxMember) {
            throw exception(GROUP_MEMBER_EXCEED, maxMember);
        }
    }

    private ImGroup validateGroupOwnerForUpdate(Long groupId, Long userId) {
        ImGroup group = validateGroupExistsForUpdate(groupId);
        if (ObjUtil.notEqual(group.getOwnerUserId(), userId)) {
            throw exception(GROUP_NOT_OWNER);
        }
        return group;
    }

    private ImGroup validateGroupExistsForUpdate(Long groupId) {
        ImGroup group = groupMapper.selectByIdForUpdate(groupId);
        if (group == null) {
            throw exception(GROUP_NOT_EXISTS);
        }
        if (Boolean.TRUE.equals(group.getBanned())) {
            throw exception(GROUP_BANNED);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(group.getStatus())) {
            throw exception(GROUP_DISSOLVED);
        }
        return group;
    }

    private ImGroup validateGroupNotDissolved(Long id) {
        ImGroup group = getSelf().getGroup(id);
        if (group == null) {
            throw exception(GROUP_NOT_EXISTS);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(group.getStatus())) {
            throw exception(GROUP_DISSOLVED);
        }
        return group;
    }

    /**
     * 校验当前用户是群主或管理员，否则抛 GROUP_NOT_OWNER_OR_ADMIN
     */
    private ImGroupMember validateGroupOwnerOrAdmin(Long groupId, Long userId) {
        validateGroupExists(groupId);
        ImGroupMember member = groupMemberService.validateMemberInGroup(groupId, userId);
        if (!ImGroupMemberRoleEnum.isOwnerOrAdmin(member.getRole())) {
            throw exception(GROUP_NOT_OWNER_OR_ADMIN);
        }
        return member;
    }

    @Override
    @Cacheable(cacheNames = GROUP, key = "#id", unless = "#result == null")
    public ImGroup getGroup(Long id) {
        return groupMapper.selectById(id);
    }

    @Override
    public Map<Long, ImGroup> getGroupMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return convertMap(groupMapper.selectByIds(ids), ImGroup::getId);
    }

    @Override
    public List<ImGroup> getMyGroupList(Long userId) {
        // 1.1 查用户所在的、仍有效的群成员记录（仅 ENABLE 状态）
        List<ImGroupMember> members = groupMemberService.getActiveGroupMemberListByUserId(userId);
        // 1.2 再查最近 N 天（与群消息离线拉取窗口一致）内退群的成员记录（退群前可能有离线消息需要展示，一并返回作为前端缓存）
        LocalDateTime minQuitTime = LocalDateTime.now().minusDays(imProperties.getMessage().getGroupPullMaxDays());
        members.addAll(groupMemberService.getQuitGroupMemberListByUserId(userId, minQuitTime));
        if (CollUtil.isEmpty(members)) {
            return Collections.emptyList();
        }

        // 2. 批量查询群信息（不按 status / banned 过滤，已解散 / 封禁的群也要返回，供前端展示历史消息的群名 / 头像）
        Set<Long> groupIds = convertSet(members, ImGroupMember::getGroupId);
        return groupMapper.selectByIds(groupIds);
    }

    // ==================== 管理后台 ====================

    @Override
    public PageResult<ImGroup> getGroupPage(ImGroupManagerPageReqVo pageReqVo) {
        return groupMapper.selectPage(pageReqVo);
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#banReqVo.id")
    public void banGroup(Long operatorUserId, ImGroupManagerBanReqVo banReqVo) {
        // 1. 校验群存在且未解散
        ImGroup group = getSelf().getGroup(banReqVo.getId());
        if (group == null) {
            throw exception(GROUP_NOT_EXISTS);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(group.getStatus())) {
            throw exception(GROUP_DISSOLVED);
        }
        // 2. 幂等：已封禁直接返回，避免重复广播封禁通知
        if (Boolean.TRUE.equals(group.getBanned())) {
            return;
        }

        // 3. 更新封禁状态
        groupMapper.updateById(new ImGroup().setId(banReqVo.getId())
                .setBanned(true).setBannedReason(banReqVo.getReason()).setBannedTime(LocalDateTime.now()));

        // 4. 广播通知
        groupMessageService.sendGroupMessage(operatorUserId,
                ImGroupMessageSendDTO.ofGroupBanned(banReqVo.getId(), operatorUserId, true));
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#id")
    public void unbanGroup(Long operatorUserId, Long id) {
        // 1. 校验群存在
        if (getSelf().getGroup(id) == null) {
            throw exception(GROUP_NOT_EXISTS);
        }

        // 2. 解封（保留 bannedReason / bannedTime 作为历史记录）
        groupMapper.updateById(new ImGroup().setId(id).setBanned(false));

        // 3. 广播通知
        groupMessageService.sendGroupMessage(operatorUserId,
                ImGroupMessageSendDTO.ofGroupBanned(id, operatorUserId, false));
    }

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public void dissolveGroupByManager(Long operatorUserId, Long id) {
        // 1. 校验群存在且未解散
        validateGroupNotDissolved(id);

        // 2. 解散群
        dissolveGroup0(id, operatorUserId);
    }

    // ==================== 群禁言 ====================

    @Override
    @CacheEvict(cacheNames = GROUP, key = "#reqVo.id")
    @Transactional(rollbackFor = Exception.class)
    public void muteAll(Long userId, ImGroupMuteAllReqVo reqVo) {
        // 1. 校验群主或管理员
        validateGroupOwnerOrAdmin(reqVo.getId(), userId);

        // 2. 更新 mutedAll
        groupMapper.updateById(new ImGroup().setId(reqVo.getId()).setMutedAll(reqVo.getMutedAll()));

        // 3. 广播通知
        ImGroupMessageSendDTO messageSendDTO = Boolean.TRUE.equals(reqVo.getMutedAll())
                ? ImGroupMessageSendDTO.ofGroupMuted(reqVo.getId(), userId)
                : ImGroupMessageSendDTO.ofGroupCancelMuted(reqVo.getId(), userId);
        groupMessageService.sendGroupMessage(userId, messageSendDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void muteMember(Long userId, ImGroupMuteMemberReqVo reqVo) {
        // 1.1 不能禁言自己
        if (ObjUtil.equal(userId, reqVo.getUserId())) {
            throw exception(GROUP_MUTE_MEMBER_SELF);
        }
        // 1.2 校验群存在且未封禁
        validateGroupExists(reqVo.getId());
        // 1.3 校验操作人和目标都在群中
        ImGroupMember operatorMember = groupMemberService.validateMemberInGroup(reqVo.getId(), userId);
        ImGroupMember targetMember = groupMemberService.validateMemberInGroup(reqVo.getId(), reqVo.getUserId());
        // 1.4 三档权限校验
        validateMutePermission(operatorMember, targetMember);

        // 2. 设置 muteEndTime
        LocalDateTime muteEndTime = reqVo.getMutedSeconds() == 0
                ? ImGroupMember.PERMANENT_MUTE_END_TIME : LocalDateTime.now().plusSeconds(reqVo.getMutedSeconds());
        groupMemberService.updateGroupMemberMuteEndTime(reqVo.getId(), reqVo.getUserId(), muteEndTime);

        // 3. 广播通知
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupMemberMuted(reqVo.getId(), userId,
                        reqVo.getUserId(), muteEndTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMuteMember(Long userId, ImGroupCancelMuteMemberReqVo reqVo) {
        // 1.1 校验群存在且未封禁
        validateGroupExists(reqVo.getId());
        // 1.2 校验操作人和目标都在群中
        ImGroupMember operatorMember = groupMemberService.validateMemberInGroup(reqVo.getId(), userId);
        ImGroupMember targetMember = groupMemberService.validateMemberInGroup(reqVo.getId(), reqVo.getUserId());
        // 1.3 三档权限校验
        validateMutePermission(operatorMember, targetMember);

        // 2. 取消禁言（清空 muteEndTime）
        groupMemberService.updateGroupMemberMuteEndTime(reqVo.getId(), reqVo.getUserId(), null);

        // 3. 广播通知
        groupMessageService.sendGroupMessage(userId,
                ImGroupMessageSendDTO.ofGroupMemberCancelMuted(reqVo.getId(), userId, reqVo.getUserId()));
    }

    /**
     * 三档分层禁言权限校验
     */
    private void validateMutePermission(ImGroupMember operator, ImGroupMember target) {
        // 普通成员不能禁言任何人
        if (!ImGroupMemberRoleEnum.isOwnerOrAdmin(operator.getRole())) {
            throw exception(GROUP_NOT_OWNER_OR_ADMIN);
        }
        // 群主不可被禁言
        if (ImGroupMemberRoleEnum.isOwner(target.getRole())) {
            throw exception(GROUP_MUTE_OWNER_DENIED);
        }
        // 管理员不能禁言其他管理员
        if (ImGroupMemberRoleEnum.isAdmin(target.getRole()) && !ImGroupMemberRoleEnum.isOwner(operator.getRole())) {
            throw exception(GROUP_MUTE_ADMIN_DENIED);
        }
    }

    private ImGroupServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

    /**
     * 根据用户编号集合，拼接用户昵称字符串（逗号分隔）
     *
     * @param userIds 用户编号集合
     * @return 昵称字符串，如 "张三,李四"
     */
    private String getUserNicknames(Collection<Long> userIds) {
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        return userIds.stream()
                .map(id -> userMap.containsKey(id) ? userMap.get(id).getName() : String.valueOf(id))
                .collect(Collectors.joining(","));
    }

}
