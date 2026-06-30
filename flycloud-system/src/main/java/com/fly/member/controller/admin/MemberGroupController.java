package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberGroupService;
import com.fly.system.api.member.domain.bo.MemberGroupBo;
import com.fly.system.api.member.domain.vo.MemberGroupVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员分组控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/group")
public class MemberGroupController {

    private final IMemberGroupService groupService;

    @PreAuthorize("@pms.hasPermission('member:group:list')")
    @GetMapping("/list")
    public R<PageVo<MemberGroupVo>> list(MemberGroupBo bo, PageBo pageBo) {
        return R.ok(groupService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:group:list')")
    @GetMapping("/allList")
    public R<List<MemberGroupVo>> allList(MemberGroupBo bo) {
        return R.ok(groupService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:group:query')")
    @GetMapping("/get/{id}")
    public R<MemberGroupVo> getInfo(@PathVariable Long id) {
        return R.ok(groupService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:group:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody MemberGroupBo bo) {
        return R.ok(groupService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:group:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(groupService.deleteById(id));
    }

}
