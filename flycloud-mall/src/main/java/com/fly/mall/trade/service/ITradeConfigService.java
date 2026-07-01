package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.TradeConfigBo;
import com.fly.mall.api.trade.domain.vo.TradeConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 交易配置 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ITradeConfigService {

    /**
     * 获得交易配置。
     */
    TradeConfigVo getTradeConfig();

    /**
     * 查询交易配置详情。
     */
    TradeConfigVo queryById(Long id);

    /**
     * 分页查询交易配置。
     */
    PageVo<TradeConfigVo> queryPageList(TradeConfigBo bo, PageBo pageBo);

    /**
     * 查询交易配置列表。
     */
    List<TradeConfigVo> queryList(TradeConfigBo bo);

    /**
     * 新增或修改交易配置。
     */
    Boolean saveOrUpdate(TradeConfigBo bo);

    /**
     * 校验并批量删除交易配置。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
