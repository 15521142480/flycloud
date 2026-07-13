package com.fly.mall.product.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.enums.mall.ProductSpuStatusEnum;
import com.fly.mall.api.product.domain.ProductSpu;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;

/**
 * 商品 SPU Mapper 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ProductSpuMapper extends BaseMapperPlus<ProductSpuMapper, ProductSpu, ProductSpuVo> {

    /**
     * 为全部上架商品的销量加一。
     *
     * <p>使用数据库原子自增，避免任务重叠执行或商品成交时发生读改写竞争。</p>
     *
     * @return 更新的商品数量
     */
    default int incrementSalesCountForOnSaleProducts() {
        return update(null, new LambdaUpdateWrapper<ProductSpu>()
                .eq(ProductSpu::getStatus, ProductSpuStatusEnum.ENABLE.getStatus())
                .setSql("sales_count = COALESCE(sales_count, 0) + 1"));
    }

}
