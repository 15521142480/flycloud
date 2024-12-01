package com.fly.system.mapper;

import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysMenu;
import com.fly.system.api.domain.bo.SysMenuBo;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import com.fly.system.api.domain.vo.SysMenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface SysMenuMapper extends BaseMapperPlus<SysMenuMapper, SysMenu, SysMenuVo> {


    /**
     * 查询菜单列表
     */
    List<SysMenuTreeVo> selectAllList(SysMenuBo bo);

    /**
     * 根据用户获取菜单列表
     */
    List<SysMenuTreeVo> selectMenuListByUserId(@Param("userId") Long userId, @Param("type") Long type);
}
