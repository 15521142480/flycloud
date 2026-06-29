package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.member.service.IMemberSignInConfigService;
import com.fly.system.api.member.domain.bo.MemberSignInConfigBo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 会员签到规则控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/member/sign-in-config")
public class AppMemberSignInConfigController {

    private final IMemberSignInConfigService signInConfigService;

    /**
     * 查询启用签到规则列表。
     */
    @GetMapping("/list")
    public R<List<MemberSignInConfigVo>> list() {
        MemberSignInConfigBo bo = new MemberSignInConfigBo();
        bo.setStatus(0);
        return R.ok(signInConfigService.queryList(bo));
    }

}
