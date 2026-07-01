package com.fly.report.service;

import com.fly.system.api.report.domain.vo.GoViewDataRespVo;

/**
 * GoView 数据 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IGoViewDataService {

    /**
     * 使用 SQL 查询 GoView 数据。
     */
    GoViewDataRespVo getDataBySql(String sql);

}
