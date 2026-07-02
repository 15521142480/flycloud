package com.fly.mall.statistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.file.FileUrlFieldConverter;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.statistics.domain.ProductStatistics;
import com.fly.mall.api.statistics.domain.bo.ProductStatisticsBo;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsRespVo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsVo;
import com.fly.mall.statistics.mapper.ProductStatisticsMapper;
import com.fly.mall.statistics.service.IProductStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 商品统计 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class ProductStatisticsServiceImpl extends BaseServiceImpl<ProductStatisticsMapper, ProductStatistics> implements IProductStatisticsService {

    private final ProductStatisticsMapper baseMapper;
    private final FileUrlFieldConverter fileUrlFieldConverter;

    /**
     * 查询商品统计详情。
     */
    @Override
    public ProductStatisticsVo queryById(Long id) {
        return fileUrlFieldConverter.buildUrl(baseMapper.selectVoById(id), "picUrl");
    }

    /**
     * 分页查询商品统计。
     */
    @Override
    public PageVo<ProductStatisticsVo> queryPageList(ProductStatisticsBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductStatistics> lqw = buildQueryWrapper(bo);
        Page<ProductStatisticsVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return fileUrlFieldConverter.buildUrlPage(this.build(result), "picUrl");
    }

    /**
     * 查询商品统计列表。
     */
    @Override
    public List<ProductStatisticsVo> queryList(ProductStatisticsBo bo) {
        LambdaQueryWrapper<ProductStatistics> lqw = buildQueryWrapper(bo);
        return fileUrlFieldConverter.buildUrlList(baseMapper.selectVoList(lqw), "picUrl");
    }

    /**
     * 查询商品统计分析数据。
     */
    @Override
    public DataComparisonRespVo<ProductStatisticsRespVo> getProductStatisticsAnalyse(StatisticsTimeRangeBo bo) {
        List<ProductStatisticsRespVo> valueList = getProductStatisticsList(bo);
        List<ProductStatisticsRespVo> referenceList = getProductStatisticsList(previousRange(bo));
        return new DataComparisonRespVo<>(summaryProductStatistics(valueList), summaryProductStatistics(referenceList));
    }

    /**
     * 查询商品统计明细。
     */
    @Override
    public List<ProductStatisticsRespVo> getProductStatisticsList(StatisticsTimeRangeBo bo) {
        return fileUrlFieldConverter.buildUrlList(baseMapper.selectVoList(buildRangeWrapper(bo)).stream()
                .map(this::convertRespVo)
                .toList(), "picUrl");
    }

    /**
     * 分页查询商品统计排行榜。
     */
    @Override
    public PageVo<ProductStatisticsRespVo> getProductStatisticsRankPage(StatisticsTimeRangeBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductStatistics> lqw = buildRangeWrapper(bo);
        lqw.orderByDesc(ProductStatistics::getOrderPayPrice)
                .orderByDesc(ProductStatistics::getOrderPayCount)
                .orderByDesc(ProductStatistics::getBrowseCount);
        int pageNum = pageBo == null || pageBo.getPageNum() == null ? 1 : pageBo.getPageNum();
        int pageSize = pageBo == null || pageBo.getPageSize() == null ? 10 : pageBo.getPageSize();
        Page<ProductStatisticsVo> page = baseMapper.selectVoPage(new Page<>(pageNum, pageSize), lqw);
        PageVo<ProductStatisticsRespVo> result = new PageVo<>();
        result.setPages(page.getPages());
        result.setTotal(page.getTotal());
        result.setList(fileUrlFieldConverter.buildUrlList(page.getRecords().stream().map(this::convertRespVo).toList(), "picUrl"));
        return result;
    }

    /**
     * 新增或修改商品统计。
     */
    @Override
    public Boolean saveOrUpdate(ProductStatisticsBo bo) {
        fileUrlFieldConverter.toPath(bo, "picUrl");
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

    /**
     * 构建商品统计时间范围查询条件。
     */
    private LambdaQueryWrapper<ProductStatistics> buildRangeWrapper(StatisticsTimeRangeBo bo) {
        LambdaQueryWrapper<ProductStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductStatistics::getIsDeleted, false);
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        if (times != null && times.length > 0 && times[0] != null) {
            lqw.ge(ProductStatistics::getTime, times[0].toLocalDate());
        }
        if (times != null && times.length > 1 && times[1] != null) {
            lqw.le(ProductStatistics::getTime, times[1].toLocalDate());
        }
        lqw.orderByAsc(ProductStatistics::getTime);
        return lqw;
    }

    /**
     * 生成对照时间范围。
     */
    private StatisticsTimeRangeBo previousRange(StatisticsTimeRangeBo bo) {
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        if (times == null || times.length < 2 || times[0] == null || times[1] == null) {
            return null;
        }
        long seconds = Math.max(1, Duration.between(times[0], times[1]).getSeconds());
        StatisticsTimeRangeBo previous = new StatisticsTimeRangeBo();
        previous.setTimes(new LocalDateTime[]{times[0].minusSeconds(seconds), times[0].minusSeconds(1)});
        return previous;
    }

    /**
     * 转换商品统计响应对象。
     */
    private ProductStatisticsRespVo convertRespVo(ProductStatisticsVo vo) {
        ProductStatisticsRespVo respVo = BeanUtil.toBean(vo, ProductStatisticsRespVo.class);
        if (respVo.getTime() == null) {
            respVo.setTime(LocalDate.now());
        }
        return respVo;
    }

    /**
     * 汇总商品统计数据。
     */
    private ProductStatisticsRespVo summaryProductStatistics(List<ProductStatisticsRespVo> list) {
        ProductStatisticsRespVo respVo = new ProductStatisticsRespVo();
        respVo.setTime(LocalDate.now());
        respVo.setBrowseCount(sum(list, ProductStatisticsRespVo::getBrowseCount));
        respVo.setBrowseUserCount(sum(list, ProductStatisticsRespVo::getBrowseUserCount));
        respVo.setFavoriteCount(sum(list, ProductStatisticsRespVo::getFavoriteCount));
        respVo.setCartCount(sum(list, ProductStatisticsRespVo::getCartCount));
        respVo.setOrderCount(sum(list, ProductStatisticsRespVo::getOrderCount));
        respVo.setOrderPayCount(sum(list, ProductStatisticsRespVo::getOrderPayCount));
        respVo.setOrderPayPrice(sum(list, ProductStatisticsRespVo::getOrderPayPrice));
        respVo.setAfterSaleCount(sum(list, ProductStatisticsRespVo::getAfterSaleCount));
        respVo.setAfterSaleRefundPrice(sum(list, ProductStatisticsRespVo::getAfterSaleRefundPrice));
        respVo.setBrowseConvertPercent(respVo.getBrowseUserCount() == 0 ? 0
                : respVo.getOrderPayCount() * 100 / respVo.getBrowseUserCount());
        return respVo;
    }

    /**
     * 求和统计字段。
     */
    private Integer sum(List<ProductStatisticsRespVo> list, java.util.function.Function<ProductStatisticsRespVo, Integer> getter) {
        return list.stream().map(getter).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
    }

}
