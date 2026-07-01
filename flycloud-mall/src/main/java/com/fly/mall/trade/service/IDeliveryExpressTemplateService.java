package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 运费模板 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IDeliveryExpressTemplateService {

    /**
     * 查询运费模板详情。
     */
    DeliveryExpressTemplateVo queryById(Long id);

    /**
     * 分页查询运费模板。
     */
    PageVo<DeliveryExpressTemplateVo> queryPageList(DeliveryExpressTemplateBo bo, PageBo pageBo);

    /**
     * 查询运费模板列表。
     */
    List<DeliveryExpressTemplateVo> queryList(DeliveryExpressTemplateBo bo);

    /**
     * 新增或修改运费模板。
     */
    Boolean saveOrUpdate(DeliveryExpressTemplateBo bo);

    /**
     * 校验并批量删除运费模板。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
