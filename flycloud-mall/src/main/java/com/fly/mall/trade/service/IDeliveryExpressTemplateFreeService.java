package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.DeliveryExpressTemplateFreeBo;
import com.fly.mall.api.domain.trade.vo.DeliveryExpressTemplateFreeVo;

import java.util.Collection;
import java.util.List;

/**
 * 运费模板包邮规则 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDeliveryExpressTemplateFreeService {

    /**
     * 查询运费模板包邮规则详情。
     */
    DeliveryExpressTemplateFreeVo queryById(Long id);

    /**
     * 分页查询运费模板包邮规则。
     */
    PageVo<DeliveryExpressTemplateFreeVo> queryPageList(DeliveryExpressTemplateFreeBo bo, PageBo pageBo);

    /**
     * 查询运费模板包邮规则列表。
     */
    List<DeliveryExpressTemplateFreeVo> queryList(DeliveryExpressTemplateFreeBo bo);

    /**
     * 新增或修改运费模板包邮规则。
     */
    Boolean saveOrUpdate(DeliveryExpressTemplateFreeBo bo);

    /**
     * 校验并批量删除运费模板包邮规则。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
