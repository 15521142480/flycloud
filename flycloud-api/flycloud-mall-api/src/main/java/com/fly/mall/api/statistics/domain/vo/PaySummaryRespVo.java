package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付统计响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class PaySummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rechargePrice;

}
