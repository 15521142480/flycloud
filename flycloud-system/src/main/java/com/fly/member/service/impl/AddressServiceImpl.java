package com.fly.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.member.mapper.MemberAddressMapper;
import com.fly.member.service.IAddressService;
import com.fly.system.api.member.domain.MemberAddress;
import com.fly.system.api.member.domain.bo.AppAddressCreateReqBo;
import com.fly.system.api.member.domain.bo.AppAddressUpdateReqBo;
import com.fly.system.api.member.domain.bo.MemberAddressBo;
import com.fly.system.api.member.domain.vo.AppAddressRespVo;
import com.fly.system.api.member.domain.vo.MemberAddressVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 会员收件地址 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class AddressServiceImpl extends BaseServiceImpl<MemberAddressMapper, MemberAddress> implements IAddressService {

    private final MemberAddressMapper baseMapper;

    /**
     * 创建移动端会员收件地址。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAddress(Long userId, AppAddressCreateReqBo createReqBo) {
        if (Boolean.TRUE.equals(createReqBo.getDefaultStatus())) {
            cancelOtherDefaultAddress(userId, null);
        }
        LocalDateTime now = LocalDateTime.now();
        MemberAddress address = BeanUtil.toBean(createReqBo, MemberAddress.class);
        address.setUserId(userId);
        address.setIsDeleted(false);
        address.setCreateBy(String.valueOf(userId));
        address.setCreateTime(now);
        address.setUpdateBy(String.valueOf(userId));
        address.setUpdateTime(now);
        baseMapper.insert(address);
        return address.getId();
    }

    /**
     * 更新移动端会员收件地址。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAddress(Long userId, AppAddressUpdateReqBo updateReqBo) {
        validateUserAddress(userId, updateReqBo.getId());
        if (Boolean.TRUE.equals(updateReqBo.getDefaultStatus())) {
            cancelOtherDefaultAddress(userId, updateReqBo.getId());
        }
        MemberAddress address = BeanUtil.toBean(updateReqBo, MemberAddress.class);
        address.setUserId(userId);
        address.setUpdateBy(String.valueOf(userId));
        address.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(address) > 0;
    }

    /**
     * 删除移动端会员收件地址。
     */
    @Override
    public Boolean deleteAddress(Long userId, Long id) {
        validateUserAddress(userId, id);
        MemberAddress address = new MemberAddress();
        address.setId(id);
        address.setIsDeleted(true);
        address.setUpdateBy(String.valueOf(userId));
        address.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(address) > 0;
    }

    /**
     * 查询移动端会员收件地址。
     */
    @Override
    public AppAddressRespVo getAddress(Long userId, Long id) {
        MemberAddressVo address = selectByIdAndUserId(id, userId);
        return convertAppAddress(address);
    }

    /**
     * 查询移动端默认会员收件地址。
     */
    @Override
    public AppAddressRespVo getDefaultUserAddress(Long userId) {
        LambdaQueryWrapper<MemberAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberAddress::getIsDeleted, false);
        lqw.eq(MemberAddress::getUserId, userId);
        lqw.eq(MemberAddress::getDefaultStatus, true);
        lqw.orderByDesc(MemberAddress::getId);
        lqw.last("LIMIT 1");
        return convertAppAddress(baseMapper.selectVoOne(lqw));
    }

    /**
     * 查询移动端会员收件地址列表。
     */
    @Override
    public List<AppAddressRespVo> getAddressList(Long userId) {
        LambdaQueryWrapper<MemberAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberAddress::getIsDeleted, false);
        lqw.eq(MemberAddress::getUserId, userId);
        lqw.orderByDesc(MemberAddress::getDefaultStatus);
        lqw.orderByDesc(MemberAddress::getId);
        return baseMapper.selectVoList(lqw).stream().map(this::convertAppAddress).toList();
    }

    /**
     * 查询会员收件地址详情。
     */
    @Override
    public MemberAddressVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询会员收件地址。
     */
    @Override
    public PageVo<MemberAddressVo> queryPageList(MemberAddressBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberAddress> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberAddress::getId);
        Page<MemberAddressVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询会员收件地址列表。
     */
    @Override
    public List<MemberAddressVo> queryList(MemberAddressBo bo) {
        LambdaQueryWrapper<MemberAddress> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(MemberAddress::getId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改会员收件地址。
     */
    @Override
    public Boolean saveOrUpdate(MemberAddressBo bo) {
        MemberAddress address = BeanUtil.toBean(bo, MemberAddress.class);
        boolean isUpdate = address.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        address.setUpdateBy(userId);
        address.setUpdateTime(now);
        if (address.getIsDeleted() == null) {
            address.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(address) > 0;
        }
        address.setCreateBy(userId);
        address.setCreateTime(now);
        return baseMapper.insert(address) > 0;
    }

    /**
     * 校验并批量删除会员收件地址。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        for (Long id : ids) {
            MemberAddress address = new MemberAddress();
            address.setId(id);
            address.setIsDeleted(true);
            address.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            address.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(address);
        }
        return true;
    }

    /**
     * 构建会员收件地址查询条件。
     */
    private LambdaQueryWrapper<MemberAddress> buildQueryWrapper(MemberAddressBo bo) {
        LambdaQueryWrapper<MemberAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberAddress::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberAddress::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, MemberAddress::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), MemberAddress::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getMobile()), MemberAddress::getMobile, bo.getMobile());
        lqw.eq(bo.getAreaId() != null, MemberAddress::getAreaId, bo.getAreaId());
        lqw.like(StringUtils.isNotBlank(bo.getDetailAddress()), MemberAddress::getDetailAddress, bo.getDetailAddress());
        lqw.eq(bo.getDefaultStatus() != null, MemberAddress::getDefaultStatus, bo.getDefaultStatus());
        return lqw;
    }

    /**
     * 查询当前用户指定地址。
     */
    private MemberAddressVo selectByIdAndUserId(Long id, Long userId) {
        LambdaQueryWrapper<MemberAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberAddress::getIsDeleted, false);
        lqw.eq(MemberAddress::getId, id);
        lqw.eq(MemberAddress::getUserId, userId);
        lqw.last("LIMIT 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 校验当前用户是否可以操作地址。
     */
    private void validateUserAddress(Long userId, Long id) {
        if (selectByIdAndUserId(id, userId) == null) {
            throw new ServiceException("收件地址不存在");
        }
    }

    /**
     * 取消当前用户其它默认地址。
     */
    private void cancelOtherDefaultAddress(Long userId, Long excludeId) {
        MemberAddress update = new MemberAddress();
        update.setDefaultStatus(false);
        update.setUpdateBy(String.valueOf(userId));
        update.setUpdateTime(LocalDateTime.now());
        LambdaQueryWrapper<MemberAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberAddress::getIsDeleted, false);
        lqw.eq(MemberAddress::getUserId, userId);
        lqw.eq(MemberAddress::getDefaultStatus, true);
        lqw.ne(excludeId != null, MemberAddress::getId, excludeId);
        baseMapper.update(update, lqw);
    }

    /**
     * 转换移动端地址返回对象。
     */
    private AppAddressRespVo convertAppAddress(MemberAddressVo address) {
        if (address == null) {
            return null;
        }
        AppAddressRespVo respVo = new AppAddressRespVo();
        respVo.setId(address.getId());
        respVo.setName(address.getName());
        respVo.setMobile(address.getMobile());
        respVo.setAreaId(address.getAreaId());
        respVo.setDetailAddress(address.getDetailAddress());
        respVo.setDefaultStatus(address.getDefaultStatus());
        return respVo;
    }

}
