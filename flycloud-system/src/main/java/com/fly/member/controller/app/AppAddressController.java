package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IAddressService;
import com.fly.system.api.member.domain.bo.AppAddressCreateReqBo;
import com.fly.system.api.member.domain.bo.AppAddressUpdateReqBo;
import com.fly.system.api.member.domain.vo.AppAddressRespVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 会员收件地址控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/member/address")
public class AppAddressController {

    private final IAddressService addressService;

    /**
     * 创建用户收件地址。
     */
    @PostMapping("/create")
    public R<Long> createAddress(@Valid @RequestBody AppAddressCreateReqBo createReqBo) {
        return R.ok(addressService.createAddress(UserUtils.getCurUserId(), createReqBo));
    }

    /**
     * 更新用户收件地址。
     */
    @PutMapping("/update")
    public R<Boolean> updateAddress(@Valid @RequestBody AppAddressUpdateReqBo updateReqBo) {
        return R.result(addressService.updateAddress(UserUtils.getCurUserId(), updateReqBo));
    }

    /**
     * 删除用户收件地址。
     */
    @DeleteMapping("/delete")
    public R<Boolean> deleteAddress(@RequestParam("id") Long id) {
        return R.result(addressService.deleteAddress(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得用户收件地址。
     */
    @GetMapping("/get")
    public R<AppAddressRespVo> getAddress(@RequestParam("id") Long id) {
        return R.ok(addressService.getAddress(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得默认用户收件地址。
     */
    @GetMapping("/get-default")
    public R<AppAddressRespVo> getDefaultUserAddress() {
        return R.ok(addressService.getDefaultUserAddress(UserUtils.getCurUserId()));
    }

    /**
     * 获得用户收件地址列表。
     */
    @GetMapping("/list")
    public R<List<AppAddressRespVo>> getAddressList() {
        return R.ok(addressService.getAddressList(UserUtils.getCurUserId()));
    }

}
