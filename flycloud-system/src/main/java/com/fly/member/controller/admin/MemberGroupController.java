package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberGroupService;
import com.fly.system.api.member.domain.bo.MemberGroupBo;
import com.fly.system.api.member.domain.vo.MemberGroupRespVo;
import com.fly.system.api.member.domain.vo.MemberGroupSimpleRespVo;
import com.fly.system.api.member.domain.vo.MemberGroupVo;
import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员分组控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/group")
public class MemberGroupController {

    private final IMemberGroupService groupService;

    @PreAuthorize("@pms.hasPermission('member:group:list')")
    @GetMapping({"/list", "/page"})
    public R<PageVo<MemberGroupRespVo>> list(MemberGroupBo bo, PageBo pageBo) {
        return R.ok(convertPage(groupService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('member:group:list')")
    @GetMapping({"/allList", "/list-all-simple"})
    public R<List<MemberGroupSimpleRespVo>> allList(MemberGroupBo bo) {
        return R.ok(BeanUtil.copyToList(groupService.queryList(bo), MemberGroupSimpleRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:group:query')")
    @GetMapping("/get/{id}")
    public R<MemberGroupRespVo> getInfo(@PathVariable Long id) {
        return R.ok(BeanUtil.toBean(groupService.queryById(id), MemberGroupRespVo.class));
    }

    /**
     * 获得详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<MemberGroupRespVo> yudaoGet(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(groupService.queryById(id), MemberGroupRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:group:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody MemberGroupBo bo) {
        if (bo.getId() == null) {
            return R.ok(groupService.createGroup(bo));
        }
        groupService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody MemberGroupBo bo) {
        return R.result(groupService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:group:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Boolean> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.result(groupService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(groupService.deleteById(id));
    }

    private PageVo<MemberGroupRespVo> convertPage(PageVo<MemberGroupVo> page) {
        PageVo<MemberGroupRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), MemberGroupRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}
