package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.file.FileUrlFieldConverter;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.enums.StatusEnum;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductCategory;
import com.fly.mall.api.product.domain.bo.ProductCategoryBo;
import com.fly.mall.api.product.domain.vo.ProductCategoryVo;
import com.fly.mall.product.mapper.ProductCategoryMapper;
import com.fly.mall.product.service.IProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
    private final FileUrlFieldConverter fileUrlFieldConverter;

    /**
     * 查询商品分类详情。
     */
    @Override
    public ProductCategoryVo queryById(Long id) {
        return fileUrlFieldConverter.buildUrl(baseMapper.selectVoById(id), "picUrl");
    }

    /**
     * 分页查询商品分类。
     */
    @Override
    public PageVo<ProductCategoryVo> queryPageList(ProductCategoryBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductCategory> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ProductCategory::getSort);
        Page<ProductCategoryVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return fileUrlFieldConverter.buildUrlPage(this.build(result), "picUrl");
    }

    /**
     * 查询商品分类列表。
     */
    @Override
    public List<ProductCategoryVo> queryList(ProductCategoryBo bo) {
        LambdaQueryWrapper<ProductCategory> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ProductCategory::getSort);
        return fileUrlFieldConverter.buildUrlList(baseMapper.selectVoList(lqw), "picUrl");
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
        return fileUrlFieldConverter.buildUrlList(baseMapper.selectVoList(lqw), "picUrl");
    }

    /**
     * 新增或修改商品分类。
     */
    @Override
    public Boolean saveOrUpdate(ProductCategoryBo bo) {
        fileUrlFieldConverter.toPath(bo, "picUrl");
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
        fileUrlFieldConverter.toPath(bo, "picUrl");
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
