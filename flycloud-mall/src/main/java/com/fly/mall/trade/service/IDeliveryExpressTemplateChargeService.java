package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateChargeBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateChargeVo;

import java.util.Collection;
import java.util.List;

/**
 * 运费模板计费规则 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDeliveryExpressTemplateChargeService {

    /**
     * 查询运费模板计费规则详情。
     */
    DeliveryExpressTemplateChargeVo queryById(Long id);

    /**
     * 分页查询运费模板计费规则。
     */
    PageVo<DeliveryExpressTemplateChargeVo> queryPageList(DeliveryExpressTemplateChargeBo bo, PageBo pageBo);

    /**
     * 查询运费模板计费规则列表。
     */
    List<DeliveryExpressTemplateChargeVo> queryList(DeliveryExpressTemplateChargeBo bo);

    /**
     * 新增或修改运费模板计费规则。
     */
    Boolean saveOrUpdate(DeliveryExpressTemplateChargeBo bo);

    /**
     * 校验并批量删除运费模板计费规则。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
