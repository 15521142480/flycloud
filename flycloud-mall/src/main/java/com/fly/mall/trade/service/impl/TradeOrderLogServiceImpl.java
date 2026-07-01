package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.trade.domain.TradeOrderLog;
import com.fly.mall.api.trade.domain.bo.TradeOrderLogBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderLogVo;
import com.fly.mall.trade.mapper.TradeOrderLogMapper;
import com.fly.mall.trade.service.ITradeOrderLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 交易订单日志 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class TradeOrderLogServiceImpl extends BaseServiceImpl<TradeOrderLogMapper, TradeOrderLog> implements ITradeOrderLogService {

    private final TradeOrderLogMapper baseMapper;

    /**
     * 查询交易订单日志详情。
     */
    @Override
    public TradeOrderLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询交易订单日志。
     */
    @Override
    public PageVo<TradeOrderLogVo> queryPageList(TradeOrderLogBo bo, PageBo pageBo) {
        LambdaQueryWrapper<TradeOrderLog> lqw = buildQueryWrapper(bo);
        Page<TradeOrderLogVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询交易订单日志列表。
     */
    @Override
    public List<TradeOrderLogVo> queryList(TradeOrderLogBo bo) {
        LambdaQueryWrapper<TradeOrderLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改交易订单日志。
     */
    @Override
    public Boolean saveOrUpdate(TradeOrderLogBo bo) {
        TradeOrderLog entity = BeanUtil.toBean(bo, TradeOrderLog.class);
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
     * 校验并批量删除交易订单日志。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            TradeOrderLog entity = new TradeOrderLog();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建交易订单日志查询条件。
     */
    private LambdaQueryWrapper<TradeOrderLog> buildQueryWrapper(TradeOrderLogBo bo) {
        LambdaQueryWrapper<TradeOrderLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeOrderLog::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, TradeOrderLog::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, TradeOrderLog::getUserId, bo.getUserId());
        lqw.eq(bo.getUserType() != null, TradeOrderLog::getUserType, bo.getUserType());
        lqw.eq(bo.getOrderId() != null, TradeOrderLog::getOrderId, bo.getOrderId());
        lqw.eq(bo.getBeforeStatus() != null, TradeOrderLog::getBeforeStatus, bo.getBeforeStatus());
        lqw.eq(bo.getAfterStatus() != null, TradeOrderLog::getAfterStatus, bo.getAfterStatus());
        lqw.eq(bo.getOperateType() != null, TradeOrderLog::getOperateType, bo.getOperateType());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), TradeOrderLog::getContent, bo.getContent());
        return lqw;
    }

}
