package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.StringUtils;
import com.fly.pay.enums.PayNotifyTypeEnum;
import com.fly.pay.mapper.PayRefundMapper;
import com.fly.pay.service.IPayNotifyService;
import com.fly.pay.service.IPayRefundService;
import com.fly.pay.utils.PayNotifyParseUtils;
import com.fly.system.api.pay.domain.PayRefund;
import com.fly.system.api.pay.domain.bo.PayRefundBo;
import com.fly.system.api.pay.domain.vo.PayRefundVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 支付退款单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayRefundServiceImpl implements IPayRefundService {

    /**
     * 退款成功。
     */
    private static final int REFUND_STATUS_SUCCESS = 10;

    private final PayRefundMapper payRefundMapper;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    @Override
    public PayRefundVo queryById(Long id) {
        return payRefundMapper.selectVoById(id);
    }

    @Override
    public PageVo<PayRefundVo> queryPageList(PayRefundBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayRefund> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayRefund::getId);
        Page<PayRefundVo> page = payRefundMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayRefundVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<PayRefundVo> queryList(PayRefundBo bo) {
        LambdaQueryWrapper<PayRefund> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayRefund::getId);
        return payRefundMapper.selectVoList(lqw);
    }

    /**
     * 支付渠道退款回调后更新退款单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyRefund(Long channelId, Map<String, String> params, String body, Map<String, String> headers) {
        Map<String, String> data = PayNotifyParseUtils.parseNotifyData(params, body);
        PayRefund refund = findNotifyRefund(data);
        if (refund == null) {
            throw new ServiceException("退款回调对应的退款单不存在");
        }
        if (Objects.equals(refund.getStatus(), REFUND_STATUS_SUCCESS)) {
            return;
        }

        PayRefund updateRefund = new PayRefund();
        updateRefund.setId(refund.getId());
        updateRefund.setChannelId(channelId);
        updateRefund.setStatus(REFUND_STATUS_SUCCESS);
        updateRefund.setSuccessTime(LocalDateTime.now());
        updateRefund.setChannelRefundNo(PayNotifyParseUtils.firstValue(data, "channelRefundNo", "refund_id", "refund_no"));
        updateRefund.setChannelNotifyData(PayNotifyParseUtils.toNotifyData(params, body, headers));
        updateRefund.setUpdateBy("callback");
        updateRefund.setUpdateTime(LocalDateTime.now());
        payRefundMapper.updateById(updateRefund);

        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(PayNotifyTypeEnum.REFUND.getType(), refund.getId());
        }
    }

    /**
     * 构建支付退款单查询条件。
     */
    private LambdaQueryWrapper<PayRefund> buildQueryWrapper(PayRefundBo bo) {
        LambdaQueryWrapper<PayRefund> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayRefund::getIsDeleted, false);
        if (bo != null) {
            lqw.eq(bo.getId() != null, PayRefund::getId, bo.getId());
            lqw.eq(StringUtils.isNotBlank(bo.getNo()), PayRefund::getNo, bo.getNo());
            lqw.eq(bo.getAppId() != null, PayRefund::getAppId, bo.getAppId());
            lqw.eq(bo.getOrderId() != null, PayRefund::getOrderId, bo.getOrderId());
            lqw.eq(bo.getUserId() != null, PayRefund::getUserId, bo.getUserId());
            lqw.eq(StringUtils.isNotBlank(bo.getMerchantOrderId()), PayRefund::getMerchantOrderId, bo.getMerchantOrderId());
            lqw.eq(StringUtils.isNotBlank(bo.getMerchantRefundId()), PayRefund::getMerchantRefundId, bo.getMerchantRefundId());
            lqw.eq(bo.getStatus() != null, PayRefund::getStatus, bo.getStatus());
        }
        return lqw;
    }

    /**
     * 查询退款回调对应的本地退款单。
     */
    private PayRefund findNotifyRefund(Map<String, String> data) {
        String no = PayNotifyParseUtils.firstValue(data, "no", "out_refund_no", "refundNo");
        if (StringUtils.isNotBlank(no)) {
            LambdaQueryWrapper<PayRefund> noQuery = Wrappers.lambdaQuery();
            noQuery.eq(PayRefund::getIsDeleted, false);
            noQuery.eq(PayRefund::getNo, no);
            noQuery.last("LIMIT 1");
            PayRefund refund = payRefundMapper.selectOne(noQuery);
            if (refund != null) {
                return refund;
            }
        }
        String merchantRefundId = PayNotifyParseUtils.firstValue(data, "merchantRefundId", "merchant_refund_id");
        if (StringUtils.isNotBlank(merchantRefundId)) {
            LambdaQueryWrapper<PayRefund> merchantQuery = Wrappers.lambdaQuery();
            merchantQuery.eq(PayRefund::getIsDeleted, false);
            merchantQuery.eq(PayRefund::getMerchantRefundId, merchantRefundId);
            merchantQuery.last("LIMIT 1");
            return payRefundMapper.selectOne(merchantQuery);
        }
        return null;
    }

}
