package com.fly.bpm.test.service.impl;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import com.fly.bpm.test.mapper.TestMapper;
import com.fly.bpm.test.service.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public int seataTest(String testData) {

        return testMapper.insertTestData(testData);
    }

    @Override
    public List<SeataTestDataVo> listSeataTestData() {
        return testMapper.selectSeataTestData();
    }

}
