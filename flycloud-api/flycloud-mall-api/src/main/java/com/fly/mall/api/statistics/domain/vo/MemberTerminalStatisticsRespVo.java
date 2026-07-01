package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员终端统计响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberTerminalStatisticsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer terminal;

    private Integer userCount;

}
