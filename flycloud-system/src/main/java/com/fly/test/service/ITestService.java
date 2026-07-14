package com.fly.test.service;

import com.fly.test.domain.vo.SeataTestDataListVo;

import java.io.IOException;

/**
 * test
 *
 * @author: lxs
 * @date: 2025/8/15
 */
public interface ITestService {


    /**
     * 测试seata
     *
     * @param isRollback 是否回滚
     * @param dataSourceOneTestData 数据源一的测试数据
     * @param dataSourceTwoTestData 数据源二的测试数据
     * @return 是否执行成功
     */
    Boolean seataTest(Integer isRollback, String dataSourceOneTestData, String dataSourceTwoTestData);

    /**
     * 查询两个数据源各自最新的十条 Seata 测试数据。
     *
     * @return 测试数据集合
     */
    SeataTestDataListVo listTestData();

    /**
     * 测试es
     */
    int esTest(Integer type, String indexName) throws IOException;
}
