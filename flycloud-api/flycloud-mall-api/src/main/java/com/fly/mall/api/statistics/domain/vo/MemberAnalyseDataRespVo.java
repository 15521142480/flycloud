package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员分析数据响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class MemberAnalyseDataRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer registerUserCount;

    private Integer visitUserCount;

    private Integer rechargeUserCount;

}
