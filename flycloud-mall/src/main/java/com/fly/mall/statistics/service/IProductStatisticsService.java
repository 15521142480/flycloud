package com.fly.mall.statistics.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.statistics.domain.bo.ProductStatisticsBo;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsRespVo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品统计 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IProductStatisticsService {

    /**
     * 查询商品统计详情。
     */
    ProductStatisticsVo queryById(Long id);

    /**
     * 分页查询商品统计。
     */
    PageVo<ProductStatisticsVo> queryPageList(ProductStatisticsBo bo, PageBo pageBo);

    /**
     * 查询商品统计列表。
     */
    List<ProductStatisticsVo> queryList(ProductStatisticsBo bo);

    /**
     * 查询商品统计分析数据。
     */
    DataComparisonRespVo<ProductStatisticsRespVo> getProductStatisticsAnalyse(StatisticsTimeRangeBo bo);

    /**
     * 查询商品统计明细。
     */
    List<ProductStatisticsRespVo> getProductStatisticsList(StatisticsTimeRangeBo bo);

    /**
     * 分页查询商品统计排行榜。
     */
    PageVo<ProductStatisticsRespVo> getProductStatisticsRankPage(StatisticsTimeRangeBo bo, PageBo pageBo);

    /**
     * 新增或修改商品统计。
     */
    Boolean saveOrUpdate(ProductStatisticsBo bo);

    /**
     * 校验并批量删除商品统计。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
