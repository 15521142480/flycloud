package com.fly.member.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.member.service.IAddressService;
import com.fly.system.api.member.domain.bo.MemberAddressBo;
import com.fly.system.api.member.domain.vo.AddressRespVo;
import com.fly.system.api.member.domain.vo.MemberAddressVo;
import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 后台 - 会员收件地址控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member/address")
public class AddressController {

    private final IAddressService addressService;

    /**
     * 查询会员收件地址列表。
     */
//    @PreAuthorize("@pms.hasPermission('member:address:list')")
    @GetMapping("/list")
    public R<List<AddressRespVo>> list(MemberAddressBo bo) {
        return R.ok(BeanUtil.copyToList(addressService.queryList(bo), AddressRespVo.class));
    }

    /**
     * 查询会员收件地址分页列表。
     */
//    @PreAuthorize("@pms.hasPermission('member:address:list')")
    @GetMapping("/page")
    public R<PageVo<MemberAddressVo>> page(MemberAddressBo bo, PageBo page) {
        return R.ok(addressService.queryPageList(bo, page));
    }

    /**
     * 查询会员收件地址列表。
     */
//    @PreAuthorize("@pms.hasPermission('member:address:list')")
    @GetMapping("/allList")
    public R<List<AddressRespVo>> allList(MemberAddressBo bo) {
        return R.ok(BeanUtil.copyToList(addressService.queryList(bo), AddressRespVo.class));
    }

    /**
     * 获取会员收件地址详情。
     */
    @GetMapping("/get/{id}")
    public R<MemberAddressVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(addressService.queryById(id));
    }

    /**
     * 新增或修改会员收件地址。
     */
//    @PreAuthorize("@pms.hasPermission('member:address:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Boolean> saveOrUpdate(@RequestBody MemberAddressBo bo) {
        return R.result(addressService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody MemberAddressBo bo) {
        return R.result(addressService.saveOrUpdate(bo));
    }

    /**
     * 删除会员收件地址。
     */
//    @PreAuthorize("@pms.hasPermission('member:address:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(addressService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(addressService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}
