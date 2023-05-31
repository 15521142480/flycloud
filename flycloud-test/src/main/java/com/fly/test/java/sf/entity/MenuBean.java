package com.fly.test.java.sf.entity;

import lombok.Data;

import java.util.List;

/**
 * 菜单-bean
 *
 * @author lxs
 * @date 2023/5/31
 */
@Data
public class MenuBean {

    /**
     * 菜单id
     */
    private String id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单父id (如何没有则为空)
     */
    private String parentId;


    /**
     * 子菜单信息
     */
    private List<MenuBean> childList;

}
