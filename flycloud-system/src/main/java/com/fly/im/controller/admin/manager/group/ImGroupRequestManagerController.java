package com.fly.im.controller.admin.manager.group;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.group.vo.ImGroupRequestManagerPageReqVO;
import com.fly.im.controller.admin.manager.group.vo.ImGroupRequestManagerRespVO;
import com.fly.im.dal.dataobject.group.ImGroupDO;
import com.fly.im.dal.dataobject.group.ImGroupRequestDO;
import com.fly.im.service.group.ImGroupRequestService;
import com.fly.im.service.group.ImGroupService;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;
import static com.fly.common.utils.collection.CollectionUtils.convertSetByFlatMap;

@Tag(name = "管理后台 - IM 加群申请管理")
@RestController
@RequestMapping("/im/manager/group-request")
@Validated
public class ImGroupRequestManagerController {

    @Resource
    private ImGroupRequestService groupRequestService;
    @Resource
    private ImGroupService groupService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得加群申请分页")
    @PreAuthorize("@pms.hasPermission('im:manager:group-request:query')")
    public R<PageResult<ImGroupRequestManagerRespVO>> getGroupRequestPage(
            @Valid ImGroupRequestManagerPageReqVO pageReqVO) {
        // 1. 分页查询
        PageResult<ImGroupRequestDO> pageResult = groupRequestService.getGroupRequestPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }

        // 2.1 批量聚合 user / inviter / handler 用户昵称
        Set<Long> userIds = convertSetByFlatMap(pageResult.getList(),
                request -> Stream.of(request.getUserId(), request.getInviterUserId(), request.getHandleUserId())
                        .filter(Objects::nonNull));
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        // 2.2 批量聚合群信息（取群名）
        Set<Long> groupIds = convertSet(pageResult.getList(), ImGroupRequestDO::getGroupId);
        Map<Long, ImGroupDO> groupMap = groupService.getGroupMap(groupIds);
        return ok(PageResult.convert(pageResult, ImGroupRequestManagerRespVO.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getUserId(), user -> vo.setUserNickname(user.getName()));
            MapUtils.findAndThen(userMap, vo.getInviterUserId(), user -> vo.setInviterNickname(user.getName()));
            MapUtils.findAndThen(userMap, vo.getHandleUserId(), user -> vo.setHandleNickname(user.getName()));
            MapUtils.findAndThen(groupMap, vo.getGroupId(), group -> vo.setGroupName(group.getName()));
        }));
    }

}
