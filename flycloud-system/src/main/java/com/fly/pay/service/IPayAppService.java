package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.PayAppBo;
import com.fly.system.api.pay.domain.vo.PayAppVo;

import java.util.List;

/**
 * 支付应用 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayAppService {
    PageVo<PayAppVo> queryPageList(PayAppBo bo, PageBo pageBo);
    List<PayAppVo> queryList(PayAppBo bo);
    PayAppVo queryById(Long id);
    Boolean saveOrUpdate(PayAppBo bo);
    Boolean updateStatus(Long id, Integer status);
    Boolean deleteById(Long id);
}
