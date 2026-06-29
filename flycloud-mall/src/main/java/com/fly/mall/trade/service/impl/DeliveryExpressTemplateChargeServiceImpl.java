package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.DeliveryExpressTemplateCharge;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateChargeBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateChargeVo;
import com.fly.mall.trade.mapper.DeliveryExpressTemplateChargeMapper;
import com.fly.mall.trade.service.IDeliveryExpressTemplateChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 运费模板计费规则 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DeliveryExpressTemplateChargeServiceImpl extends BaseServiceImpl<DeliveryExpressTemplateChargeMapper, DeliveryExpressTemplateCharge> implements IDeliveryExpressTemplateChargeService {

    private final DeliveryExpressTemplateChargeMapper baseMapper;

    /**
     * 查询运费模板计费规则详情。
     */
    @Override
    public DeliveryExpressTemplateChargeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询运费模板计费规则。
     */
    @Override
    public PageVo<DeliveryExpressTemplateChargeVo> queryPageList(DeliveryExpressTemplateChargeBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DeliveryExpressTemplateCharge> lqw = buildQueryWrapper(bo);
        Page<DeliveryExpressTemplateChargeVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询运费模板计费规则列表。
     */
    @Override
    public List<DeliveryExpressTemplateChargeVo> queryList(DeliveryExpressTemplateChargeBo bo) {
        LambdaQueryWrapper<DeliveryExpressTemplateCharge> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改运费模板计费规则。
     */
    @Override
    public Boolean saveOrUpdate(DeliveryExpressTemplateChargeBo bo) {
        DeliveryExpressTemplateCharge entity = BeanUtil.toBean(bo, DeliveryExpressTemplateCharge.class);
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
     * 校验并批量删除运费模板计费规则。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DeliveryExpressTemplateCharge entity = new DeliveryExpressTemplateCharge();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建运费模板计费规则查询条件。
     */
    private LambdaQueryWrapper<DeliveryExpressTemplateCharge> buildQueryWrapper(DeliveryExpressTemplateChargeBo bo) {
        LambdaQueryWrapper<DeliveryExpressTemplateCharge> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeliveryExpressTemplateCharge::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DeliveryExpressTemplateCharge::getId, bo.getId());
        lqw.eq(bo.getTemplateId() != null, DeliveryExpressTemplateCharge::getTemplateId, bo.getTemplateId());
        lqw.eq(bo.getChargeMode() != null, DeliveryExpressTemplateCharge::getChargeMode, bo.getChargeMode());
        lqw.eq(bo.getStartCount() != null, DeliveryExpressTemplateCharge::getStartCount, bo.getStartCount());
        lqw.eq(bo.getStartPrice() != null, DeliveryExpressTemplateCharge::getStartPrice, bo.getStartPrice());
        lqw.eq(bo.getExtraCount() != null, DeliveryExpressTemplateCharge::getExtraCount, bo.getExtraCount());
        lqw.eq(bo.getExtraPrice() != null, DeliveryExpressTemplateCharge::getExtraPrice, bo.getExtraPrice());
        return lqw;
    }

}
