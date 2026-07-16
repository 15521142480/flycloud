package com.fly.bpm.test.service.impl;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import com.fly.bpm.test.mapper.TestMapper;
import com.fly.bpm.test.service.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Seata 测试场景的数据源二业务服务实现。
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@RequiredArgsConstructor
@Service
public class TestServiceImpl implements ITestService {

    private final TestMapper testMapper;

    /**
     * 写入数据源二的 Seata 测试数据。
     *
     * @param testData 测试内容
     * @return 影响行数
     */
    @Override
    public int seataTest(String testData) {

        return testMapper.insertTestData(testData);
    }

    /**
     * 查询数据源二最新的 Seata 测试数据。
     *
     * @return 测试数据列表
     */
    @Override
    public List<SeataTestDataVo> listSeataTestData() {
        return testMapper.selectSeataTestData();
    }

}
