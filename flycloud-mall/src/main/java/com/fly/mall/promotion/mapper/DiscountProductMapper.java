package com.fly.mall.promotion.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.mall.api.promotion.domain.DiscountProduct;
import com.fly.mall.api.promotion.domain.vo.DiscountProductVo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 限时折扣商品 Mapper 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface DiscountProductMapper extends BaseMapperPlus<DiscountProductMapper, DiscountProduct, DiscountProductVo> {

    /**
     * 查询活动下的限时折扣商品。
     */
    default List<DiscountProduct> selectListByActivityId(Long activityId) {
        return selectList(new LambdaQueryWrapper<DiscountProduct>()
                .eq(DiscountProduct::getIsDeleted, false)
                .eq(DiscountProduct::getActivityId, activityId));
    }

    /**
     * 查询多个活动下的限时折扣商品。
     */
    default List<DiscountProduct> selectListByActivityId(Collection<Long> activityIds) {
        if (activityIds == null || activityIds.isEmpty()) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapper<DiscountProduct>()
                .eq(DiscountProduct::getIsDeleted, false)
                .in(DiscountProduct::getActivityId, activityIds));
    }

    /**
     * 按活动编号更新活动商品冗余状态。
     */
    default void updateByActivityId(DiscountProduct discountProduct) {
        update(discountProduct, new LambdaUpdateWrapper<DiscountProduct>()
                .eq(DiscountProduct::getActivityId, discountProduct.getActivityId()));
    }

    /**
     * 删除活动下的商品。
     */
    default void deleteByActivityId(Long activityId) {
        delete(new LambdaQueryWrapper<DiscountProduct>()
                .eq(DiscountProduct::getActivityId, activityId));
    }

    /**
     * 查询当前生效的 SKU 限时折扣商品。
     */
    default List<DiscountProduct> selectListBySkuIdsAndStatusAndNow(Collection<Long> skuIds, Integer status) {
        if (skuIds == null || skuIds.isEmpty()) {
            return Collections.emptyList();
        }
        LocalDateTime now = LocalDateTime.now();
        return selectList(new LambdaQueryWrapper<DiscountProduct>()
                .eq(DiscountProduct::getIsDeleted, false)
                .in(DiscountProduct::getSkuId, skuIds)
                .eq(DiscountProduct::getActivityStatus, status)
                .lt(DiscountProduct::getActivityStartTime, now)
                .gt(DiscountProduct::getActivityEndTime, now));
    }

}
