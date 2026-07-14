package com.fly.bpm.api.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Seata 测试数据视图对象。
 *
 * @author lxs
 */
@Data
public class SeataTestDataVo {

    /** 数据ID。 */
    private Integer id;

    /** 数据源名称。 */
    private String databaseName;

    /** 测试数据。 */
    private String testData;

    /** 创建时间。 */
    private LocalDateTime createTime;

}
