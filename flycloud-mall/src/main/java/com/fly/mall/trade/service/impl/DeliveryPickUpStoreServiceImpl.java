package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.trade.domain.DeliveryPickUpStore;
import com.fly.mall.api.trade.domain.bo.DeliveryPickUpStoreBo;
import com.fly.mall.api.trade.domain.vo.DeliveryPickUpStoreVo;
import com.fly.mall.trade.mapper.DeliveryPickUpStoreMapper;
import com.fly.mall.trade.service.IDeliveryPickUpStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 自提门店 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DeliveryPickUpStoreServiceImpl extends BaseServiceImpl<DeliveryPickUpStoreMapper, DeliveryPickUpStore> implements IDeliveryPickUpStoreService {

    private final DeliveryPickUpStoreMapper baseMapper;

    /**
     * 查询自提门店详情。
     */
    @Override
    public DeliveryPickUpStoreVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询自提门店。
     */
    @Override
    public PageVo<DeliveryPickUpStoreVo> queryPageList(DeliveryPickUpStoreBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DeliveryPickUpStore> lqw = buildQueryWrapper(bo);
        Page<DeliveryPickUpStoreVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询自提门店列表。
     */
    @Override
    public List<DeliveryPickUpStoreVo> queryList(DeliveryPickUpStoreBo bo) {
        LambdaQueryWrapper<DeliveryPickUpStore> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改自提门店。
     */
    @Override
    public Boolean saveOrUpdate(DeliveryPickUpStoreBo bo) {
        DeliveryPickUpStore entity = BeanUtil.toBean(bo, DeliveryPickUpStore.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除自提门店。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DeliveryPickUpStore entity = new DeliveryPickUpStore();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建自提门店查询条件。
     */
    private LambdaQueryWrapper<DeliveryPickUpStore> buildQueryWrapper(DeliveryPickUpStoreBo bo) {
        LambdaQueryWrapper<DeliveryPickUpStore> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeliveryPickUpStore::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DeliveryPickUpStore::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeliveryPickUpStore::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getIntroduction()), DeliveryPickUpStore::getIntroduction, bo.getIntroduction());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), DeliveryPickUpStore::getPhone, bo.getPhone());
        lqw.eq(bo.getAreaId() != null, DeliveryPickUpStore::getAreaId, bo.getAreaId());
        lqw.like(StringUtils.isNotBlank(bo.getDetailAddress()), DeliveryPickUpStore::getDetailAddress, bo.getDetailAddress());
        lqw.like(StringUtils.isNotBlank(bo.getLogo()), DeliveryPickUpStore::getLogo, bo.getLogo());
        lqw.eq(bo.getOpeningTime() != null, DeliveryPickUpStore::getOpeningTime, bo.getOpeningTime());
        lqw.eq(bo.getClosingTime() != null, DeliveryPickUpStore::getClosingTime, bo.getClosingTime());
        lqw.eq(bo.getLatitude() != null, DeliveryPickUpStore::getLatitude, bo.getLatitude());
        lqw.eq(bo.getLongitude() != null, DeliveryPickUpStore::getLongitude, bo.getLongitude());
        lqw.eq(bo.getStatus() != null, DeliveryPickUpStore::getStatus, bo.getStatus());
        return lqw;
    }

}
