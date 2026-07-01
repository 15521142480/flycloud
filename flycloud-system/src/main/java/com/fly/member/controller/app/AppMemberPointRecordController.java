package com.fly.member.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberPointRecordService;
import com.fly.system.api.member.domain.bo.MemberPointRecordBo;
import com.fly.system.api.member.domain.vo.MemberPointRecordVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 会员积分记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/app/member/point-record", "/app/member/point/record"})
public class AppMemberPointRecordController {

    private final IMemberPointRecordService pointRecordService;

    /**
     * 查询当前会员积分记录分页。
     */
    @GetMapping("/page")
    public R<PageVo<MemberPointRecordVo>> page(MemberPointRecordBo bo, PageBo pageBo) {
        bo.setUserId(UserUtils.getCurUserId());
        return R.ok(pointRecordService.queryPageList(bo, pageBo));
    }

}
