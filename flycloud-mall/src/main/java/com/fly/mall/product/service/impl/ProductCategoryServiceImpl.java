package com.fly.mall.product.service.impl;

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
import com.fly.common.enums.StatusEnum;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductCategory;
import com.fly.mall.api.product.domain.bo.ProductCategoryBo;
import com.fly.mall.api.product.domain.vo.ProductCategoryVo;
import com.fly.mall.product.mapper.ProductCategoryMapper;
import com.fly.mall.product.service.IProductCategoryService;
import com.fly.mall.product.service.IProductSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 商品分类 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory>
        implements IProductCategoryService {

    private final ProductCategoryMapper baseMapper;
    private final IProductSpuService productSpuService;

    /**
     * 查询商品分类详情。
     */
    @Override
    public ProductCategoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品分类。
     */
    @Override
    public PageVo<ProductCategoryVo> queryPageList(ProductCategoryBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductCategory> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ProductCategory::getSort);
        Page<ProductCategoryVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询商品分类列表。
     */
    @Override
    public List<ProductCategoryVo> queryList(ProductCategoryBo bo) {
        LambdaQueryWrapper<ProductCategory> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ProductCategory::getSort);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询启用的商品分类列表。
     */
    @Override
    public List<ProductCategoryVo> queryEnableList(Collection<Long> ids) {
        LambdaQueryWrapper<ProductCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductCategory::getIsDeleted, false);
        lqw.eq(ProductCategory::getStatus, StatusEnum.ENABLE.getStatus());
        lqw.in(!CollectionUtils.isEmpty(ids), ProductCategory::getId, ids);
        lqw.orderByAsc(ProductCategory::getSort);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改商品分类。
     */
    @Override
    public Boolean saveOrUpdate(ProductCategoryBo bo) {
        validateParentCategory(bo.getId(), bo.getParentId());
        ProductCategory entity = BeanUtil.toBean(bo, ProductCategory.class);
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
     * 新增商品分类并返回编号。
     */
    @Override
    public Long createCategory(ProductCategoryBo bo) {
        validateParentCategory(null, bo.getParentId());
        ProductCategory entity = BeanUtil.toBean(bo, ProductCategory.class);
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
     * 校验并批量删除商品分类。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            validateCategoryCanDelete(id);
            ProductCategory entity = new ProductCategory();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 校验父级商品分类可用。
     */
    private void validateParentCategory(Long id, Long parentId) {
        if (parentId == null || Objects.equals(parentId, ProductCategory.PARENT_ID_ROOT)) {
            return;
        }
        if (Objects.equals(id, parentId)) {
            throw new ServiceException("父级商品分类不能选择自己");
        }
        ProductCategory parent = baseMapper.selectById(parentId);
        if (parent == null || Boolean.TRUE.equals(parent.getIsDeleted())) {
            throw new ServiceException("父级商品分类不存在");
        }
        if (!Objects.equals(parent.getParentId(), ProductCategory.PARENT_ID_ROOT)) {
            throw new ServiceException("父级商品分类必须是一级分类");
        }
    }

    /**
     * 校验商品分类是否允许删除。
     */
    private void validateCategoryCanDelete(Long id) {
        ProductCategory category = baseMapper.selectById(id);
        if (category == null || Boolean.TRUE.equals(category.getIsDeleted())) {
            throw new ServiceException("商品分类不存在");
        }
        LambdaQueryWrapper<ProductCategory> childrenWrapper = Wrappers.lambdaQuery();
        childrenWrapper.eq(ProductCategory::getIsDeleted, false);
        childrenWrapper.eq(ProductCategory::getParentId, id);
        if (baseMapper.selectCount(childrenWrapper) > 0) {
            throw new ServiceException("商品分类存在子分类，不能删除");
        }
        if (productSpuService.getSpuCountByCategoryId(id) > 0) {
            throw new ServiceException("商品分类已绑定商品，不能删除");
        }
    }

    /**
     * 构建商品分类查询条件。
     */
    private LambdaQueryWrapper<ProductCategory> buildQueryWrapper(ProductCategoryBo bo) {
        LambdaQueryWrapper<ProductCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductCategory::getIsDeleted, false);
        lqw.eq(bo.getId() != null, ProductCategory::getId, bo.getId());
        lqw.eq(bo.getParentId() != null, ProductCategory::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductCategory::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, ProductCategory::getStatus, bo.getStatus());
        return lqw;
    }

}
