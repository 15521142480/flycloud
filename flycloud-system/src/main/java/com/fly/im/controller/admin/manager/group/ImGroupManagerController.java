package com.fly.im.controller.admin.manager.group;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.group.vo.ImGroupManagerBanReqVO;
import com.fly.im.controller.admin.manager.group.vo.ImGroupManagerPageReqVO;
import com.fly.im.controller.admin.manager.group.vo.ImGroupManagerRespVO;
import com.fly.im.dal.dataobject.group.ImGroupDO;
import com.fly.im.service.group.ImGroupMemberService;
import com.fly.im.service.group.ImGroupService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;
import static com.fly.common.security.util.UserUtils.getCurUserId;

@Tag(name = "管理后台 - IM 群聊管理")
@RestController
@RequestMapping("/im/manager/group")
@Validated
public class ImGroupManagerController {

    @Resource
    private ImGroupService groupService;
    @Resource
    private ImGroupMemberService groupMemberService;

    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/page")
    @Operation(summary = "获得群分页")
    @PreAuthorize("@pms.hasPermission('im:manager:group:query')")
    public R<PageResult<ImGroupManagerRespVO>> getGroupPage(@Valid ImGroupManagerPageReqVO pageReqVO) {
        // 1. 分页查询群
        PageResult<ImGroupDO> pageResult = groupService.getGroupPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 2.1 批量查询相关数据
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                convertSet(pageResult.getList(), ImGroupDO::getOwnerUserId));
        Map<Long, Long> memberCountMap = groupMemberService.getActiveMemberCountMap(
                convertSet(pageResult.getList(), ImGroupDO::getId));
        // 2.2 转换为 VO，填充群主昵称、群成员数量
        return ok(PageResult.convert(pageResult, ImGroupManagerRespVO.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getOwnerUserId(),
                    user -> vo.setOwnerNickname(user.getName()));
            vo.setMemberCount(memberCountMap.getOrDefault(vo.getId(), 0L).intValue());
        }));
    }

    @GetMapping("/get")
    @Operation(summary = "获得群详情")
    @Parameter(name = "id", description = "群编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:group:query')")
    public R<ImGroupManagerRespVO> getGroup(@RequestParam("id") Long id) {
        ImGroupDO group = groupService.getGroup(id);
        return ok(BeanUtils.toBean(group, ImGroupManagerRespVO.class));
    }

    @PutMapping("/ban")
    @Operation(summary = "封禁群")
    @PreAuthorize("@pms.hasPermission('im:manager:group:ban')")
    public R<Boolean> banGroup(@Valid @RequestBody ImGroupManagerBanReqVO reqVO) {
        groupService.banGroup(getCurUserId(), reqVO);
        return ok(true);
    }

    @PutMapping("/unban")
    @Operation(summary = "解封群")
    @Parameter(name = "id", description = "群编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:group:ban')")
    public R<Boolean> unbanGroup(@RequestParam("id") Long id) {
        groupService.unbanGroup(getCurUserId(), id);
        return ok(true);
    }

    @DeleteMapping("/dissolve")
    @Operation(summary = "解散群")
    @Parameter(name = "id", description = "群编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:group:dissolve')")
    public R<Boolean> dissolveGroup(@RequestParam("id") Long id) {
        groupService.dissolveGroupByManager(getCurUserId(), id);
        return ok(true);
    }

}
