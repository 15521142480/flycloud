package com.fly.common.database.web.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.database.web.service.BaseService;


/**
 * 基础服务类，所有Service都要继承
 *
 * @author lxs
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {


    /**
     * 构建分页对象
     *
     * @param query 分页参数
     */
    public IPage<T> build(PageBo query) {

        Page<T> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 字段排序
//        if (query.getColumns().length > 0 && query.getOrders().length > 0) {
//            return page.addOrder(query.getOrderBy());
//        }

        return page;
    }


    /**
     * 构建分页vo泛型
     */
    public <T> PageVo<T> build(IPage<T> page) {

        PageVo<T> pageVo = new PageVo<>();
        pageVo.setPages(page.getPages());
        pageVo.setTotal(page.getTotal());
        pageVo.setList(page.getRecords());
        return pageVo;
    }


    /**
     * 原生SQL 数据权限
     * @param tableAlias 表别名，多表关联时，需要填写表别名
     * @param orgIdAlias 机构ID别名，null：表示org_id
     * @return 返回数据权限
     */
//    protected DataScope getDataScope(String tableAlias, String orgIdAlias)  {
//        UserDetail user = SecurityUser.getUser();
//        // 如果是超级管理员，则不进行数据过滤
//        if(user.getSuperAdmin().equals(Constant.SUPER_ADMIN)) {
//            return null;
//        }
//
//        // 如果为null，则设置成空字符串
//        if(tableAlias == null){
//            tableAlias = "";
//        }
//
//        // 获取表的别名
//        if(StringUtils.isNotBlank(tableAlias)){
//            tableAlias +=  ".";
//        }
//
//        StringBuilder sqlFilter = new StringBuilder();
//        sqlFilter.append(" (");
//
//        // 数据权限范围
//        List<Long> dataScopeList = user.getDataScopeList();
//        // 全部数据权限
//        if (dataScopeList == null){
//            return null;
//        }
//        // 数据过滤
//        if(dataScopeList.size() > 0){
//            if(StringUtils.isBlank(orgIdAlias)){
//                orgIdAlias = "org_id";
//            }
//            sqlFilter.append(tableAlias).append(orgIdAlias);
//
//            sqlFilter.append(" in(").append(StrUtil.join(",", dataScopeList)).append(")");
//
//            sqlFilter.append(" or ");
//        }
//
//        // 查询本人数据
//        sqlFilter.append(tableAlias).append("creator").append("=").append(user.getId());
//
//        sqlFilter.append(")");
//
//        return new DataScope(sqlFilter.toString());
//    }

    /**
     * MyBatis-Plus 数据权限
     */
//    protected void dataScopeWrapper(LambdaQueryWrapper<T> queryWrapper)  {
//        DataScope dataScope = getDataScope(null, null);
//        if (dataScope != null){
//            queryWrapper.apply(dataScope.getSqlFilter());
//        }
//    }

}