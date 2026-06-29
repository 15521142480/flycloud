package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberExperienceRecordService;
import com.fly.system.api.member.domain.bo.MemberExperienceRecordBo;
import com.fly.system.api.member.domain.vo.MemberExperienceRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 会员经验记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/experience-record")
public class MemberExperienceRecordController {

    private final IMemberExperienceRecordService experienceRecordService;

    @PreAuthorize("@pms.hasPermission('member:experience-record:list')")
    @GetMapping("/list")
    public R<PageVo<MemberExperienceRecordVo>> list(MemberExperienceRecordBo bo, PageBo pageBo) {
        return R.ok(experienceRecordService.queryPageList(bo, pageBo));
    }

}
