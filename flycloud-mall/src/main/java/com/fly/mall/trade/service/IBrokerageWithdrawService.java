package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.BrokerageWithdrawBo;
import com.fly.mall.api.domain.trade.vo.BrokerageWithdrawVo;

import java.util.Collection;
import java.util.List;

/**
 * 佣金提现 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IBrokerageWithdrawService {

    /**
     * 查询佣金提现详情。
     */
    BrokerageWithdrawVo queryById(Long id);

    /**
     * 分页查询佣金提现。
     */
    PageVo<BrokerageWithdrawVo> queryPageList(BrokerageWithdrawBo bo, PageBo pageBo);

    /**
     * 查询佣金提现列表。
     */
    List<BrokerageWithdrawVo> queryList(BrokerageWithdrawBo bo);

    /**
     * 新增或修改佣金提现。
     */
    Boolean saveOrUpdate(BrokerageWithdrawBo bo);

    /**
     * 校验并批量删除佣金提现。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
