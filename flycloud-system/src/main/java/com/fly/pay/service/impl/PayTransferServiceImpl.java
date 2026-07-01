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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付转账单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayTransferServiceImpl implements IPayTransferService {

    /**
     * 等待转账。
     */
    private static final int TRANSFER_STATUS_WAITING = 0;

    /**
     * 转账进行中。
     */
    private static final int TRANSFER_STATUS_PROCESSING = 5;

    /**
     * 转账成功。
     */
    private static final int TRANSFER_STATUS_SUCCESS = 10;

    /**
     * 转账关闭。
     */
    private static final int TRANSFER_STATUS_CLOSED = 20;

    /**
     * 默认支付应用编号。
     */
    private static final long DEFAULT_APP_ID = 1L;

    private final PayTransferMapper payTransferMapper;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    @Override
    public PayTransferVo queryById(Long id) {
        return payTransferMapper.selectVoById(id);
    }

    /**
     * 根据转账单号查询转账单。
     */
    @Override
    public PayTransferVo getTransferByNo(String no) {
        if (StringUtils.isBlank(no)) {
            return null;
        }
        LambdaQueryWrapper<PayTransfer> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayTransfer::getIsDeleted, false);
        lqw.eq(PayTransfer::getNo, no);
        lqw.last("LIMIT 1");
        return payTransferMapper.selectVoOne(lqw);
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
     * 创建转账单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTransfer(PayTransferBo bo, Long userId, String userIp) {
        validateCreateTransfer(bo);
        Long appId = bo.getAppId() == null ? DEFAULT_APP_ID : bo.getAppId();
        PayTransfer transfer = selectByAppIdAndMerchantTransferId(appId, bo.getMerchantTransferId());
        if (transfer != null) {
            if (!Objects.equals(transfer.getStatus(), TRANSFER_STATUS_CLOSED)) {
                throw new ServiceException("转账单已存在，且不是关闭状态");
            }
            if (!Objects.equals(transfer.getPrice(), bo.getPrice())) {
                throw new ServiceException("转账金额与原转账单不一致");
            }
            if (!Objects.equals(transfer.getChannelCode(), bo.getChannelCode())) {
                throw new ServiceException("转账渠道与原转账单不一致");
            }
            PayTransfer updateTransfer = new PayTransfer();
            updateTransfer.setId(transfer.getId());
            updateTransfer.setStatus(TRANSFER_STATUS_WAITING);
            updateTransfer.setChannelErrorCode("");
            updateTransfer.setChannelErrorMsg("");
            updateTransfer.setUpdateBy(String.valueOf(userId));
            updateTransfer.setUpdateTime(LocalDateTime.now());
            payTransferMapper.updateById(updateTransfer);
            return transfer.getId();
        }

        LocalDateTime now = LocalDateTime.now();
        transfer = new PayTransfer();
        transfer.setNo(generateTransferNo());
        transfer.setAppId(appId);
        transfer.setChannelId(resolveChannelId(bo.getChannelCode()));
        transfer.setChannelCode(bo.getChannelCode());
        transfer.setUserId(userId);
        transfer.setUserType(bo.getUserType());
        transfer.setMerchantTransferId(bo.getMerchantTransferId());
        transfer.setSubject(bo.getSubject());
        transfer.setPrice(bo.getPrice());
        transfer.setUserAccount(bo.getUserAccount());
        transfer.setUserName(bo.getUserName());
        transfer.setStatus(TRANSFER_STATUS_WAITING);
        transfer.setUserIp(userIp);
        transfer.setIsDeleted(false);
        transfer.setCreateBy(String.valueOf(userId));
        transfer.setCreateTime(now);
        transfer.setUpdateBy(String.valueOf(userId));
        transfer.setUpdateTime(now);
        payTransferMapper.insert(transfer);

        if (Objects.equals("mock", transfer.getChannelCode())) {
            markTransferSuccess(transfer, "mock-" + transfer.getNo(), null);
        }
        return transfer.getId();
    }

    /**
     * 同步全部待处理转账单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncTransfer() {
        LambdaQueryWrapper<PayTransfer> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayTransfer::getIsDeleted, false);
        lqw.in(PayTransfer::getStatus, TRANSFER_STATUS_WAITING, TRANSFER_STATUS_PROCESSING);
        List<PayTransfer> transfers = payTransferMapper.selectList(lqw);

        int count = 0;
        for (PayTransfer transfer : transfers) {
            if (syncLocalTransfer(transfer)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 同步单个转账单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTransfer(Long id) {
        PayTransfer transfer = payTransferMapper.selectById(id);
        if (transfer == null || Boolean.TRUE.equals(transfer.getIsDeleted())) {
            throw new ServiceException("转账单不存在");
        }
        syncLocalTransfer(transfer);
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
        if (Objects.equals(transfer.getStatus(), TRANSFER_STATUS_SUCCESS)
                || Objects.equals(transfer.getStatus(), TRANSFER_STATUS_CLOSED)) {
            return;
        }

        Integer notifyStatus = resolveNotifyStatus(data);
        String channelTransferNo = PayNotifyParseUtils.firstValue(data, "channelTransferNo", "transfer_id", "transfer_no");
        String notifyData = PayNotifyParseUtils.toNotifyData(params, body, headers);
        if (Objects.equals(notifyStatus, TRANSFER_STATUS_PROCESSING)) {
            markTransferProcessing(transfer, channelId, channelTransferNo, notifyData);
            return;
        }
        if (Objects.equals(notifyStatus, TRANSFER_STATUS_CLOSED)) {
            markTransferClosed(transfer, channelId, channelTransferNo,
                    PayNotifyParseUtils.firstValue(data, "channelErrorCode", "error_code", "err_code"),
                    PayNotifyParseUtils.firstValue(data, "channelErrorMsg", "error_msg", "err_msg"),
                    notifyData);
            return;
        }
        markTransferSuccess(transfer, channelTransferNo, notifyData);
    }

    /**
     * 校验创建转账单参数。
     */
    private void validateCreateTransfer(PayTransferBo bo) {
        if (bo == null) {
            throw new ServiceException("转账参数不能为空");
        }
        if (StringUtils.isBlank(bo.getMerchantTransferId())) {
            throw new ServiceException("商户转账单号不能为空");
        }
        if (StringUtils.isBlank(bo.getChannelCode())) {
            throw new ServiceException("转账渠道不能为空");
        }
        if (bo.getPrice() == null || bo.getPrice() <= 0) {
            throw new ServiceException("转账金额必须大于 0");
        }
    }

    /**
     * 同步本地可确认的转账状态。
     */
    private boolean syncLocalTransfer(PayTransfer transfer) {
        if (Objects.equals(transfer.getStatus(), TRANSFER_STATUS_SUCCESS)
                || Objects.equals(transfer.getStatus(), TRANSFER_STATUS_CLOSED)) {
            return false;
        }
        if (Objects.equals("mock", transfer.getChannelCode())) {
            markTransferSuccess(transfer, "mock-" + transfer.getNo(), transfer.getChannelNotifyData());
            return true;
        }
        if (Objects.equals(transfer.getStatus(), TRANSFER_STATUS_WAITING)) {
            markTransferProcessing(transfer, transfer.getChannelId(), transfer.getChannelTransferNo(), transfer.getChannelNotifyData());
            return true;
        }
        return false;
    }

    /**
     * 标记转账单为进行中。
     */
    private void markTransferProcessing(PayTransfer transfer, Long channelId, String channelTransferNo, String notifyData) {
        if (!Objects.equals(transfer.getStatus(), TRANSFER_STATUS_WAITING)
                && !Objects.equals(transfer.getStatus(), TRANSFER_STATUS_PROCESSING)) {
            throw new ServiceException("转账单状态不是等待或处理中");
        }
        PayTransfer updateTransfer = new PayTransfer();
        updateTransfer.setId(transfer.getId());
        updateTransfer.setChannelId(channelId);
        updateTransfer.setStatus(TRANSFER_STATUS_PROCESSING);
        updateTransfer.setChannelTransferNo(channelTransferNo);
        updateTransfer.setChannelNotifyData(notifyData);
        updateTransfer.setUpdateTime(LocalDateTime.now());
        payTransferMapper.updateById(updateTransfer);
    }

    /**
     * 标记转账单为成功。
     */
    private void markTransferSuccess(PayTransfer transfer, String channelTransferNo, String notifyData) {
        if (!Objects.equals(transfer.getStatus(), TRANSFER_STATUS_WAITING)
                && !Objects.equals(transfer.getStatus(), TRANSFER_STATUS_PROCESSING)) {
            throw new ServiceException("转账单状态不是等待或处理中");
        }
        PayTransfer updateTransfer = new PayTransfer();
        updateTransfer.setId(transfer.getId());
        updateTransfer.setStatus(TRANSFER_STATUS_SUCCESS);
        updateTransfer.setSuccessTime(LocalDateTime.now());
        updateTransfer.setChannelTransferNo(channelTransferNo);
        updateTransfer.setChannelNotifyData(notifyData);
        updateTransfer.setUpdateTime(LocalDateTime.now());
        payTransferMapper.updateById(updateTransfer);
        createTransferNotifyTask(transfer.getId());
    }

    /**
     * 标记转账单为关闭。
     */
    private void markTransferClosed(PayTransfer transfer, Long channelId, String channelTransferNo,
                                    String errorCode, String errorMsg, String notifyData) {
        if (!Objects.equals(transfer.getStatus(), TRANSFER_STATUS_WAITING)
                && !Objects.equals(transfer.getStatus(), TRANSFER_STATUS_PROCESSING)) {
            throw new ServiceException("转账单状态不是等待或处理中");
        }
        PayTransfer updateTransfer = new PayTransfer();
        updateTransfer.setId(transfer.getId());
        updateTransfer.setChannelId(channelId);
        updateTransfer.setStatus(TRANSFER_STATUS_CLOSED);
        updateTransfer.setChannelTransferNo(channelTransferNo);
        updateTransfer.setChannelErrorCode(errorCode);
        updateTransfer.setChannelErrorMsg(errorMsg);
        updateTransfer.setChannelNotifyData(notifyData);
        updateTransfer.setUpdateTime(LocalDateTime.now());
        payTransferMapper.updateById(updateTransfer);
        createTransferNotifyTask(transfer.getId());
    }

    /**
     * 解析渠道回调的转账状态。
     */
    private Integer resolveNotifyStatus(Map<String, String> data) {
        String status = PayNotifyParseUtils.firstValue(data, "status", "transferStatus", "trade_state");
        if (StringUtils.isBlank(status)) {
            return TRANSFER_STATUS_SUCCESS;
        }
        String normalizedStatus = status.toLowerCase();
        if (normalizedStatus.contains("process") || normalizedStatus.contains("wait")) {
            return TRANSFER_STATUS_PROCESSING;
        }
        if (normalizedStatus.contains("close") || normalizedStatus.contains("fail") || normalizedStatus.contains("error")) {
            return TRANSFER_STATUS_CLOSED;
        }
        return TRANSFER_STATUS_SUCCESS;
    }

    /**
     * 查询指定商户转账单。
     */
    private PayTransfer selectByAppIdAndMerchantTransferId(Long appId, String merchantTransferId) {
        LambdaQueryWrapper<PayTransfer> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayTransfer::getIsDeleted, false);
        lqw.eq(PayTransfer::getAppId, appId);
        lqw.eq(PayTransfer::getMerchantTransferId, merchantTransferId);
        lqw.last("LIMIT 1");
        return payTransferMapper.selectOne(lqw);
    }

    /**
     * 创建转账通知任务。
     */
    private void createTransferNotifyTask(Long transferId) {
        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(PayNotifyTypeEnum.TRANSFER.getType(), transferId);
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

    /**
     * 生成转账单号。
     */
    private String generateTransferNo() {
        return "T" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /**
     * 根据渠道编码生成基础渠道编号。
     */
    private Long resolveChannelId(String channelCode) {
        return switch (channelCode) {
            case "wallet" -> 1L;
            case "mock" -> 2L;
            default -> 0L;
        };
    }

}
