package com.fly.member.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberSignInRecordService;
import com.fly.system.api.member.domain.vo.AppMemberSignInRecordSummaryRespVo;
import com.fly.system.api.member.domain.vo.MemberSignInRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端 - 会员签到记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/app/member/sign-in-record", "/app/member/sign-in/record"})
public class AppMemberSignInRecordController {

    private final IMemberSignInRecordService signInRecordService;

    /**
     * 查询当前会员签到记录分页。
     */
    @GetMapping("/page")
    public R<PageVo<MemberSignInRecordVo>> page(PageBo pageBo) {
        return R.ok(signInRecordService.queryAppPage(UserUtils.getCurUserId(), pageBo));
    }

    /**
     * 获取当前会员签到统计。
     */
    @GetMapping("/get-summary")
    public R<AppMemberSignInRecordSummaryRespVo> getSummary() {
        return R.ok(signInRecordService.getSummary(UserUtils.getCurUserId()));
    }

    /**
     * 当前会员签到。
     */
    @PostMapping("/create")
    public R<MemberSignInRecordVo> create() {
        return R.ok(signInRecordService.createSignRecord(UserUtils.getCurUserId()));
    }

}
