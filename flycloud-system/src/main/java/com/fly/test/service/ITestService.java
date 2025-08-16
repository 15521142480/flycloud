package com.fly.test.service;

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

}
