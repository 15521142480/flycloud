package com.fly.im.service.friend;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.bo.ImFriendBo;
import com.fly.system.api.im.domain.bo.ImFriendManagerPageBo;
import com.fly.system.api.im.domain.ImFriend;
import com.fly.system.api.im.domain.ImFriendRequest;
import com.fly.system.api.im.enums.friend.ImFriendStateEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Collection;
import java.util.List;

/**
 * IM 好友关系 Service 接口
 * <p>
 * 注意：用户端「加好友」走 {@link ImFriendRequestService#applyFriend} 申请-审批流程，
 * 不再开放直接 add 接口；只有 {@link #becomeFriends} 是内部入口（被 agree 同意 / 管理员 import 触发）。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFriendService {

    /**
     * 获取 userId 视角下与 friendUserId 的好友关系状态（私聊发送热点路径）
     * <p>
     * 参见 {@link ImFriendStateEnum} 枚举类
     */
    Integer getFriendState(Long userId, Long friendUserId);

    /**
     * 校验「能否对 peerUserId 发起私聊语义动作」（消息发送 ／ RTC 邀请）
     * <p>
     * 好友 ／ 黑名单校验：和私聊消息发送同一套语义；NONE 已删 ／ 未加，BLOCKED 被对方拉黑
     *
     * @param userId     当前用户编号
     * @param peerUserId 对方用户编号
     */
    void validateFriend(Long userId, Long peerUserId);

    /**
     * 获得当前用户的好友列表（含已删除状态）
     */
    List<ImFriend> getFriendList(Long userId);

    /**
     * 获得当前用户的有效好友列表（仅 ENABLE 状态）
     */
    List<ImFriend> getEnableFriendList(Long userId);

    /**
     * 获得当前用户的双向有效好友列表（双方均 ENABLE 状态）
     */
    List<ImFriend> getMutualEnableFriendList(Long userId);

    /**
     * 获得当前用户与指定用户之间的有效好友列表（仅 ENABLE 状态）
     */
    List<ImFriend> getActiveFriendList(Long userId, Collection<Long> friendUserIds);

    /**
     * 查询一个好友关系记录
     */
    ImFriend getFriend(Long userId, Long friendUserId);

    // ==================== 内部入口 ====================

    /**
     * 双向建立好友关系（内部入口）
     * <p>
     * 由 {@link ImFriendRequestService#agreeFriendRequest} 同意申请 / 管理后台导入触发；
     * A 侧 displayName / addSource 取自申请记录；B 侧 displayName 为空、addSource 同来源。
     * 写库后推送 FRIEND_ADD 通知给 A、B 双方多端，并下发 TIP 系统消息。
     *
     * @param request 已同意的申请记录（决定 fromUserId / toUserId / addSource / displayName）
     */
    void becomeFriends(ImFriendRequest request);

    // ==================== 用户端 ====================

    /**
     * 删除好友（单向软删除）
     * <p>
     * 仅删除 userId 视角下的好友关系；对端 friendUserId 的视角不受影响。
     *
     * @param clear 是否级联清理本端相关数据（当前包含私聊会话；通过 FRIEND_DELETE 通知透传给多端）
     */
    void deleteFriend(Long userId, Long friendUserId, Boolean clear);

    /**
     * 更新好友单边属性（备注 / 免打扰 / 联系人置顶）
     */
    void updateFriend(Long userId, ImFriendBo reqVo);

    /**
     * 拉黑好友（必须先是好友）
     */
    void blockFriend(Long userId, Long friendUserId);

    /**
     * 移出黑名单
     */
    void unblockFriend(Long userId, Long friendUserId);

    /**
     * 增量拉取当前用户的好友关系（重连 / 离线补偿：含已删除，按 update_time + id 游标）
     *
     * @param userId
     * @param lastUpdateTime
     * @param lastId
     * @param limit
     */
    List<ImFriend> pullFriendList(Long userId, Long lastUpdateTime, Long lastId, Integer limit);




    // ==================== 管理后台 ====================

    /**
     * 【管理后台】分页查询好友关系
     */
    PageResult<ImFriend> getFriendPage(ImFriendManagerPageBo reqVo);



}
