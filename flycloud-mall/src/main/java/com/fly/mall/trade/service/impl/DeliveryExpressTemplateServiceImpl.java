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
import com.fly.mall.api.domain.trade.DeliveryExpressTemplate;
import com.fly.mall.api.domain.trade.bo.DeliveryExpressTemplateBo;
import com.fly.mall.api.domain.trade.vo.DeliveryExpressTemplateVo;
import com.fly.mall.trade.mapper.DeliveryExpressTemplateMapper;
import com.fly.mall.trade.service.IDeliveryExpressTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 运费模板 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class DeliveryExpressTemplateServiceImpl extends BaseServiceImpl<DeliveryExpressTemplateMapper, DeliveryExpressTemplate> implements IDeliveryExpressTemplateService {

    private final DeliveryExpressTemplateMapper baseMapper;

    /**
     * 查询运费模板详情。
     */
    @Override
    public DeliveryExpressTemplateVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询运费模板。
     */
    @Override
    public PageVo<DeliveryExpressTemplateVo> queryPageList(DeliveryExpressTemplateBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DeliveryExpressTemplate> lqw = buildQueryWrapper(bo);
        Page<DeliveryExpressTemplateVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询运费模板列表。
     */
    @Override
    public List<DeliveryExpressTemplateVo> queryList(DeliveryExpressTemplateBo bo) {
        LambdaQueryWrapper<DeliveryExpressTemplate> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改运费模板。
     */
    @Override
    public Boolean saveOrUpdate(DeliveryExpressTemplateBo bo) {
        DeliveryExpressTemplate entity = BeanUtil.toBean(bo, DeliveryExpressTemplate.class);
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
     * 校验并批量删除运费模板。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DeliveryExpressTemplate entity = new DeliveryExpressTemplate();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建运费模板查询条件。
     */
    private LambdaQueryWrapper<DeliveryExpressTemplate> buildQueryWrapper(DeliveryExpressTemplateBo bo) {
        LambdaQueryWrapper<DeliveryExpressTemplate> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeliveryExpressTemplate::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DeliveryExpressTemplate::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeliveryExpressTemplate::getName, bo.getName());
        lqw.eq(bo.getChargeMode() != null, DeliveryExpressTemplate::getChargeMode, bo.getChargeMode());
        lqw.eq(bo.getSort() != null, DeliveryExpressTemplate::getSort, bo.getSort());
        return lqw;
    }

}
