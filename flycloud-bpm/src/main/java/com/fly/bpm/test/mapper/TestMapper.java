package com.fly.bpm.test.mapper;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Seata 测试场景的数据源二数据访问接口。
 *
 * @author: lxs
 * @date: 2025/8/15
 */
public interface TestMapper {


    /**
     * 写入数据源二的测试数据。
     *
     * @param testData 测试数据
     * @return 影响行数
     */
    int insertTestData(@Param("testData") String testData);

    /**
     * 查询数据源二最新的十条测试数据。
     *
     * @return 测试数据列表
     */
    List<SeataTestDataVo> selectSeataTestData();

}
