package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberLevelRecordService;
import com.fly.system.api.member.domain.bo.MemberLevelRecordBo;
import com.fly.system.api.member.domain.vo.MemberLevelRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 会员等级记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/level-record")
public class MemberLevelRecordController {

    private final IMemberLevelRecordService levelRecordService;

    @PreAuthorize("@pms.hasPermission('member:level-record:list')")
    @GetMapping("/list")
    public R<PageVo<MemberLevelRecordVo>> list(MemberLevelRecordBo bo, PageBo pageBo) {
        return R.ok(levelRecordService.queryPageList(bo, pageBo));
    }

}
