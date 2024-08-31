package com.fly.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.common.database.entity.Search;
import com.fly.system.api.domain.SysRole;
import com.fly.system.api.poi.SysRolePOI;
import com.fly.system.api.vo.SysRoleVO;

import java.util.List;
import java.util.Map;

/**
 * 角色表 服务类
 *
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 角色树
     *
     * @return
     */
    List<SysRoleVO> tree();

    /**
     * 查询角色列表
     *
     * @param search
     * @return
     */
    List<SysRole> listSearch(Map<String, String> search);

    /**
     * 查询权限集
     *
     * @param id
     * @return
     */
    List<String> getPermission(String id);

    /**
     * 角色导出
     *
     * @return
     */
    List<SysRolePOI> export();

    /**
     * 分页列表
     *
     * @param search
     * @return
     */
    IPage<SysRole> listPage(Search search);

    /**
     * 设置角色
     *
     * @param sysRole 角色对象
     * @return 布尔
     */
    boolean set(SysRole sysRole);

    /**
     * 根据ids批量删除角色
     *
     * @param ids ID列表
     * @return boolean
     */
    boolean batchDeleteByIds(String ids);
}
