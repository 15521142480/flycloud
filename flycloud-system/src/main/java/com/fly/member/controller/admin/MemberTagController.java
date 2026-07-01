package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberTagService;
import com.fly.system.api.member.domain.bo.MemberTagBo;
import com.fly.system.api.member.domain.vo.MemberTagVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员标签控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/tag")
public class MemberTagController {

    private final IMemberTagService tagService;

    @PreAuthorize("@pms.hasPermission('member:tag:list')")
    @GetMapping({"/list", "/page"})
    public R<PageVo<MemberTagVo>> list(MemberTagBo bo, PageBo pageBo) {
        return R.ok(tagService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:list')")
    @GetMapping({"/allList", "/list-all-simple"})
    public R<List<MemberTagVo>> allList(MemberTagBo bo) {
        return R.ok(tagService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:query')")
    @GetMapping("/get/{id}")
    public R<MemberTagVo> getInfo(@PathVariable Long id) {
        return R.ok(tagService.queryById(id));
    }

    /**
     * 获得详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<MemberTagVo> yudaoGet(@RequestParam("id") Long id) {
        return R.ok(tagService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody MemberTagBo bo) {
        return R.ok(tagService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody MemberTagBo bo) {
        return R.ok(tagService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tagService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(tagService.deleteById(id));
    }

}
