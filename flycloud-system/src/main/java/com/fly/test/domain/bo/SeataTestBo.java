package com.fly.test.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Seata 分布式事务测试请求参数。
 *
 * @author lxs
 */
@Data
public class SeataTestBo {

    /**
     * 写入数据源一的测试数据。
     */
    @NotBlank(message = "数据源一的测试数据不能为空")
    @Size(max = 512, message = "数据源一的测试数据长度不能超过512个字符")
    private String dataSourceOneTestData;

    /**
     * 写入数据源二的测试数据。
     */
    @NotBlank(message = "数据源二的测试数据不能为空")
    @Size(max = 512, message = "数据源二的测试数据长度不能超过512个字符")
    private String dataSourceTwoTestData;

}
