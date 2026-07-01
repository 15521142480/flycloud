package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberSignInRecordService;
import com.fly.system.api.member.domain.bo.MemberSignInRecordBo;
import com.fly.system.api.member.domain.vo.MemberSignInRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 会员签到记录控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/member/sign-in-record", "/admin/member/sign-in/record"})
public class MemberSignInRecordController {

    private final IMemberSignInRecordService signInRecordService;

    @PreAuthorize("@pms.hasPermission('member:sign:list')")
    @GetMapping({"/list", "/page"})
    public R<PageVo<MemberSignInRecordVo>> list(MemberSignInRecordBo bo, PageBo pageBo) {
        return R.ok(signInRecordService.queryPageList(bo, pageBo));
    }

}
