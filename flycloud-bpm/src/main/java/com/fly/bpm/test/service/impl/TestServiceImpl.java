package com.fly.bpm.test.service.impl;

import com.fly.bpm.test.mapper.TestMapper;
import com.fly.bpm.test.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * test
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@RequiredArgsConstructor
@Service
public class TestServiceImpl implements ITestService {

    private final TestMapper testMapper;


    @Override
    public int seataTest() {

        return testMapper.insertTestData();
    }

}
