package com.fly.mall.product.job;

import com.fly.mall.product.service.IProductSpuService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 商品虚拟销量更新 Job。
 *
 * <p>由 XXL-JOB 每日调度一次，为全部上架商品的 {@code sales_count} 加一。</p>
 *
 * @author lxs
 * @date 2026-07-14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSalesCountJob {

    private final IProductSpuService productSpuService;

    /**
     * 更新上架商品的虚拟销量。
     *
     * @param param XXL-JOB 任务参数，当前任务不使用
     */
    @XxlJob("updateProductSalesCountJob")
    public void execute(String param) {
        int updatedCount = productSpuService.incrementSalesCountForOnSaleProducts();
        log.info("[execute][上架商品虚拟销量更新完成，商品数量：{}]", updatedCount);
        XxlJobHelper.handleSuccess("上架商品虚拟销量更新完成，商品数量：" + updatedCount);
    }

}
