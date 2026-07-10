package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.promotion.domain.CombinationProduct;
import com.fly.mall.api.promotion.domain.bo.CombinationProductBo;
import com.fly.mall.api.promotion.domain.vo.CombinationProductVo;
import com.fly.mall.promotion.mapper.CombinationProductMapper;
import com.fly.mall.promotion.service.ICombinationProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 拼团商品 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class CombinationProductServiceImpl extends BaseServiceImpl<CombinationProductMapper, CombinationProduct> implements ICombinationProductService {

    private final CombinationProductMapper baseMapper;

    /**
     * 查询拼团商品详情。
     */
    @Override
    public CombinationProductVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询拼团商品。
     */
    @Override
    public PageVo<CombinationProductVo> queryPageList(CombinationProductBo bo, PageBo pageBo) {
        LambdaQueryWrapper<CombinationProduct> lqw = buildQueryWrapper(bo);
        Page<CombinationProductVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询拼团商品列表。
     */
    @Override
    public List<CombinationProductVo> queryList(CombinationProductBo bo) {
        LambdaQueryWrapper<CombinationProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改拼团商品。
     */
    @Override
    public Boolean saveOrUpdate(CombinationProductBo bo) {
        CombinationProduct entity = BeanUtil.toBean(bo, CombinationProduct.class);
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
     * 校验并批量删除拼团商品。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建拼团商品查询条件。
     */
    private LambdaQueryWrapper<CombinationProduct> buildQueryWrapper(CombinationProductBo bo) {
        LambdaQueryWrapper<CombinationProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(CombinationProduct::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, CombinationProduct::getId, bo.getId());
        lqw.eq(bo.getActivityId() != null, CombinationProduct::getActivityId, bo.getActivityId());
        lqw.eq(bo.getSpuId() != null, CombinationProduct::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, CombinationProduct::getSkuId, bo.getSkuId());
        lqw.eq(bo.getCombinationPrice() != null, CombinationProduct::getCombinationPrice, bo.getCombinationPrice());
        lqw.eq(bo.getActivityStatus() != null, CombinationProduct::getActivityStatus, bo.getActivityStatus());
        lqw.eq(bo.getActivityStartTime() != null, CombinationProduct::getActivityStartTime, bo.getActivityStartTime());
        lqw.eq(bo.getActivityEndTime() != null, CombinationProduct::getActivityEndTime, bo.getActivityEndTime());
        return lqw;
    }

}
