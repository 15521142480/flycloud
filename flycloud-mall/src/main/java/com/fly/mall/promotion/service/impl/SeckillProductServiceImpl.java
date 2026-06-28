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
import com.fly.mall.api.domain.promotion.SeckillProduct;
import com.fly.mall.api.domain.promotion.bo.SeckillProductBo;
import com.fly.mall.api.domain.promotion.vo.SeckillProductVo;
import com.fly.mall.promotion.mapper.SeckillProductMapper;
import com.fly.mall.promotion.service.ISeckillProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 秒杀商品 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class SeckillProductServiceImpl extends BaseServiceImpl<SeckillProductMapper, SeckillProduct> implements ISeckillProductService {

    private final SeckillProductMapper baseMapper;

    /**
     * 查询秒杀商品详情。
     */
    @Override
    public SeckillProductVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询秒杀商品。
     */
    @Override
    public PageVo<SeckillProductVo> queryPageList(SeckillProductBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SeckillProduct> lqw = buildQueryWrapper(bo);
        Page<SeckillProductVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询秒杀商品列表。
     */
    @Override
    public List<SeckillProductVo> queryList(SeckillProductBo bo) {
        LambdaQueryWrapper<SeckillProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改秒杀商品。
     */
    @Override
    public Boolean saveOrUpdate(SeckillProductBo bo) {
        SeckillProduct entity = BeanUtil.toBean(bo, SeckillProduct.class);
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
     * 校验并批量删除秒杀商品。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            SeckillProduct entity = new SeckillProduct();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建秒杀商品查询条件。
     */
    private LambdaQueryWrapper<SeckillProduct> buildQueryWrapper(SeckillProductBo bo) {
        LambdaQueryWrapper<SeckillProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(SeckillProduct::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, SeckillProduct::getId, bo.getId());
        lqw.eq(bo.getActivityId() != null, SeckillProduct::getActivityId, bo.getActivityId());
        lqw.eq(bo.getSpuId() != null, SeckillProduct::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, SeckillProduct::getSkuId, bo.getSkuId());
        lqw.eq(bo.getSeckillPrice() != null, SeckillProduct::getSeckillPrice, bo.getSeckillPrice());
        lqw.eq(bo.getStock() != null, SeckillProduct::getStock, bo.getStock());
        lqw.eq(bo.getActivityStatus() != null, SeckillProduct::getActivityStatus, bo.getActivityStatus());
        lqw.eq(bo.getActivityStartTime() != null, SeckillProduct::getActivityStartTime, bo.getActivityStartTime());
        lqw.eq(bo.getActivityEndTime() != null, SeckillProduct::getActivityEndTime, bo.getActivityEndTime());
        return lqw;
    }

}
