package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.StringUtils;
import com.fly.pay.enums.PayNotifyTypeEnum;
import com.fly.pay.mapper.PayTransferMapper;
import com.fly.pay.service.IPayNotifyService;
import com.fly.pay.service.IPayTransferService;
import com.fly.pay.utils.PayNotifyParseUtils;
import com.fly.system.api.pay.domain.PayTransfer;
import com.fly.system.api.pay.domain.bo.PayTransferBo;
import com.fly.system.api.pay.domain.vo.PayTransferVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 支付转账单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class PayTransferServiceImpl implements IPayTransferService {

    /**
     * 转账成功。
     */
    private static final int TRANSFER_STATUS_SUCCESS = 10;

    private final PayTransferMapper payTransferMapper;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    @Override
    public PayTransferVo queryById(Long id) {
        return payTransferMapper.selectVoById(id);
    }

    @Override
    public PageVo<PayTransferVo> queryPageList(PayTransferBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayTransfer> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayTransfer::getId);
        Page<PayTransferVo> page = payTransferMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayTransferVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<PayTransferVo> queryList(PayTransferBo bo) {
        LambdaQueryWrapper<PayTransfer> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayTransfer::getId);
        return payTransferMapper.selectVoList(lqw);
    }

    /**
     * 支付渠道转账回调后更新转账单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyTransfer(Long channelId, Map<String, String> params, String body, Map<String, String> headers) {
        Map<String, String> data = PayNotifyParseUtils.parseNotifyData(params, body);
        PayTransfer transfer = findNotifyTransfer(data);
        if (transfer == null) {
            throw new ServiceException("转账回调对应的转账单不存在");
        }
        if (Objects.equals(transfer.getStatus(), TRANSFER_STATUS_SUCCESS)) {
            return;
        }

        PayTransfer updateTransfer = new PayTransfer();
        updateTransfer.setId(transfer.getId());
        updateTransfer.setChannelId(channelId);
        updateTransfer.setStatus(TRANSFER_STATUS_SUCCESS);
        updateTransfer.setSuccessTime(LocalDateTime.now());
        updateTransfer.setChannelTransferNo(PayNotifyParseUtils.firstValue(data, "channelTransferNo", "transfer_id", "transfer_no"));
        updateTransfer.setChannelNotifyData(PayNotifyParseUtils.toNotifyData(params, body, headers));
        updateTransfer.setUpdateBy("callback");
        updateTransfer.setUpdateTime(LocalDateTime.now());
        payTransferMapper.updateById(updateTransfer);

        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(PayNotifyTypeEnum.TRANSFER.getType(), transfer.getId());
        }
    }

    /**
     * 构建支付转账单查询条件。
     */
    private LambdaQueryWrapper<PayTransfer> buildQueryWrapper(PayTransferBo bo) {
        LambdaQueryWrapper<PayTransfer> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayTransfer::getIsDeleted, false);
        if (bo != null) {
            lqw.eq(bo.getId() != null, PayTransfer::getId, bo.getId());
            lqw.eq(StringUtils.isNotBlank(bo.getNo()), PayTransfer::getNo, bo.getNo());
            lqw.eq(bo.getAppId() != null, PayTransfer::getAppId, bo.getAppId());
            lqw.eq(StringUtils.isNotBlank(bo.getChannelCode()), PayTransfer::getChannelCode, bo.getChannelCode());
            lqw.eq(bo.getUserId() != null, PayTransfer::getUserId, bo.getUserId());
            lqw.eq(StringUtils.isNotBlank(bo.getMerchantTransferId()), PayTransfer::getMerchantTransferId, bo.getMerchantTransferId());
            lqw.eq(bo.getStatus() != null, PayTransfer::getStatus, bo.getStatus());
        }
        return lqw;
    }

    /**
     * 查询转账回调对应的本地转账单。
     */
    private PayTransfer findNotifyTransfer(Map<String, String> data) {
        String no = PayNotifyParseUtils.firstValue(data, "no", "out_bill_no", "transferNo");
        if (StringUtils.isNotBlank(no)) {
            LambdaQueryWrapper<PayTransfer> noQuery = Wrappers.lambdaQuery();
            noQuery.eq(PayTransfer::getIsDeleted, false);
            noQuery.eq(PayTransfer::getNo, no);
            noQuery.last("LIMIT 1");
            PayTransfer transfer = payTransferMapper.selectOne(noQuery);
            if (transfer != null) {
                return transfer;
            }
        }
        String merchantTransferId = PayNotifyParseUtils.firstValue(data, "merchantTransferId", "merchant_transfer_id");
        if (StringUtils.isNotBlank(merchantTransferId)) {
            LambdaQueryWrapper<PayTransfer> merchantQuery = Wrappers.lambdaQuery();
            merchantQuery.eq(PayTransfer::getIsDeleted, false);
            merchantQuery.eq(PayTransfer::getMerchantTransferId, merchantTransferId);
            merchantQuery.last("LIMIT 1");
            return payTransferMapper.selectOne(merchantQuery);
        }
        return null;
    }

}
