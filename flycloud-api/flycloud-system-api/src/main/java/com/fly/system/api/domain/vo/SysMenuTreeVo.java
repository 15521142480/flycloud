package com.fly.system.api.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 菜单树型vo
 *
 * @author: lxs
 * @date: 2024/8/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuTreeVo extends SysMenuVo{

    /**
     * 子节点
     */
    private List<SysMenuTreeVo> children;

}
