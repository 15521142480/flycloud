package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.DeliveryExpressTemplateFree;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateFreeBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateFreeVo;
import com.fly.mall.trade.mapper.DeliveryExpressTemplateFreeMapper;
import com.fly.mall.trade.service.IDeliveryExpressTemplateFreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 运费模板包邮规则 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DeliveryExpressTemplateFreeServiceImpl extends BaseServiceImpl<DeliveryExpressTemplateFreeMapper, DeliveryExpressTemplateFree> implements IDeliveryExpressTemplateFreeService {

    private final DeliveryExpressTemplateFreeMapper baseMapper;

    /**
     * 查询运费模板包邮规则详情。
     */
    @Override
    public DeliveryExpressTemplateFreeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询运费模板包邮规则。
     */
    @Override
    public PageVo<DeliveryExpressTemplateFreeVo> queryPageList(DeliveryExpressTemplateFreeBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DeliveryExpressTemplateFree> lqw = buildQueryWrapper(bo);
        Page<DeliveryExpressTemplateFreeVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询运费模板包邮规则列表。
     */
    @Override
    public List<DeliveryExpressTemplateFreeVo> queryList(DeliveryExpressTemplateFreeBo bo) {
        LambdaQueryWrapper<DeliveryExpressTemplateFree> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改运费模板包邮规则。
     */
    @Override
    public Boolean saveOrUpdate(DeliveryExpressTemplateFreeBo bo) {
        DeliveryExpressTemplateFree entity = BeanUtil.toBean(bo, DeliveryExpressTemplateFree.class);
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
     * 校验并批量删除运费模板包邮规则。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DeliveryExpressTemplateFree entity = new DeliveryExpressTemplateFree();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建运费模板包邮规则查询条件。
     */
    private LambdaQueryWrapper<DeliveryExpressTemplateFree> buildQueryWrapper(DeliveryExpressTemplateFreeBo bo) {
        LambdaQueryWrapper<DeliveryExpressTemplateFree> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeliveryExpressTemplateFree::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DeliveryExpressTemplateFree::getId, bo.getId());
        lqw.eq(bo.getTemplateId() != null, DeliveryExpressTemplateFree::getTemplateId, bo.getTemplateId());
        lqw.eq(bo.getFreePrice() != null, DeliveryExpressTemplateFree::getFreePrice, bo.getFreePrice());
        lqw.eq(bo.getFreeCount() != null, DeliveryExpressTemplateFree::getFreeCount, bo.getFreeCount());
        return lqw;
    }

}
