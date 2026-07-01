package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.StringUtils;
import com.fly.pay.mapper.PayTransferMapper;
import com.fly.pay.service.IPayTransferService;
import com.fly.system.api.pay.domain.PayTransfer;
import com.fly.system.api.pay.domain.bo.PayTransferBo;
import com.fly.system.api.pay.domain.vo.PayTransferVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付转账单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class PayTransferServiceImpl implements IPayTransferService {

    private final PayTransferMapper payTransferMapper;

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

}
