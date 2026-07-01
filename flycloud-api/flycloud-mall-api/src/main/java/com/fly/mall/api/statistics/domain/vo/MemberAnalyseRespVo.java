package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员分析响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberAnalyseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer visitUserCount;

    private Integer orderUserCount;

    private Integer payUserCount;

    private Integer atv;

    private DataComparisonRespVo<MemberAnalyseDataRespVo> comparison;

}
