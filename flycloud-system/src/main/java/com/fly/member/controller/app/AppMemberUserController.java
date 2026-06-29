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

}
