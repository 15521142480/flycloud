package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberLevelService;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员用户控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/user")
public class MemberUserController {

    private final IMemberUserService memberUserService;
    private final IMemberLevelService memberLevelService;

    @PreAuthorize("@pms.hasPermission('member:user:list')")
    @GetMapping("/list")
    public R<PageVo<MemberUserVo>> list(MemberUserBo bo, PageBo pageBo) {
        return R.ok(memberUserService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:list')")
    @GetMapping("/allList")
    public R<List<MemberUserVo>> allList(MemberUserBo bo) {
        return R.ok(memberUserService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:query')")
    @GetMapping("/get/{id}")
    public R<MemberUserVo> getInfo(@PathVariable Long id) {
        return R.ok(memberUserService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:user:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody MemberUserBo bo) {
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:user:update-level')")
    @PostMapping("/update-level")
    public R<Void> updateLevel(@RequestBody MemberUserUpdateLevelReq req) {
        memberLevelService.updateUserLevel(req.getId(), req.getLevelId(), req.getReason());
        return R.ok();
    }

    @Data
    public static class MemberUserUpdateLevelReq {
        @NotNull(message = "会员编号不能为空")
        private Long id;
        private Long levelId;
        private String reason;
    }

}
