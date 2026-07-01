package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.member.service.IMemberLevelService;
import com.fly.system.api.member.domain.bo.MemberLevelBo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 会员等级控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/member/level")
public class AppMemberLevelController {

    private final IMemberLevelService memberLevelService;

    /**
     * 查询启用会员等级列表。
     */
    @GetMapping("/list")
    public R<List<MemberLevelVo>> list() {
        MemberLevelBo bo = new MemberLevelBo();
        bo.setStatus(0);
        return R.ok(memberLevelService.queryList(bo));
    }

    /**
     * 获取会员等级详情。
     */
    @GetMapping("/get")
    public R<MemberLevelVo> get(@RequestParam Long id) {
        return R.ok(memberLevelService.queryById(id));
    }

}
