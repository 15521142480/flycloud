package com.fly.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.pay.mapper.PayChannelMapper;
import com.fly.pay.service.IPayChannelService;
import com.fly.system.api.pay.domain.PayChannel;
import com.fly.system.api.pay.domain.bo.PayChannelBo;
import com.fly.system.api.pay.domain.vo.PayChannelRespVo;
import com.fly.system.api.pay.domain.vo.PayChannelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 支付渠道 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayChannelServiceImpl implements IPayChannelService {

    private final PayChannelMapper payChannelMapper;

    @Override
    public PageVo<PayChannelVo> queryPageList(PayChannelBo bo, PageBo pageBo) {
        Page<PayChannelVo> page = payChannelMapper.selectVoPage(pageBo.build(), buildQueryWrapper(bo).orderByDesc(PayChannel::getId));
        PageVo<PayChannelVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<PayChannelVo> queryList(PayChannelBo bo) {
        return payChannelMapper.selectVoList(buildQueryWrapper(bo).orderByDesc(PayChannel::getId));
    }

    @Override
    public PayChannelVo queryById(Long id) {
        return payChannelMapper.selectVoById(id);
    }

    @Override
    public PayChannelRespVo getChannel(Long id, Long appId, String code) {
        PayChannelVo channel = null;
        if (id != null) {
            channel = queryById(id);
        } else if (appId != null && StringUtils.isNotBlank(code)) {
            PayChannelBo bo = new PayChannelBo();
            bo.setAppId(appId);
            bo.setCode(code);
            channel = queryList(bo).stream().findFirst().orElse(null);
        }
        return BeanUtil.toBean(channel, PayChannelRespVo.class);
    }

    @Override
    public Long createChannel(PayChannelBo bo) {
        validateCodeUnique(null, bo.getAppId(), bo.getCode());
        PayChannel channel = BeanUtil.toBean(bo, PayChannel.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        channel.setIsDeleted(false);
        channel.setCreateBy(userId);
        channel.setCreateTime(now);
        channel.setUpdateBy(userId);
        channel.setUpdateTime(now);
        payChannelMapper.insert(channel);
        return channel.getId();
    }

    @Override
    public Boolean saveOrUpdate(PayChannelBo bo) {
        validateCodeUnique(bo.getId(), bo.getAppId(), bo.getCode());
        PayChannel channel = BeanUtil.toBean(bo, PayChannel.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        channel.setUpdateBy(userId);
        channel.setUpdateTime(now);
        if (channel.getId() != null) {
            validateExists(channel.getId());
            return payChannelMapper.updateById(channel) > 0;
        }
        channel.setIsDeleted(false);
        channel.setCreateBy(userId);
        channel.setCreateTime(now);
        return payChannelMapper.insert(channel) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        return payChannelMapper.deleteById(id) > 0;
    }

    @Override
    public Set<String> getEnableChannelCodeList(Long appId) {
        PayChannelBo bo = new PayChannelBo();
        bo.setAppId(appId);
        bo.setStatus(0);
        return queryList(bo).stream().map(PayChannelVo::getCode).collect(Collectors.toSet());
    }

    private LambdaQueryWrapper<PayChannel> buildQueryWrapper(PayChannelBo bo) {
        LambdaQueryWrapper<PayChannel> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayChannel::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PayChannel::getId, bo.getId());
        lqw.eq(bo.getAppId() != null, PayChannel::getAppId, bo.getAppId());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), PayChannel::getCode, bo.getCode());
        lqw.eq(bo.getStatus() != null, PayChannel::getStatus, bo.getStatus());
        return lqw;
    }

    private void validateExists(Long id) {
        PayChannel channel = payChannelMapper.selectById(id);
        if (channel == null || Boolean.TRUE.equals(channel.getIsDeleted())) {
            throw new ServiceException("支付渠道不存在");
        }
    }

    private void validateCodeUnique(Long id, Long appId, String code) {
        if (appId == null || StringUtils.isBlank(code)) {
            return;
        }
        PayChannel channel = payChannelMapper.selectOne(Wrappers.<PayChannel>lambdaQuery()
                .eq(PayChannel::getIsDeleted, false)
                .eq(PayChannel::getAppId, appId)
                .eq(PayChannel::getCode, code)
                .last("LIMIT 1"));
        if (channel != null && !channel.getId().equals(id)) {
            throw new ServiceException("同一支付应用下渠道编码已存在");
        }
    }

}
