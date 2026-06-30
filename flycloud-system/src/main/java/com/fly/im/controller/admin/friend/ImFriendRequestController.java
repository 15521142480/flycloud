package com.fly.im.controller.admin.friend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.friend.vo.request.ImFriendRequestApplyReqVO;
import com.fly.im.controller.admin.friend.vo.request.ImFriendRequestRespVO;
import com.fly.im.dal.dataobject.friend.ImFriendRequestDO;
import com.fly.im.service.friend.ImFriendRequestService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.utils.collection.CollectionUtils.convertSetByFlatMap;
import static com.fly.common.security.util.UserUtils.getCurUserId;

/**
 * IM 好友申请记录 Controller
 *
 * @author lxs
 * @date 2026-06-30
 */
@Tag(name = "管理后台 - IM 好友申请")
@RestController
@RequestMapping("/im/friend-request")
@Validated
public class ImFriendRequestController {

    @Resource
    private ImFriendRequestService friendRequestService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/apply")
    @Operation(summary = "发起好友申请")
    public R<Long> applyFriend(@Valid @RequestBody ImFriendRequestApplyReqVO reqVO) {
        ImFriendRequestDO request = friendRequestService.applyFriend(getCurUserId(), reqVO);
        return ok(request != null ? request.getId() : null);
    }

    @PutMapping("/agree")
    @Operation(summary = "同意好友申请")
    @Parameter(name = "id", description = "申请编号", required = true, example = "1024")
    public R<Boolean> agreeFriendRequest(
            @RequestParam("id") @NotNull(message = "申请编号不能为空") Long id) {
        friendRequestService.agreeFriendRequest(getCurUserId(), id);
        return ok(true);
    }

    @PutMapping("/refuse")
    @Operation(summary = "拒绝好友申请")
    public R<Boolean> refuseFriendRequest(
            @RequestParam("id") @NotNull(message = "申请编号不能为空") Long id,
            @RequestParam(value = "handleContent", required = false)
            @Size(max = 255, message = "处理理由最多 255 个字符") String handleContent) {
        friendRequestService.refuseFriendRequest(getCurUserId(), id, handleContent);
        return ok(true);
    }

    @GetMapping("/list")
    @Operation(summary = "查询「我相关」的好友申请列表（游标分页：传 maxId 加载更多）")
    public R<List<ImFriendRequestRespVO>> getMyFriendRequestList(
            @Parameter(description = "当前列表最旧记录的 id；首页不传")
            @RequestParam(value = "maxId", required = false) Long maxId,
            @Parameter(description = "单次拉取条数", required = true)
            @RequestParam("limit") @Min(1) @Max(200) Integer limit) {
        List<ImFriendRequestDO> list = friendRequestService.getMyFriendRequestList(getCurUserId(), maxId, limit);
        return ok(buildList(list));
    }

    @GetMapping("/get")
    @Operation(summary = "按 id 单查「我相关」的申请记录（带越权过滤；WebSocket 通知到达后用）")
    @Parameter(name = "id", description = "申请记录编号", required = true)
    public R<ImFriendRequestRespVO> getMyFriendRequest(@RequestParam("id") Long id) {
        ImFriendRequestDO request = friendRequestService.getFriendRequest(id);
        // 越权过滤：fromUser / toUser 必有一方是当前用户，否则当不存在返回 null
        Long currentUserId = getCurUserId();
        if (request == null || (ObjUtil.notEqual(request.getFromUserId(), currentUserId)
                && ObjUtil.notEqual(request.getToUserId(), currentUserId))) {
            return ok((ImFriendRequestRespVO) null);
        }
        return ok(CollUtil.getFirst(buildList(Collections.singletonList(request))));
    }

    // ========== 私有方法：VO 组装 ==========

    private List<ImFriendRequestRespVO> buildList(List<ImFriendRequestDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 双向 OR 列表，userIds 取 from + to 两组并集
        Set<Long> userIds = convertSetByFlatMap(list,
                request -> Stream.of(request.getFromUserId(), request.getToUserId()));
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        return convertList(list, request -> {
            ImFriendRequestRespVO vo = BeanUtils.toBean(request, ImFriendRequestRespVO.class);
            MapUtils.findAndThen(userMap, request.getFromUserId(), user ->
                    vo.setFromNickname(user.getName()).setFromAvatar(user.getAvatar()));
            MapUtils.findAndThen(userMap, request.getToUserId(), user ->
                    vo.setToNickname(user.getName()).setToAvatar(user.getAvatar()));
            return vo;
        });
    }

}
