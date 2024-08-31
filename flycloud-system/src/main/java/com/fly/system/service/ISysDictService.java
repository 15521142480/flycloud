package com.fly.system.service;

import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.common.constant.SystemConstants;
import com.fly.common.database.entity.Search;
import com.fly.common.model.R;
import com.fly.system.api.domain.SysDict;

import java.util.List;

/**
 * 字典表 服务类
 *
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 根据编码和键获取值
     *
     * @param code
     * @param dictKey
     * @return
     */
    @Cached(name = SystemConstants.SYS_DICT_CACHE, expire = 3600)
    R<String> getValue(String code, String dictKey);

    /**
     * 根据编码查询字典列表
     *
     * @param code
     * @return
     */
    @Cached(name = SystemConstants.SYS_DICT_CACHE, key = "#code", expire = 3600)
    R<List<SysDict>> getList(String code);

    /**
     * 字典分页查询
     *
     * @param page
     * @param search
     * @return
     */
    IPage<SysDict> listPage(Page page, Search search);

}
