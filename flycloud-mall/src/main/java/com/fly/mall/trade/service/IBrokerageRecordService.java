package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.BrokerageRecordBo;
import com.fly.mall.api.trade.domain.vo.BrokerageRecordVo;

import java.util.Collection;
import java.util.List;

/**
 * 佣金记录 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IBrokerageRecordService {

    /**
     * 查询佣金记录详情。
     */
    BrokerageRecordVo queryById(Long id);

    /**
     * 分页查询佣金记录。
     */
    PageVo<BrokerageRecordVo> queryPageList(BrokerageRecordBo bo, PageBo pageBo);

    /**
     * 查询佣金记录列表。
     */
    List<BrokerageRecordVo> queryList(BrokerageRecordBo bo);

    /**
     * 新增或修改佣金记录。
     */
    Boolean saveOrUpdate(BrokerageRecordBo bo);

    /**
     * 校验并批量删除佣金记录。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
