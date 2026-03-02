package com.fly.test.service;

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
    */
    int seataTest(Integer isRollback);

    /**
     * 测试es
     */
    int esTest(Integer type, String indexName) throws IOException;
}
