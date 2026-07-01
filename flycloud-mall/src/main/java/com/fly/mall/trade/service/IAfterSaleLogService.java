package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.AfterSaleLogBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 售后日志 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IAfterSaleLogService {

    /**
     * 查询售后日志详情。
     */
    AfterSaleLogVo queryById(Long id);

    /**
     * 分页查询售后日志。
     */
    PageVo<AfterSaleLogVo> queryPageList(AfterSaleLogBo bo, PageBo pageBo);

    /**
     * 查询售后日志列表。
     */
    List<AfterSaleLogVo> queryList(AfterSaleLogBo bo);

    /**
     * 新增或修改售后日志。
     */
    Boolean saveOrUpdate(AfterSaleLogBo bo);

    /**
     * 校验并批量删除售后日志。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
