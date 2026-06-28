package com.fly.mall.statistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.domain.statistics.ProductStatistics;
import com.fly.mall.api.domain.statistics.bo.ProductStatisticsBo;
import com.fly.mall.api.domain.statistics.vo.ProductStatisticsVo;
import com.fly.mall.statistics.mapper.ProductStatisticsMapper;
import com.fly.mall.statistics.service.IProductStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 商品统计 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ProductStatisticsServiceImpl extends BaseServiceImpl<ProductStatisticsMapper, ProductStatistics> implements IProductStatisticsService {

    private final ProductStatisticsMapper baseMapper;

    /**
     * 查询商品统计详情。
     */
    @Override
    public ProductStatisticsVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品统计。
     */
    @Override
    public PageVo<ProductStatisticsVo> queryPageList(ProductStatisticsBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductStatistics> lqw = buildQueryWrapper(bo);
        Page<ProductStatisticsVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询商品统计列表。
     */
    @Override
    public List<ProductStatisticsVo> queryList(ProductStatisticsBo bo) {
        LambdaQueryWrapper<ProductStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改商品统计。
     */
    @Override
    public Boolean saveOrUpdate(ProductStatisticsBo bo) {
        ProductStatistics entity = BeanUtil.toBean(bo, ProductStatistics.class);
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
     * 校验并批量删除商品统计。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductStatistics entity = new ProductStatistics();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品统计查询条件。
     */
    private LambdaQueryWrapper<ProductStatistics> buildQueryWrapper(ProductStatisticsBo bo) {
        LambdaQueryWrapper<ProductStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductStatistics::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ProductStatistics::getId, bo.getId());
        lqw.eq(bo.getTime() != null, ProductStatistics::getTime, bo.getTime());
        lqw.eq(bo.getSpuId() != null, ProductStatistics::getSpuId, bo.getSpuId());
        lqw.eq(bo.getBrowseCount() != null, ProductStatistics::getBrowseCount, bo.getBrowseCount());
        lqw.eq(bo.getBrowseUserCount() != null, ProductStatistics::getBrowseUserCount, bo.getBrowseUserCount());
        lqw.eq(bo.getFavoriteCount() != null, ProductStatistics::getFavoriteCount, bo.getFavoriteCount());
        lqw.eq(bo.getCartCount() != null, ProductStatistics::getCartCount, bo.getCartCount());
        lqw.eq(bo.getOrderCount() != null, ProductStatistics::getOrderCount, bo.getOrderCount());
        lqw.eq(bo.getOrderPayCount() != null, ProductStatistics::getOrderPayCount, bo.getOrderPayCount());
        lqw.eq(bo.getOrderPayPrice() != null, ProductStatistics::getOrderPayPrice, bo.getOrderPayPrice());
        lqw.eq(bo.getAfterSaleCount() != null, ProductStatistics::getAfterSaleCount, bo.getAfterSaleCount());
        lqw.eq(bo.getAfterSaleRefundPrice() != null, ProductStatistics::getAfterSaleRefundPrice, bo.getAfterSaleRefundPrice());
        lqw.eq(bo.getBrowseConvertPercent() != null, ProductStatistics::getBrowseConvertPercent, bo.getBrowseConvertPercent());
        return lqw;
    }

}
