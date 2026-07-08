package com.fly.im.service.friend;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.vo.admin.friend.request.ImFriendRequestApplyReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.friend.ImFriendRequestManagerPageReqVo;
import com.fly.system.api.im.domain.friend.ImFriendRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * IM 好友申请 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFriendRequestService {

    /**
     * 发起好友申请
     *
     * @param fromUserId 发起方用户编号
     * @param reqVo      申请请求
     * @return 申请记录
     */
    ImFriendRequest applyFriend(Long fromUserId, ImFriendRequestApplyReqVo reqVo);

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
    List<ImFriendRequest> getMyFriendRequestList(Long userId, Long maxId, Integer limit);

    /**
     * 按 id 单查申请记录；通用读接口，调用方自行做越权过滤
     */
    ImFriendRequest getFriendRequest(Long id);


    /**
     * 增量拉取「我相关」的好友申请（重连 / 离线补偿：双向 OR，按 update_time + id 游标）
     *
     * @param curUserId         用户编号
     * @param lastUpdateTime 上次拉取到的最新更新时间（毫秒时间戳）；首次拉取传 null
     * @param lastId         上次拉取到的最后一条记录 id；首次拉取传 null
     * @param limit          单次拉取条数
     * @return 申请记录列表，按更新时间、id 正序
     */
    List<ImFriendRequest> pullFriendRequestList(Long curUserId, Long lastUpdateTime, Long lastId, Integer limit);


    // ==================== 管理后台 ====================

    /**
     * 【管理后台】分页查询好友申请记录
     */
    PageResult<ImFriendRequest> getFriendRequestPage(ImFriendRequestManagerPageReqVo reqVo);




}
