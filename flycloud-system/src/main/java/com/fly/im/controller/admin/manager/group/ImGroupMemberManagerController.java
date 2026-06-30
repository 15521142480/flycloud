package com.fly.im.controller.admin.manager.group;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.group.vo.member.ImGroupMemberManagerRespVO;
import com.fly.im.dal.dataobject.group.ImGroupMemberDO;
import com.fly.im.service.group.ImGroupMemberService;
import com.fly.im.framework.system.AdminUserApi;
import com.fly.system.api.system.domain.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - IM 群成员管理")
@RestController
@RequestMapping("/im/manager/group/member")
@Validated
public class ImGroupMemberManagerController {

    @Resource
    private ImGroupMemberService groupMemberService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/list")
    @Operation(summary = "获得群成员列表（含已退群成员，由前端按需过滤）")
    @Parameter(name = "groupId", description = "群编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:group:query')")
    public R<List<ImGroupMemberManagerRespVO>> getGroupMemberList(@RequestParam("groupId") Long groupId) {
        // 1. 查询群全部成员（含已退群）
        List<ImGroupMemberDO> members = groupMemberService.getGroupMemberListByGroupId(groupId);
        if (CollUtil.isEmpty(members)) {
            return ok(Collections.emptyList());
        }
        // 2.1 批量查询用户信息
        Map<Long, SysUserVo> userMap = adminUserApi.getUserMap(
                convertSet(members, ImGroupMemberDO::getUserId));
        // 2.2 转换为 VO，填充昵称、头像
        return ok(BeanUtils.toBean(members, ImGroupMemberManagerRespVO.class, vo ->
                MapUtils.findAndThen(userMap, vo.getUserId(), user ->
                        vo.setNickname(user.getName()).setAvatar(user.getAvatar()))));
    }

}
