package com.fly.im.service.friend;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.controller.admin.friend.vo.request.ImFriendRequestApplyReqVo;
import com.fly.im.controller.admin.manager.friend.vo.ImFriendRequestManagerPageReqVo;
import com.fly.im.dal.dataobject.friend.ImFriendRequestDO;

import java.util.List;

/**
 * IM 好友申请 Service 接口
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface ImFriendRequestService {

    /**
     * 发起好友申请
     *
     * @param fromUserId 发起方用户编号
     * @param reqVo      申请请求
     * @return 申请记录
     */
    ImFriendRequestDO applyFriend(Long fromUserId, ImFriendRequestApplyReqVo reqVo);

    /**
     * 同意好友申请
     *
     * @param userId    操作人用户编号
     * @param requestId 申请记录编号
     */
    void agreeFriendRequest(Long userId, Long requestId);

    /**
     * 拒绝好友申请
     *
     * @param userId        操作人用户编号
     * @param requestId     申请记录编号
     * @param handleContent 拒绝理由
     */
    void refuseFriendRequest(Long userId, Long requestId, String handleContent);

    /**
     * 查询「我相关」的申请列表（含我发起的、别人加我的）；游标分页：传 maxId 拉更早一页
     *
     * @param userId 用户编号
     * @param maxId  当前列表最旧记录的 id；首页传 null
     * @param limit  单次拉取条数（page size，由前端常量控制）
     * @return 申请记录列表，按更新时间、id 倒序
     */
    List<ImFriendRequestDO> getMyFriendRequestList(Long userId, Long maxId, Integer limit);

    /**
     * 按 id 单查申请记录；通用读接口，调用方自行做越权过滤
     */
    ImFriendRequestDO getFriendRequest(Long id);

    // ==================== 管理后台 ====================

    /**
     * 【管理后台】分页查询好友申请记录
     */
    PageResult<ImFriendRequestDO> getFriendRequestPage(ImFriendRequestManagerPageReqVo reqVo);

}
