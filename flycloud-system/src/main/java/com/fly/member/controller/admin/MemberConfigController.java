package com.fly.member.controller.admin;

import com.fly.common.domain.model.R;
import com.fly.member.service.IMemberConfigService;
import com.fly.system.api.member.domain.bo.MemberConfigBo;
import com.fly.system.api.member.domain.vo.MemberConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 会员配置控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/config")
public class MemberConfigController {

    private final IMemberConfigService configService;

    /**
     * 获取会员配置。
     */
    @PreAuthorize("@pms.hasPermission('member:config:list')")
    @GetMapping("/get")
    public R<MemberConfigVo> getConfig() {
        return R.ok(configService.getConfig());
    }

    /**
     * 保存会员配置。
     */
    @PreAuthorize("@pms.hasPermission('member:config:save')")
    @PostMapping("/save")
    public R<Void> saveConfig(@RequestBody MemberConfigBo bo) {
        return R.ok(configService.saveConfig(bo));
    }

}
