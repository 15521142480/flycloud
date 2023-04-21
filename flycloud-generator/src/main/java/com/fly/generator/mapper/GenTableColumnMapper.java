package com.fly.generator.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.fly.common.database.mapper.BaseMapperPlus;
import com.fly.generator.domain.GenTableColumn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author lxs
 */
//@InterceptorIgnore(dataPermission = "true")
@Mapper
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumnMapper, GenTableColumn, GenTableColumn> {

    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
