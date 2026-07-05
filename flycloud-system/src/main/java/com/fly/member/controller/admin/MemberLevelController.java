package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberLevelService;
import com.fly.system.api.member.domain.bo.MemberLevelBo;
import com.fly.system.api.member.domain.vo.MemberLevelRespVo;
import com.fly.system.api.member.domain.vo.MemberLevelSimpleRespVo;
import com.fly.system.api.member.domain.vo.MemberLevelVo;
import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员等级控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/level")
public class MemberLevelController {

    private final IMemberLevelService levelService;

    @PreAuthorize("@pms.hasPermission('member:level:list')")
    @GetMapping("/page")
    public R<PageVo<MemberLevelRespVo>> page(MemberLevelBo bo, PageBo pageBo) {
        return R.ok(convertPage(levelService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('member:level:list')")
    @GetMapping({"/allList", "/list-all-simple"})
    public R<List<MemberLevelSimpleRespVo>> allList(MemberLevelBo bo) {
        return R.ok(BeanUtil.copyToList(levelService.queryList(bo), MemberLevelSimpleRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:level:list')")
    @GetMapping("/list")
    public R<List<MemberLevelRespVo>> list(MemberLevelBo bo) {
        return R.ok(BeanUtil.copyToList(levelService.queryList(bo), MemberLevelRespVo.class));
    }

    @GetMapping("/get/{id}")
    public R<MemberLevelRespVo> getInfo(@PathVariable Long id) {
        return R.ok(BeanUtil.toBean(levelService.queryById(id), MemberLevelRespVo.class));
    }

    /**
     * 获得详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<MemberLevelRespVo> yudaoGet(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(levelService.queryById(id), MemberLevelRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:level:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody MemberLevelBo bo) {
        if (bo.getId() == null) {
            return R.ok(levelService.createLevel(bo));
        }
        levelService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody MemberLevelBo bo) {
        return R.result(levelService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:level:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Boolean> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.result(levelService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(levelService.deleteById(id));
    }

    private PageVo<MemberLevelRespVo> convertPage(PageVo<MemberLevelVo> page) {
        PageVo<MemberLevelRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), MemberLevelRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}
