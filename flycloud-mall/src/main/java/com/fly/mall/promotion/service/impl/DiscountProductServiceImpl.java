package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.DiscountProduct;
import com.fly.mall.api.promotion.domain.bo.DiscountProductBo;
import com.fly.mall.api.promotion.domain.vo.DiscountProductVo;
import com.fly.mall.promotion.mapper.DiscountProductMapper;
import com.fly.mall.promotion.service.IDiscountProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 限时折扣商品 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class DiscountProductServiceImpl extends BaseServiceImpl<DiscountProductMapper, DiscountProduct> implements IDiscountProductService {

    private final DiscountProductMapper baseMapper;

    /**
     * 查询限时折扣商品详情。
     */
    @Override
    public DiscountProductVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询限时折扣商品。
     */
    @Override
    public PageVo<DiscountProductVo> queryPageList(DiscountProductBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiscountProduct> lqw = buildQueryWrapper(bo);
        Page<DiscountProductVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询限时折扣商品列表。
     */
    @Override
    public List<DiscountProductVo> queryList(DiscountProductBo bo) {
        LambdaQueryWrapper<DiscountProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改限时折扣商品。
     */
    @Override
    public Boolean saveOrUpdate(DiscountProductBo bo) {
        DiscountProduct entity = BeanUtil.toBean(bo, DiscountProduct.class);
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
     * 校验并批量删除限时折扣商品。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            DiscountProduct entity = new DiscountProduct();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建限时折扣商品查询条件。
     */
    private LambdaQueryWrapper<DiscountProduct> buildQueryWrapper(DiscountProductBo bo) {
        LambdaQueryWrapper<DiscountProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiscountProduct::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DiscountProduct::getId, bo.getId());
        lqw.eq(bo.getActivityId() != null, DiscountProduct::getActivityId, bo.getActivityId());
        lqw.eq(bo.getSpuId() != null, DiscountProduct::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, DiscountProduct::getSkuId, bo.getSkuId());
        lqw.eq(bo.getDiscountType() != null, DiscountProduct::getDiscountType, bo.getDiscountType());
        lqw.eq(bo.getDiscountPercent() != null, DiscountProduct::getDiscountPercent, bo.getDiscountPercent());
        lqw.eq(bo.getDiscountPrice() != null, DiscountProduct::getDiscountPrice, bo.getDiscountPrice());
        lqw.like(StringUtils.isNotBlank(bo.getActivityName()), DiscountProduct::getActivityName, bo.getActivityName());
        lqw.eq(bo.getActivityStatus() != null, DiscountProduct::getActivityStatus, bo.getActivityStatus());
        lqw.eq(bo.getActivityStartTime() != null, DiscountProduct::getActivityStartTime, bo.getActivityStartTime());
        lqw.eq(bo.getActivityEndTime() != null, DiscountProduct::getActivityEndTime, bo.getActivityEndTime());
        return lqw;
    }

}
