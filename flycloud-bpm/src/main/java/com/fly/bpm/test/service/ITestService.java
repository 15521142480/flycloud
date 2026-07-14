package com.fly.bpm.test.service;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;

import java.util.List;

/**
 * - 接口层
 *
 * @author: lxs
 * @date: 2025/8/15
 */
public interface ITestService {


    /**
     * seata测试
     */
    int seataTest(String testData);

    /**
     * 查询数据源二最新的十条 Seata 测试数据。
     *
     * @return 测试数据列表
     */
    List<SeataTestDataVo> listSeataTestData();

}
