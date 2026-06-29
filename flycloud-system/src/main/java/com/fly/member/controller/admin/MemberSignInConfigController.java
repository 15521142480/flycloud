package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberSignInConfigService;
import com.fly.system.api.member.domain.bo.MemberSignInConfigBo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员签到规则控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/sign-in-config")
public class MemberSignInConfigController {

    private final IMemberSignInConfigService signInConfigService;

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:list')")
    @GetMapping("/list")
    public R<PageVo<MemberSignInConfigVo>> list(MemberSignInConfigBo bo, PageBo pageBo) {
        return R.ok(signInConfigService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:list')")
    @GetMapping("/allList")
    public R<List<MemberSignInConfigVo>> allList(MemberSignInConfigBo bo) {
        return R.ok(signInConfigService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:query')")
    @GetMapping("/get/{id}")
    public R<MemberSignInConfigVo> getInfo(@PathVariable Long id) {
        return R.ok(signInConfigService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody MemberSignInConfigBo bo) {
        return R.ok(signInConfigService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(signInConfigService.deleteById(id));
    }

}
