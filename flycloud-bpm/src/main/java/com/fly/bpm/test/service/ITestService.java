package com.fly.bpm.test.service;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;

import java.util.List;

/**
 * Seata 测试场景的数据源二业务服务。
 *
 * @author: lxs
 * @date: 2025/8/15
 */
public interface ITestService {


    /**
     * 写入数据源二的 Seata 测试数据。
     *
     * @param testData 测试内容
     * @return 影响行数
     */
    int seataTest(String testData);

    /**
     * 查询数据源二最新的十条 Seata 测试数据。
     *
     * @return 测试数据列表
     */
    List<SeataTestDataVo> listSeataTestData();

}
