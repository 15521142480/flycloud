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
//    private final ElasticSearchService elasticSearchService;



    /**
     * note: @GlobalTransactional(
     *             name = "system-test-seata",
     *             timeoutMills = 60000,
     *             rollbackFor = Exception.class
     *     )
     *     声明全局事物，写在分布式业务的发起服务层
     *
     * note: @Transactional(rollbackFor = Exception.class): 保留本地事务（如果本地事物涉及多条-增删改）
     *
     *
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

    @Override
    public SeataTestDataListVo listTestData() {
        SeataTestDataListVo result = new SeataTestDataListVo();
        result.setDataSourceOne(testMapper.selectDataSourceOneTestData());
        result.setDataSourceTwo(testApi.listSeataTestData().getCheckedData());
        return result;
    }


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
