package com.fly.system.api.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 菜单树型vo
 *
 * @author: lxs
 * @date: 2024/8/31
 */
@Data
public class SysMenuTreeVo {

    /**
     * id
     */
    private String id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 按钮权限
     */
    private String buttonPermission;

    /**
     * 按钮权限
     */
    private int level;

    /**
     * 按钮权限
     */
    private String status;

    /**
     * 是否展开
     */
    private Boolean expand;

    /**
     * 子节点
     */
    private List<SysMenuTreeVo> children;

}
