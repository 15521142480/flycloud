package com.fly.bpm.api.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Seata 测试数据请求对象。
 *
 * @author lxs
 */
@Data
public class SeataTestDataReqDTO {

    /**
     * 写入数据源二的测试内容。
     */
    @NotBlank(message = "数据源二的测试数据不能为空")
    @Size(max = 512, message = "数据源二的测试数据长度不能超过512个字符")
    private String dataSourceTwoTestData;

}
