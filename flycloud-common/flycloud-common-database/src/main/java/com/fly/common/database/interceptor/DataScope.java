package com.fly.common.database.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数据范围
 *
 * @author lxs
 * @date 2023/3/22
 */
@Data
@AllArgsConstructor
public class DataScope {

    private String sqlFilter;

}
