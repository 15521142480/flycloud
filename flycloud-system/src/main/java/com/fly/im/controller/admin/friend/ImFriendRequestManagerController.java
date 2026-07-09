package com.fly.im.controller.admin.friend;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.system.api.im.domain.bo.ImFriendRequestManagerPageBo;
import com.fly.system.api.im.domain.vo.ImFriendRequestManagerVo;
import com.fly.system.api.im.domain.ImFriendRequest;
import com.fly.im.service.friend.ImFriendRequestService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSetByFlatMap;

@Tag(name = "管理后台 - IM 好友申请管理")
@RestController
@RequestMapping({"/im/manager/friend-request", "/admin/im/manager/friend-request"})
@Validated
public class ImFriendRequestManagerController {

    @Resource
    private ImFriendRequestService friendRequestService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得好友申请分页")
    @PreAuthorize("@pms.hasPermission('im:friend:request:list')")
    public R<PageResult<ImFriendRequestManagerVo>> getFriendRequestPage(
            @Valid ImFriendRequestManagerPageBo pageReqVo) {
        // 1. 分页查询
        PageResult<ImFriendRequest> pageResult = friendRequestService.getFriendRequestPage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }

        // 2.1 一次性批量查询发起方 + 接收方的昵称
        Set<Long> userIds = convertSetByFlatMap(pageResult.getList(),
                request -> Stream.of(request.getFromUserId(), request.getToUserId()));
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        // 2.2 转换为 VO，填充昵称
        return ok(PageResult.convert(pageResult, ImFriendRequestManagerVo.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getFromUserId(),
                    user -> vo.setFromNickname(user.getName()));
            MapUtils.findAndThen(userMap, vo.getToUserId(),
                    user -> vo.setToNickname(user.getName()));
        }));
    }

}
