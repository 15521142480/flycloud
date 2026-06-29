package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberLevelService;
import com.fly.system.api.member.domain.bo.MemberLevelBo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员等级控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/level")
public class MemberLevelController {

    private final IMemberLevelService levelService;

    @PreAuthorize("@pms.hasPermission('member:level:list')")
    @GetMapping("/list")
    public R<PageVo<MemberLevelVo>> list(MemberLevelBo bo, PageBo pageBo) {
        return R.ok(levelService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:level:list')")
    @GetMapping("/allList")
    public R<List<MemberLevelVo>> allList(MemberLevelBo bo) {
        return R.ok(levelService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:level:query')")
    @GetMapping("/get/{id}")
    public R<MemberLevelVo> getInfo(@PathVariable Long id) {
        return R.ok(levelService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:level:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody MemberLevelBo bo) {
        return R.ok(levelService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:level:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(levelService.deleteById(id));
    }

}
