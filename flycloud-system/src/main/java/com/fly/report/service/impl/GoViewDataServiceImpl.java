package com.fly.report.service.impl;

import cn.hutool.core.map.MapUtil;
import com.fly.report.service.IGoViewDataService;
import com.fly.system.api.report.domain.vo.GoViewDataVo;
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
 *
 * @author lxs
 * @date 2026-06-30
 */
@Service
@Validated
@RequiredArgsConstructor
public class GoViewDataServiceImpl implements IGoViewDataService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public GoViewDataVo getDataBySql(String sql) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        String[] columnNames = metaData.getColumnNames();

        GoViewDataVo respVo = new GoViewDataVo();
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

}
