package com.fly.im.service.group;

import com.fly.im.framework.pojo.PageResult;
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
import com.fly.system.api.im.domain.group.ImGroup;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户群群 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImGroupService {

    // ==================== 群的写操作 ====================

    /**
     * 创建群
     * <p>
     * 同时将当前登录用户设置为群主，并插入群主的群成员记录
     *
     * @param createReqVo 创建信息
     * @param userId      当前登录用户编号（群主）
     * @return 创建后的群信息
     */
    ImGroup createGroup(@Valid ImGroupCreateReqVo createReqVo, Long userId);

    /**
     * 更新群信息
     *
     * @param updateReqVo 更新信息
     * @param userId      当前登录用户编号
     * @return 更新后的群信息
     */
    ImGroup updateGroup(@Valid ImGroupUpdateReqVo updateReqVo, Long userId);

    /**
     * 解散群
     * <p>
     * 仅群主可执行
     *
     * @param id     群编号
     * @param userId 当前登录用户编号
     */
    void dissolveGroup(Long id, Long userId);

    // ==================== 群的读操作 ====================

    /**
     * 获得群
     *
     * @param id 编号
     * @return 群
     */
    ImGroup getGroup(Long id);

    /**
     * 批量获得群 Map
     *
     * @param ids 群编号集合
     * @return 群 Map（key = 群编号）
     */
    Map<Long, ImGroup> getGroupMap(Collection<Long> ids);

    /**
     * 校验群存在且未封禁、未解散
     *
     * @param groupId 群编号
     * @return 群信息
     */
    ImGroup validateGroupExists(Long groupId);

    /**
     * 校验入群人数上限
     * <p>
     * 调用方场景：自由进群 / 审批通过等不经 inviteGroupMember 的入群路径，需在写群成员前主动校验
     *
     * @param groupId  群编号
     * @param addCount 即将新增的人数
     * @throws com.fly.common.exception.ServiceException 群已满抛 GROUP_MEMBER_EXCEED
     */
    void validateMemberCountLimit(Long groupId, int addCount);

    /**
     * 获取指定用户的群列表
     * <p>
     * 返回用户当前仍有效的群，以及最近 yudao.im.message.group-pull-max-days 天内退群的群
     * —— 退群前可能还有离线消息需要展示，前端需要把这些群信息作为缓存。
     *
     * @param userId 用户编号
     * @return 群列表
     */
    List<ImGroup> getMyGroupList(Long userId);

    // ==================== 群成员的写操作 ====================
    // 说明：群成员的写操作统一放在 ImGroupService，而非 ImGroupMemberService，
    //       保持 ImGroupMemberService 无 WebSocket 推送等外部依赖，职责更单一。

    /**
     * 邀请用户加入群
     * <p>
     * 群成员即可执行，支持批量。
     * 校验群人数上限，邀请后推送提示消息和群创建事件给被邀请人。
     *
     * @param userId      当前登录用户编号
     * @param inviteReqVo 邀请信息
     */
    void inviteGroupMember(Long userId, @Valid ImGroupMemberInviteReqVo inviteReqVo);

    /**
     * 退群
     * <p>
     * 群主不可退群（只能解散）
     *
     * @param groupId 群编号
     * @param userId  当前登录用户编号
     */
    void quitGroup(Long groupId, Long userId);

    /**
     * 移除群成员（踢人）
     * <p>
     * 群主可踢管理员和普通成员；管理员仅能踢普通成员；群主不可被踢。
     *
     * @param userId      当前登录用户编号
     * @param removeReqVo 移除信息
     */
    void removeGroupMember(Long userId, @Valid ImGroupMemberRemoveReqVo removeReqVo);

    /**
     * 添加群管理员（仅群主可执行）
     *
     * @param userId 当前登录用户编号（群主）
     * @param reqVo  添加信息（含群编号、目标用户编号列表）
     */
    void addGroupAdmin(Long userId, @Valid ImGroupAdminAddReqVo reqVo);

    /**
     * 撤销群管理员（仅群主可执行）
     *
     * @param userId 当前登录用户编号（群主）
     * @param reqVo  撤销信息（含群编号、目标用户编号列表）
     */
    void removeGroupAdmin(Long userId, @Valid ImGroupAdminRemoveReqVo reqVo);

    /**
     * 转让群主（仅老群主可执行）
     * <p>
     * 转让后：旧群主 role 降为 MEMBER，新群主 role 升为 OWNER
     *
     * @param userId      当前登录用户编号（旧群主）
     * @param transferReqVo 转让信息
     */
    void transferGroupOwner(Long userId, @Valid ImGroupTransferOwnerReqVo transferReqVo);

    /**
     * 置顶群消息（仅群主或管理员可执行）
     * <p>
     * 上限由 yudao.im.group.pin-max-count 控制；幂等失败时抛业务异常
     *
     * @param userId    当前登录用户编号
     * @param groupId   群编号
     * @param messageId 被置顶的消息编号
     */
    void pinGroupMessage(Long userId, Long groupId, Long messageId);

    /**
     * 取消置顶群消息（仅群主或管理员可执行）
     *
     * @param userId    当前登录用户编号
     * @param groupId   群编号
     * @param messageId 被取消置顶的消息编号
     */
    void unpinGroupMessage(Long userId, Long groupId, Long messageId);

    // ==================== 群禁言 ====================

    /**
     * 全群禁言 / 取消（仅群主或管理员可执行）
     *
     * @param userId 当前登录用户编号
     * @param reqVo  禁言信息
     */
    void muteAll(Long userId, @Valid ImGroupMuteAllReqVo reqVo);

    /**
     * 禁言单个成员（三档分层权限）
     *
     * @param userId 当前登录用户编号
     * @param reqVo  禁言信息
     */
    void muteMember(Long userId, @Valid ImGroupMuteMemberReqVo reqVo);

    /**
     * 取消成员禁言（三档分层权限）
     *
     * @param userId 当前登录用户编号
     * @param reqVo  取消禁言信息
     */
    void cancelMuteMember(Long userId, @Valid ImGroupCancelMuteMemberReqVo reqVo);

    // ==================== 管理后台 ====================

    /**
     * 【管理后台】分页查询群列表
     *
     * @param pageReqVo 分页查询条件
     * @return 群分页列表
     */
    PageResult<ImGroup> getGroupPage(ImGroupManagerPageReqVo pageReqVo);

    /**
     * 【管理后台】封禁群
     *
     * @param operatorUserId 操作人用户编号
     * @param banReqVo 封禁信息（含群编号、封禁原因）
     */
    void banGroup(Long operatorUserId, @Valid ImGroupManagerBanReqVo banReqVo);

    /**
     * 【管理后台】解封群
     *
     * @param operatorUserId 操作人用户编号
     * @param id 群编号
     */
    void unbanGroup(Long operatorUserId, Long id);

    /**
     * 【管理后台】解散群
     *
     * @param operatorUserId 操作人用户编号
     * @param id 群编号
     */
    void dissolveGroupByManager(Long operatorUserId, Long id);

}
