package com.fly.im.controller.admin.group;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.message.group.ImGroupMessageManagerPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.message.group.ImGroupMessageManagerRespVo;
import com.fly.system.api.im.domain.group.ImGroup;
import com.fly.system.api.im.domain.message.ImGroupMessage;
import com.fly.im.service.group.ImGroupService;
import com.fly.im.service.message.ImGroupMessageService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;
import static com.fly.common.utils.collection.CollectionUtils.convertSetByFlatMap;
import static com.fly.system.api.im.enums.ImCommonConstants.AT_USER_ID_ALL;

@Tag(name = "管理后台 - IM 群聊消息")
@RestController
@RequestMapping({"/im/manager/message/group", "/admin/im/manager/message/group"})
@Validated
public class ImGroupMessageManagerController {

    @Resource
    private ImGroupMessageService groupMessageService;
    @Resource
    private ImGroupService groupService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得群聊消息分页")
    @PreAuthorize("@pms.hasPermission('im:friend:message:list')")
    public R<PageResult<ImGroupMessageManagerRespVo>> getGroupMessagePage(
            @Valid ImGroupMessageManagerPageReqVo pageReqVo) {
        // 1. 分页查询
        PageResult<ImGroupMessage> pageResult = groupMessageService.getGroupMessagePage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 2.1 批量查询群名称、发送人昵称、@ 用户昵称（-1 表示 @所有人，跳过查询，由前端判断渲染）
        Map<Long, ImGroup> groupMap = groupService.getGroupMap(
                convertSet(pageResult.getList(), ImGroupMessage::getGroupId));
        Set<Long> userIds = convertSetByFlatMap(pageResult.getList(), m -> Stream.concat(
                Stream.of(m.getSenderId()),
                CollUtil.emptyIfNull(m.getAtUserIds()).stream()
                        .filter(id -> !Objects.equals(id, AT_USER_ID_ALL))));
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(userIds);
        // 2.2 转换为 VO，填充群名 / 发送人昵称 / @ 用户昵称（-1 位置留 null，由前端展示「@所有人」）
        return ok(PageResult.convert(pageResult, ImGroupMessageManagerRespVo.class, vo -> {
            MapUtils.findAndThen(groupMap, vo.getGroupId(), group -> vo.setGroupName(group.getName()));
            MapUtils.findAndThen(userMap, vo.getSenderId(), user -> vo.setSenderNickname(user.getName()));
            if (CollUtil.isNotEmpty(vo.getAtUserIds())) {
                vo.setAtUserNicknames(convertList(vo.getAtUserIds(), id -> {
                    SysUserVo user = userMap.get(id);
                    return user != null ? user.getName() : null;
                }));
            }
        }));
    }

    @GetMapping("/get")
    @Operation(summary = "获得群聊消息详情")
    @Parameter(name = "id", description = "消息编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:friend:message:list')")
    public R<ImGroupMessageManagerRespVo> getGroupMessage(@RequestParam("id") Long id) {
        ImGroupMessage message = groupMessageService.getGroupMessage(id);
        return ok(BeanUtils.toBean(message, ImGroupMessageManagerRespVo.class));
    }

}
