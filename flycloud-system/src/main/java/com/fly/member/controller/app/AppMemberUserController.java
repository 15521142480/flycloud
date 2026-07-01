package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.member.domain.bo.MemberUserBo;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端 - 会员用户控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/member/user")
public class AppMemberUserController {

    private final IMemberUserService memberUserService;

    /**
     * 获取当前会员用户信息。
     */
    @GetMapping("/get")
    public R<MemberUserVo> getUser() {
        return R.ok(memberUserService.queryById(UserUtils.getCurUserId()));
    }

    /**
     * 更新当前会员用户信息。
     */
    @PostMapping("/update")
    public R<Void> updateUser(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 更新当前会员用户信息，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdateUser(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 修改当前会员手机号。
     */
    @PutMapping("/update-mobile")
    public R<Void> updateMobile(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 通过微信修改当前会员手机号。
     */
    @PutMapping("/update-mobile-by-weixin")
    public R<Void> updateMobileByWeixin(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 修改当前会员密码。
     */
    @PutMapping("/update-password")
    public R<Void> updatePassword(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

    /**
     * 重置当前会员密码。
     */
    @PutMapping("/reset-password")
    public R<Void> resetPassword(@RequestBody MemberUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        return R.ok(memberUserService.saveOrUpdate(bo));
    }

}
