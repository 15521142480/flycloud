package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.promotion.domain.PointProduct;
import com.fly.mall.api.promotion.domain.bo.PointProductBo;
import com.fly.mall.api.promotion.domain.vo.PointProductVo;
import com.fly.mall.promotion.mapper.PointProductMapper;
import com.fly.mall.promotion.service.IPointProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 积分商城商品 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PointProductServiceImpl extends BaseServiceImpl<PointProductMapper, PointProduct> implements IPointProductService {

    private final PointProductMapper baseMapper;

    /**
     * 查询积分商城商品详情。
     */
    @Override
    public PointProductVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询积分商城商品。
     */
    @Override
    public PageVo<PointProductVo> queryPageList(PointProductBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PointProduct> lqw = buildQueryWrapper(bo);
        Page<PointProductVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询积分商城商品列表。
     */
    @Override
    public List<PointProductVo> queryList(PointProductBo bo) {
        LambdaQueryWrapper<PointProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改积分商城商品。
     */
    @Override
    public Boolean saveOrUpdate(PointProductBo bo) {
        PointProduct entity = BeanUtil.toBean(bo, PointProduct.class);
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
     * 校验并批量删除积分商城商品。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建积分商城商品查询条件。
     */
    private LambdaQueryWrapper<PointProduct> buildQueryWrapper(PointProductBo bo) {
        LambdaQueryWrapper<PointProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(PointProduct::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PointProduct::getId, bo.getId());
        lqw.eq(bo.getActivityId() != null, PointProduct::getActivityId, bo.getActivityId());
        lqw.eq(bo.getSpuId() != null, PointProduct::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, PointProduct::getSkuId, bo.getSkuId());
        lqw.eq(bo.getCount() != null, PointProduct::getCount, bo.getCount());
        lqw.eq(bo.getPoint() != null, PointProduct::getPoint, bo.getPoint());
        lqw.eq(bo.getPrice() != null, PointProduct::getPrice, bo.getPrice());
        lqw.eq(bo.getStock() != null, PointProduct::getStock, bo.getStock());
        lqw.eq(bo.getActivityStatus() != null, PointProduct::getActivityStatus, bo.getActivityStatus());
        return lqw;
    }

}
