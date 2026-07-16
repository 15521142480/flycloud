package com.fly.test.service.impl;

import com.fly.bpm.api.domain.dto.SeataTestDataReqDTO;
import com.fly.bpm.api.feign.ITestApi;
import com.fly.test.domain.vo.SeataTestDataListVo;
import com.fly.test.mapper.TestMapper2;
import com.fly.test.service.ITestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Seata 分布式事务测试服务实现。
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



    /**
     * 执行两个数据源的 Seata AT 模式事务测试。
     *
     * <p>全局事务由当前服务发起；本地事务用于保证当前服务的多条 SQL 操作保持原子性。
     * 当 {@code isRollback} 为 1 时，在两个数据源写入完成后主动抛出异常，以验证全局回滚。</p>
     *
     * @param isRollback 是否模拟异常并回滚：0 否、1 是
     * @param dataSourceOneTestData 写入数据源一的测试内容
     * @param dataSourceTwoTestData 写入数据源二的测试内容
     * @return 执行成功时返回 {@code true}
     */
    @Override
    @GlobalTransactional(
            name = "system-test-seata",
            timeoutMills = 60000,
            rollbackFor = Exception.class
    )
    @Transactional(rollbackFor = Exception.class)
    public Boolean seataTest(Integer isRollback, String dataSourceOneTestData, String dataSourceTwoTestData) {

        int oneDataBaseResultCount = testMapper.insertTestData(dataSourceOneTestData);
        SeataTestDataReqDTO reqDTO = new SeataTestDataReqDTO();
        reqDTO.setDataSourceTwoTestData(dataSourceTwoTestData);
        Integer twoDataBaseResultCount = testApi.seataTest(reqDTO).getCheckedData();
        log.info("=== seata测试");
        log.info("=== 插入第一个数据库返回行数:{}", oneDataBaseResultCount);
        log.info("=== 插入第二个数据库返回行数:{}", twoDataBaseResultCount);

        if (isRollback == 1) {
            throw new RuntimeException("回滚测试，已手工异常，请检查下面的各数据源是否有该数据！");
        }

        return true;
    }

    /**
     * 查询两个数据源各自最新的测试数据。
     *
     * @return 数据源一与数据源二的测试数据集合
     */
    @Override
    public SeataTestDataListVo listTestData() {
        SeataTestDataListVo result = new SeataTestDataListVo();
        result.setDataSourceOne(testMapper.selectDataSourceOneTestData());
        result.setDataSourceTwo(testApi.listSeataTestData().getCheckedData());
        return result;
    }


    /**
     * 预留的 Elasticsearch 测试入口。
     *
     * @param type 操作类型
     * @param indexName 索引名称
     * @return 当前固定返回 1
     * @throws IOException Elasticsearch 调用异常
     */
    @Override
    public int esTest(Integer type, String indexName) throws IOException {

        // 1到4分别是增删改查
        switch (type) {
            case 1:
//                elasticSearchService.createIndex("testIndex");
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                log.error("未知类型");
                break;
        }

        return 1;
    }
}
