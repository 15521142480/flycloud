package com.fly.im.controller.admin.friend;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.system.api.im.domain.bo.ImFriendManagerPageBo;
import com.fly.system.api.im.domain.vo.ImFriendManagerVo;
import com.fly.system.api.im.domain.ImFriend;
import com.fly.im.service.friend.ImFriendService;
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

@Tag(name = "管理后台 - IM 好友管理")
@RestController
@RequestMapping({"/im/manager/friend", "/admin/im/manager/friend"})
@Validated
public class ImFriendManagerController {

    @Resource
    private ImFriendService friendService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得好友关系分页")
    @PreAuthorize("@pms.hasPermission('im:friend:message:list')")
    public R<PageResult<ImFriendManagerVo>> getFriendPage(
            @Valid ImFriendManagerPageBo pageReqVo) {
        // 1. 分页查询
        PageResult<ImFriend> pageResult = friendService.getFriendPage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 2.1 一次性批量查询用户 + 好友的昵称
        Set<Long> userIds = convertSetByFlatMap(pageResult.getList(),
                f -> Stream.of(f.getUserId(), f.getFriendUserId()));
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        // 2.2 转换为 VO，填充昵称
        return ok(PageResult.convert(pageResult, ImFriendManagerVo.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getUserId(),
                    user -> vo.setUserNickname(user.getName()));
            MapUtils.findAndThen(userMap, vo.getFriendUserId(),
                    user -> vo.setFriendNickname(user.getName()));
        }));
    }

}
