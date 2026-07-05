package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IMemberSignInConfigService;
import com.fly.system.api.member.domain.bo.MemberSignInConfigBo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigRespVo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigVo;
import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 会员签到规则控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/member/sign-in-config", "/admin/member/sign-in/config"})
public class MemberSignInConfigController {

    private final IMemberSignInConfigService signInConfigService;

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:list')")
    @GetMapping("/list")
    public R<List<MemberSignInConfigRespVo>> list(MemberSignInConfigBo bo) {
        return R.ok(BeanUtil.copyToList(signInConfigService.queryList(bo), MemberSignInConfigRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:list')")
    @GetMapping("/page")
    public R<PageVo<MemberSignInConfigRespVo>> page(MemberSignInConfigBo bo, PageBo pageBo) {
        return R.ok(convertPage(signInConfigService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:list')")
    @GetMapping("/allList")
    public R<List<MemberSignInConfigRespVo>> allList(MemberSignInConfigBo bo) {
        return R.ok(BeanUtil.copyToList(signInConfigService.queryList(bo), MemberSignInConfigRespVo.class));
    }

    @GetMapping("/get/{id}")
    public R<MemberSignInConfigRespVo> getInfo(@PathVariable Long id) {
        return R.ok(BeanUtil.toBean(signInConfigService.queryById(id), MemberSignInConfigRespVo.class));
    }

    /**
     * 获得详情，兼容 yudao 前端接口。
     */
    @GetMapping("/get")
    public R<MemberSignInConfigRespVo> yudaoGet(@RequestParam("id") Long id) {
        return R.ok(BeanUtil.toBean(signInConfigService.queryById(id), MemberSignInConfigRespVo.class));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody MemberSignInConfigBo bo) {
        if (bo.getId() == null) {
            return R.ok(signInConfigService.createSignInConfig(bo));
        }
        signInConfigService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody MemberSignInConfigBo bo) {
        return R.result(signInConfigService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('member:sign-in-config:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Boolean> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.result(signInConfigService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(signInConfigService.deleteById(id));
    }

    private PageVo<MemberSignInConfigRespVo> convertPage(PageVo<MemberSignInConfigVo> page) {
        PageVo<MemberSignInConfigRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), MemberSignInConfigRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}
