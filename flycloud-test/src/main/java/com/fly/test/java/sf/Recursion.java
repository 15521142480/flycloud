package com.fly.test.java.sf;

import com.fly.test.java.sf.entity.MenuBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 递归
 *
 * @author lxs
 * @date 2023/5/31
 */
public class Recursion {


    public static void main(String[] args) {


        // 获取菜单递归信息
        List<MenuBean> recursionList = getMenuRecursion();
        System.out.println("菜单递归数据:" + recursionList);

        // 菜单递归数据转数组数据
        List<MenuBean> menuBeanList = getMenuListByRecursion(recursionList);
        System.out.println("菜单数组数据:" + menuBeanList);

    }


    /**
     * 根据递归获取数组信息
     */
    public static List<MenuBean> getMenuListByRecursion(List<MenuBean> recursionList) {

        // 结果(递归)数据
        List<MenuBean> resultList = new ArrayList<>();

        handleMenuBeanList(resultList, recursionList);

        return resultList;
    }

    public static void handleMenuBeanList(List<MenuBean> resultList, List<MenuBean> recursionList) {

        for (MenuBean menuBean : recursionList) {

            resultList.add(menuBean);
            List<MenuBean> menuBeanList2 = menuBean.getChildList();
            if (menuBeanList2 != null && menuBeanList2.size() > 0) {
                handleMenuBeanList(resultList, menuBeanList2);
            }
        }
    }



    /**
     * 获取菜单递归信息
     */
    public static List<MenuBean> getMenuRecursion() {

        // 数据赋值
        List<MenuBean> menuBeanList = setMenuBeanData();

        // 结果(递归)数据
        List<MenuBean> resultList = new ArrayList<>();

        // 定义pidMap 装载某个父id的所有仅下一级的id信息
        Map<String, List<MenuBean>> pidMap = new HashMap<>();

        // 赋值pidMap
        for (MenuBean menuBean : menuBeanList) {

            String parentId = menuBean.getParentId();
            if (pidMap.get(parentId) == null) {
                pidMap.put(parentId, new ArrayList<>());
            }
            pidMap.get(parentId).add(menuBean);
        }

        // 数据
        for (MenuBean menuBean : menuBeanList) {

            // todo 使用查找到最大的一级去递归遍历 (使用最大的一级才有下一级的逻辑)
            if (menuBean.getParentId() == null) {
                menuBean.setChildList(handleChildMenuBeanList(menuBean.getId(), pidMap));
                resultList.add(menuBean);
            }
        }

        return resultList;
    }


    /**
     * 处理子菜单数据
     */
    public static List<MenuBean> handleChildMenuBeanList(String id, Map<String, List<MenuBean>> pidMap) {

        // 当前id的下有无其下一级的信息, 当前id即就是pidMap的pid, 其pid存放的下一级的信息list
        List<MenuBean> menuBeanList = pidMap.get(id);
        if (menuBeanList != null && menuBeanList.size() > 0) {
            for (MenuBean menuBean : menuBeanList) {
                menuBean.setChildList(handleChildMenuBeanList(menuBean.getId(), pidMap));
            }
        }

        return menuBeanList;
    }


    /**
     * 菜单数组赋值
     */
    public static List<MenuBean> setMenuBeanData() {

        List<MenuBean> menuBeanList = new ArrayList<>();

        MenuBean menuBean1 = new MenuBean();
        MenuBean menuBean2 = new MenuBean();
        MenuBean menuBean3 = new MenuBean();
        MenuBean menuBean4 = new MenuBean();
        MenuBean menuBean5 = new MenuBean();
        MenuBean menuBean6 = new MenuBean();

        menuBean1.setId("1");
        menuBean1.setName("系统管理");
        menuBean1.setParentId(null);

        menuBean2.setId("2");
        menuBean2.setName("商城管理");
        menuBean2.setParentId(null);

        menuBean3.setId("1.1");
        menuBean3.setName("账号管理");
        menuBean3.setParentId("1");

        menuBean4.setId("1.2");
        menuBean4.setName("菜单管理");
        menuBean4.setParentId("1");

        menuBean5.setId("1.2.1");
        menuBean5.setName("菜单按钮管理");
        menuBean5.setParentId("1.2");

        menuBean6.setId("2.1");
        menuBean6.setName("商品管理");
        menuBean6.setParentId("2");


        menuBeanList.add(menuBean1);
        menuBeanList.add(menuBean2);
        menuBeanList.add(menuBean3);
        menuBeanList.add(menuBean4);
        menuBeanList.add(menuBean5);
        menuBeanList.add(menuBean6);
        return menuBeanList;
    }




}
