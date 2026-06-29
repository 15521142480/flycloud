package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressVo;

import java.util.Collection;
import java.util.List;

/**
 * 快递公司 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDeliveryExpressService {

    /**
     * 查询快递公司详情。
     */
    DeliveryExpressVo queryById(Long id);

    /**
     * 分页查询快递公司。
     */
    PageVo<DeliveryExpressVo> queryPageList(DeliveryExpressBo bo, PageBo pageBo);

    /**
     * 查询快递公司列表。
     */
    List<DeliveryExpressVo> queryList(DeliveryExpressBo bo);

    /**
     * 新增或修改快递公司。
     */
    Boolean saveOrUpdate(DeliveryExpressBo bo);

    /**
     * 校验并批量删除快递公司。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
