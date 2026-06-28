package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.domain.product.ProductPropertyValue;
import com.fly.mall.api.domain.product.bo.ProductPropertyValueBo;
import com.fly.mall.api.domain.product.vo.ProductPropertyValueVo;
import com.fly.mall.product.mapper.ProductPropertyValueMapper;
import com.fly.mall.product.service.IProductPropertyValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 商品属性值 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ProductPropertyValueServiceImpl extends BaseServiceImpl<ProductPropertyValueMapper, ProductPropertyValue> implements IProductPropertyValueService {

    private final ProductPropertyValueMapper baseMapper;

    /**
     * 查询商品属性值详情。
     */
    @Override
    public ProductPropertyValueVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品属性值。
     */
    @Override
    public PageVo<ProductPropertyValueVo> queryPageList(ProductPropertyValueBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductPropertyValue> lqw = buildQueryWrapper(bo);
        Page<ProductPropertyValueVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询商品属性值列表。
     */
    @Override
    public List<ProductPropertyValueVo> queryList(ProductPropertyValueBo bo) {
        LambdaQueryWrapper<ProductPropertyValue> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改商品属性值。
     */
    @Override
    public Boolean saveOrUpdate(ProductPropertyValueBo bo) {
        ProductPropertyValue entity = BeanUtil.toBean(bo, ProductPropertyValue.class);
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
     * 校验并批量删除商品属性值。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductPropertyValue entity = new ProductPropertyValue();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品属性值查询条件。
     */
    private LambdaQueryWrapper<ProductPropertyValue> buildQueryWrapper(ProductPropertyValueBo bo) {
        LambdaQueryWrapper<ProductPropertyValue> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductPropertyValue::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ProductPropertyValue::getId, bo.getId());
        lqw.eq(bo.getPropertyId() != null, ProductPropertyValue::getPropertyId, bo.getPropertyId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductPropertyValue::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), ProductPropertyValue::getRemark, bo.getRemark());
        return lqw;
    }

}
