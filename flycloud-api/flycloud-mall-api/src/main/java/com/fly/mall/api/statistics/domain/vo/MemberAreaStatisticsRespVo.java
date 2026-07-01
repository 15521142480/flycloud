package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员地区统计响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberAreaStatisticsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer areaId;

    private String areaName;

    private Integer userCount;

    private Integer orderCreateUserCount;

    private Integer orderPayUserCount;

    private Integer orderPayPrice;

}
