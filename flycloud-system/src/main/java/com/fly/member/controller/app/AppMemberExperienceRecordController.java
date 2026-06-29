package com.fly.member.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberExperienceRecordService;
import com.fly.system.api.member.domain.bo.MemberExperienceRecordBo;
import com.fly.system.api.member.domain.vo.MemberExperienceRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 会员经验记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/member/experience-record")
public class AppMemberExperienceRecordController {

    private final IMemberExperienceRecordService experienceRecordService;

    /**
     * 查询当前会员经验记录分页。
     */
    @GetMapping("/page")
    public R<PageVo<MemberExperienceRecordVo>> page(MemberExperienceRecordBo bo, PageBo pageBo) {
        bo.setUserId(UserUtils.getCurUserId());
        return R.ok(experienceRecordService.queryPageList(bo, pageBo));
    }

}
