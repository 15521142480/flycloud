package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.trade.domain.BrokerageWithdraw;
import com.fly.mall.api.trade.domain.bo.BrokerageWithdrawBo;
import com.fly.mall.api.trade.domain.vo.BrokerageWithdrawVo;
import com.fly.mall.trade.mapper.BrokerageWithdrawMapper;
import com.fly.mall.trade.service.IBrokerageWithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 佣金提现 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class BrokerageWithdrawServiceImpl extends BaseServiceImpl<BrokerageWithdrawMapper, BrokerageWithdraw> implements IBrokerageWithdrawService {

    private final BrokerageWithdrawMapper baseMapper;

    /**
     * 查询佣金提现详情。
     */
    @Override
    public BrokerageWithdrawVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询佣金提现。
     */
    @Override
    public PageVo<BrokerageWithdrawVo> queryPageList(BrokerageWithdrawBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BrokerageWithdraw> lqw = buildQueryWrapper(bo);
        Page<BrokerageWithdrawVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询佣金提现列表。
     */
    @Override
    public List<BrokerageWithdrawVo> queryList(BrokerageWithdrawBo bo) {
        LambdaQueryWrapper<BrokerageWithdraw> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改佣金提现。
     */
    @Override
    public Boolean saveOrUpdate(BrokerageWithdrawBo bo) {
        BrokerageWithdraw entity = BeanUtil.toBean(bo, BrokerageWithdraw.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除佣金提现。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            BrokerageWithdraw entity = new BrokerageWithdraw();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建佣金提现查询条件。
     */
    private LambdaQueryWrapper<BrokerageWithdraw> buildQueryWrapper(BrokerageWithdrawBo bo) {
        LambdaQueryWrapper<BrokerageWithdraw> lqw = Wrappers.lambdaQuery();
        lqw.eq(BrokerageWithdraw::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BrokerageWithdraw::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, BrokerageWithdraw::getUserId, bo.getUserId());
        lqw.eq(bo.getPrice() != null, BrokerageWithdraw::getPrice, bo.getPrice());
        lqw.eq(bo.getFeePrice() != null, BrokerageWithdraw::getFeePrice, bo.getFeePrice());
        lqw.eq(bo.getTotalPrice() != null, BrokerageWithdraw::getTotalPrice, bo.getTotalPrice());
        lqw.eq(bo.getType() != null, BrokerageWithdraw::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), BrokerageWithdraw::getUserName, bo.getUserName());
        lqw.like(StringUtils.isNotBlank(bo.getUserAccount()), BrokerageWithdraw::getUserAccount, bo.getUserAccount());
        lqw.like(StringUtils.isNotBlank(bo.getQrCodeUrl()), BrokerageWithdraw::getQrCodeUrl, bo.getQrCodeUrl());
        lqw.like(StringUtils.isNotBlank(bo.getBankName()), BrokerageWithdraw::getBankName, bo.getBankName());
        lqw.like(StringUtils.isNotBlank(bo.getBankAddress()), BrokerageWithdraw::getBankAddress, bo.getBankAddress());
        lqw.eq(bo.getStatus() != null, BrokerageWithdraw::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getAuditReason()), BrokerageWithdraw::getAuditReason, bo.getAuditReason());
        lqw.eq(bo.getAuditTime() != null, BrokerageWithdraw::getAuditTime, bo.getAuditTime());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), BrokerageWithdraw::getRemark, bo.getRemark());
        lqw.eq(bo.getPayTransferId() != null, BrokerageWithdraw::getPayTransferId, bo.getPayTransferId());
        lqw.like(StringUtils.isNotBlank(bo.getTransferChannelCode()), BrokerageWithdraw::getTransferChannelCode, bo.getTransferChannelCode());
        lqw.eq(bo.getTransferTime() != null, BrokerageWithdraw::getTransferTime, bo.getTransferTime());
        lqw.like(StringUtils.isNotBlank(bo.getTransferErrorMsg()), BrokerageWithdraw::getTransferErrorMsg, bo.getTransferErrorMsg());
        return lqw;
    }

}
