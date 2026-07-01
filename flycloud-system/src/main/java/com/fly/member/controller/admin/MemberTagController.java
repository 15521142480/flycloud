package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberTagService;
import com.fly.system.api.member.domain.bo.MemberTagBo;
import com.fly.system.api.member.domain.vo.MemberTagRespVo;
import com.fly.system.api.member.domain.vo.MemberTagVo;
import cn.hutool.core.bean.BeanUtil;
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
    @GetMapping("/page")
    public R<PageVo<MemberTagRespVo>> page(MemberTagBo bo, PageBo pageBo) {
        return R.ok(convertPage(tagService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:list')")
    @GetMapping({"/allList", "/list-all-simple"})
    public R<List<MemberTagRespVo>> allList(MemberTagBo bo) {
        return R.ok(BeanUtil.copyToList(tagService.queryList(bo), MemberTagRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:list')")
    @GetMapping("/list")
    public R<List<MemberTagRespVo>> list(MemberTagBo bo) {
        return R.ok(BeanUtil.copyToList(tagService.queryList(bo), MemberTagRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:query')")
    @GetMapping("/get/{id}")
    public R<MemberTagRespVo> getInfo(@PathVariable Long id) {
        return R.ok(BeanUtil.toBean(tagService.queryById(id), MemberTagRespVo.class));
    }

    /**
     * 获得详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<MemberTagRespVo> yudaoGet(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(tagService.queryById(id), MemberTagRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody MemberTagBo bo) {
        if (bo.getId() == null) {
            return R.ok(tagService.createTag(bo));
        }
        tagService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody MemberTagBo bo) {
        return R.ok(tagService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:tag:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Boolean> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tagService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(tagService.deleteById(id));
    }

    private PageVo<MemberTagRespVo> convertPage(PageVo<MemberTagVo> page) {
        PageVo<MemberTagRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), MemberTagRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}
