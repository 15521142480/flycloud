package com.fly.test.service.impl;

import com.fly.bpm.api.feign.ITestApi;
import com.fly.common.domain.model.R;
import com.fly.common.utils.json.JsonUtils;
import com.fly.test.mapper.TestMapper2;
import com.fly.test.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * test
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TestServiceImpl2 implements ITestService {


    private final TestMapper2 testMapper;
    private final ITestApi testApi;



    @Override
    @GlobalTransactional
    public int seataTest(Integer isRollback) {

        int insertOne = testMapper.insertTestData();
        R<Void> insertTwoResult = testApi.seataTest();
        log.info("=== seata测试");
        log.info("=== 插入第一个数据库返回行数:{}", insertOne);
        log.info("=== 插入第二个数据库返回:{}", JsonUtils.toJsonString(insertTwoResult));

        if (isRollback == 1) {
            throw new RuntimeException("回滚测试，已手工异常，请检查各库的test表是否有数据！");
        }

        return 1;
    }
}
