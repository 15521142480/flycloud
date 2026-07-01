package com.fly.report.service.impl;

import cn.hutool.core.map.MapUtil;
import com.fly.report.service.IGoViewDataService;
import com.fly.system.api.report.domain.vo.GoViewDataRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

/**
 * GoView 数据 Service 业务层处理。
 * <p>
 * 默认使用系统数据源执行 SQL 查询；如果后续需要多数据源，可以在这里按数据源类型扩展 JdbcTemplate。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
@RequiredArgsConstructor
public class GoViewDataServiceImpl implements IGoViewDataService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public GoViewDataRespVo getDataBySql(String sql) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        String[] columnNames = metaData.getColumnNames();

        GoViewDataRespVo respVo = new GoViewDataRespVo();
        respVo.setDimensions(Arrays.asList(columnNames));
        respVo.setSource(new LinkedList<>());
        while (sqlRowSet.next()) {
            Map<String, Object> row = MapUtil.newHashMap(columnNames.length);
            for (String columnName : columnNames) {
                row.put(columnName, sqlRowSet.getObject(columnName));
            }
            respVo.getSource().add(row);
        }
        return respVo;
    }

    @Override
    public GoViewDataRespVo getDataBySQL(String sql) {
        return getDataBySql(sql);
    }

}
