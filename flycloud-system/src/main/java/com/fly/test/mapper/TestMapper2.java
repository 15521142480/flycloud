package com.fly.test.mapper;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * - 接口层
 *
 * @author: lxs
 * @date: 2025/8/15
 */
public interface TestMapper2 {


    /**
     * 写入数据源一的测试数据。
     *
     * @param testData 测试数据
     * @return 影响行数
     */
    int insertTestData(@Param("testData") String testData);

    /**
     * 查询数据源一最新的十条测试数据。
     *
     * @return 测试数据列表
     */
    List<SeataTestDataVo> selectDataSourceOneTestData();

}
