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
import com.fly.mall.api.product.domain.ProductBrand;
import com.fly.mall.api.product.domain.bo.ProductBrandBo;
import com.fly.mall.api.product.domain.vo.ProductBrandVo;
import com.fly.mall.product.mapper.ProductBrandMapper;
import com.fly.mall.product.service.IProductBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 商品品牌 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ProductBrandServiceImpl extends BaseServiceImpl<ProductBrandMapper, ProductBrand>
        implements IProductBrandService {

    private final ProductBrandMapper baseMapper;

    /**
     * 查询商品品牌详情。
     */
    @Override
    public ProductBrandVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品品牌。
     */
    @Override
    public PageVo<ProductBrandVo> queryPageList(ProductBrandBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductBrand> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ProductBrand::getSort);
        Page<ProductBrandVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询商品品牌列表。
     */
    @Override
    public List<ProductBrandVo> queryList(ProductBrandBo bo) {
        LambdaQueryWrapper<ProductBrand> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ProductBrand::getSort);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改商品品牌。
     */
    @Override
    public Boolean saveOrUpdate(ProductBrandBo bo) {
        ProductBrand entity = BeanUtil.toBean(bo, ProductBrand.class);
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
     * 新增商品品牌并返回编号。
     */
    @Override
    public Long createBrand(ProductBrandBo bo) {
        ProductBrand entity = BeanUtil.toBean(bo, ProductBrand.class);
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
     * 校验并批量删除商品品牌。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductBrand entity = new ProductBrand();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品品牌查询条件。
     */
    private LambdaQueryWrapper<ProductBrand> buildQueryWrapper(ProductBrandBo bo) {
        LambdaQueryWrapper<ProductBrand> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductBrand::getIsDeleted, false);
        lqw.eq(bo.getId() != null, ProductBrand::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductBrand::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, ProductBrand::getStatus, bo.getStatus());
        return lqw;
    }

}
