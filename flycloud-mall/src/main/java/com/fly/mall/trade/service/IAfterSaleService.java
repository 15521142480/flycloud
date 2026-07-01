package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.AfterSaleBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleVo;

import java.util.Collection;
import java.util.List;

/**
 * 售后单 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IAfterSaleService {

    /**
     * 查询售后单详情。
     */
    AfterSaleVo queryById(Long id);

    /**
     * 查询当前用户售后单详情。
     */
    AfterSaleVo queryByUserAndId(Long userId, Long id);

    /**
     * 分页查询售后单。
     */
    PageVo<AfterSaleVo> queryPageList(AfterSaleBo bo, PageBo pageBo);

    /**
     * 查询售后单列表。
     */
    List<AfterSaleVo> queryList(AfterSaleBo bo);

    /**
     * 用户创建售后申请。
     */
    Long createAfterSale(Long userId, AfterSaleBo bo);

    /**
     * 用户取消售后申请。
     */
    Boolean cancelAfterSale(Long userId, Long id);

    /**
     * 用户填写退货物流。
     */
    Boolean deliveryAfterSale(Long userId, AfterSaleBo bo);

    /**
     * 后台同意售后申请。
     */
    Boolean agreeAfterSale(Long userId, Long id);

    /**
     * 后台拒绝售后申请。
     */
    Boolean disagreeAfterSale(Long userId, AfterSaleBo bo);

    /**
     * 后台确认收到退货。
     */
    Boolean receiveAfterSale(Long userId, Long id);

    /**
     * 后台确认退款。
     */
    Boolean refundAfterSale(Long userId, Long id);

    /**
     * 后台拒绝收货。
     */
    Boolean refuseAfterSale(Long userId, AfterSaleBo bo);

    /**
     * 支付退款回调后更新售后单为已退款。
     */
    Boolean updateAfterSaleRefunded(Long id, Long orderId, Long payRefundId);

    /**
     * 新增或修改售后单。
     */
    Boolean saveOrUpdate(AfterSaleBo bo);

    /**
     * 校验并批量删除售后单。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
