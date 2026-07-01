package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.exception.ServiceException;
import com.fly.system.api.member.domain.bo.AppSocialUserBindReqBo;
import com.fly.system.api.member.domain.bo.AppSocialUserUnbindReqBo;
import com.fly.system.api.member.domain.bo.AppSocialWxaQrcodeReqBo;
import com.fly.system.api.member.domain.vo.AppSocialUserRespVo;
import com.fly.system.api.member.domain.vo.AppSocialWxaSubscribeTemplateRespVo;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 会员社交用户控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/app/member/social-user", "/member/social-user"})
public class AppSocialUserController {

    /**
     * 社交绑定。
     */
    @PostMapping("/bind")
    @PermitAll
    public R<String> socialBind(@Valid @RequestBody AppSocialUserBindReqBo reqBo) {
        throw new ServiceException("社交绑定能力未配置");
    }

    /**
     * 取消社交绑定。
     */
    @DeleteMapping("/unbind")
    public R<Boolean> socialUnbind(@RequestBody AppSocialUserUnbindReqBo reqBo) {
        throw new ServiceException("社交绑定能力未配置");
    }

    /**
     * 获得社交用户。
     */
    @GetMapping("/get")
    public R<AppSocialUserRespVo> getSocialUser(@RequestParam("type") Integer type) {
        return R.ok((AppSocialUserRespVo) null);
    }

    /**
     * 获得微信小程序码。
     */
    @PostMapping("/wxa-qrcode")
    @PermitAll
    public R<String> getWxaQrcode(@Valid @RequestBody AppSocialWxaQrcodeReqBo reqBo) {
        throw new ServiceException("微信小程序码能力未配置");
    }

    /**
     * 获得微信小程序订阅模板列表。
     */
    @GetMapping("/get-subscribe-template-list")
    @PermitAll
    public R<List<AppSocialWxaSubscribeTemplateRespVo>> getSubscribeTemplateList() {
        return R.ok(List.of());
    }

}
