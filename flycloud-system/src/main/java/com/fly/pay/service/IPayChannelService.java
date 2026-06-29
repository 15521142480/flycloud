package com.fly.pay.service;

import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.system.api.pay.domain.bo.PayChannelBo;
import com.fly.system.api.pay.domain.vo.PayChannelVo;

import java.util.List;
import java.util.Set;

/**
 * 支付渠道 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayChannelService {
    PageVo<PayChannelVo> queryPageList(PayChannelBo bo, PageBo pageBo);
    List<PayChannelVo> queryList(PayChannelBo bo);
    PayChannelVo queryById(Long id);
    Boolean saveOrUpdate(PayChannelBo bo);
    Boolean deleteById(Long id);
    Set<String> getEnableChannelCodeList(Long appId);
}
