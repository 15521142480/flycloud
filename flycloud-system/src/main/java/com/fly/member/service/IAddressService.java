package com.fly.member.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.member.domain.bo.AppAddressCreateReqBo;
import com.fly.system.api.member.domain.bo.AppAddressUpdateReqBo;
import com.fly.system.api.member.domain.bo.MemberAddressBo;
import com.fly.system.api.member.domain.vo.AppAddressRespVo;
import com.fly.system.api.member.domain.vo.MemberAddressVo;

import java.util.Collection;
import java.util.List;

/**
 * 会员收件地址 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IAddressService {

    /**
     * 创建移动端会员收件地址。
     */
    Long createAddress(Long userId, AppAddressCreateReqBo createReqBo);

    /**
     * 更新移动端会员收件地址。
     */
    Boolean updateAddress(Long userId, AppAddressUpdateReqBo updateReqBo);

    /**
     * 删除移动端会员收件地址。
     */
    Boolean deleteAddress(Long userId, Long id);

    /**
     * 查询移动端会员收件地址。
     */
    AppAddressRespVo getAddress(Long userId, Long id);

    /**
     * 查询移动端默认会员收件地址。
     */
    AppAddressRespVo getDefaultUserAddress(Long userId);

    /**
     * 查询移动端会员收件地址列表。
     */
    List<AppAddressRespVo> getAddressList(Long userId);

    /**
     * 查询会员收件地址详情。
     */
    MemberAddressVo queryById(Long id);

    /**
     * 分页查询会员收件地址。
     */
    PageVo<MemberAddressVo> queryPageList(MemberAddressBo bo, PageBo pageBo);

    /**
     * 查询会员收件地址列表。
     */
    List<MemberAddressVo> queryList(MemberAddressBo bo);

    /**
     * 新增或修改会员收件地址。
     */
    Boolean saveOrUpdate(MemberAddressBo bo);

    /**
     * 校验并批量删除会员收件地址。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
