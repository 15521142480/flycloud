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
import com.fly.mall.api.product.domain.ProductProperty;
import com.fly.mall.api.product.domain.bo.ProductPropertyBo;
import com.fly.mall.api.product.domain.vo.ProductPropertyVo;
import com.fly.mall.product.mapper.ProductPropertyMapper;
import com.fly.mall.product.service.IProductPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 商品属性 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class ProductPropertyServiceImpl extends BaseServiceImpl<ProductPropertyMapper, ProductProperty> implements IProductPropertyService {

    private final ProductPropertyMapper baseMapper;

    /**
     * 查询商品属性详情。
     */
    @Override
    public ProductPropertyVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品属性。
     */
    @Override
    public PageVo<ProductPropertyVo> queryPageList(ProductPropertyBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductProperty> lqw = buildQueryWrapper(bo);
        Page<ProductPropertyVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询商品属性列表。
     */
    @Override
    public List<ProductPropertyVo> queryList(ProductPropertyBo bo) {
        LambdaQueryWrapper<ProductProperty> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改商品属性。
     */
    @Override
    public Boolean saveOrUpdate(ProductPropertyBo bo) {
        ProductProperty entity = BeanUtil.toBean(bo, ProductProperty.class);
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
     * 新增商品属性并返回编号。
     */
    @Override
    public Long createProperty(ProductPropertyBo bo) {
        ProductProperty entity = BeanUtil.toBean(bo, ProductProperty.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setIsDeleted(false);
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 校验并批量删除商品属性。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductProperty entity = new ProductProperty();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品属性查询条件。
     */
    private LambdaQueryWrapper<ProductProperty> buildQueryWrapper(ProductPropertyBo bo) {
        LambdaQueryWrapper<ProductProperty> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductProperty::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ProductProperty::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductProperty::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), ProductProperty::getRemark, bo.getRemark());
        return lqw;
    }

}
