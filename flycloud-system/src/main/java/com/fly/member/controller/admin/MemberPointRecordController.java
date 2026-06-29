package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberPointRecordService;
import com.fly.system.api.member.domain.bo.MemberPointRecordBo;
import com.fly.system.api.member.domain.vo.MemberPointRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 会员积分记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/point-record")
public class MemberPointRecordController {

    private final IMemberPointRecordService pointRecordService;

    @PreAuthorize("@pms.hasPermission('member:point-record:list')")
    @GetMapping("/list")
    public R<PageVo<MemberPointRecordVo>> list(MemberPointRecordBo bo, PageBo pageBo) {
        return R.ok(pointRecordService.queryPageList(bo, pageBo));
    }

}
