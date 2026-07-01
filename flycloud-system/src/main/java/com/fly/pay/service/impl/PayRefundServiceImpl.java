package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.StringUtils;
import com.fly.pay.mapper.PayRefundMapper;
import com.fly.pay.service.IPayRefundService;
import com.fly.system.api.pay.domain.PayRefund;
import com.fly.system.api.pay.domain.bo.PayRefundBo;
import com.fly.system.api.pay.domain.vo.PayRefundVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付退款单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class PayRefundServiceImpl implements IPayRefundService {

    private final PayRefundMapper payRefundMapper;

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

}
