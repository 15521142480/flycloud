package com.fly.system.api.system.path;

import com.fly.common.constant.CommonConstants;

/**
 * 系统api常量
 *
 */
public interface SystemApiPaths {


    /**
     * 远程调用公共前缀；如：/feign + /sys
     */
    String PROVIDER = CommonConstants.FEIGN_API_PREFIX +  "/sys";


    // ================================================== 用户
    /**
     * 根据id查询用户信息
     */
    String PROVIDER_USER_ID = PROVIDER + "/user/id";

    /**
     * 根据ids查询用户信息
     */
    String PROVIDER_USER_IDS = PROVIDER + "/user/ids";

    /**
     * 根据岗位ids查询用户列表
     */
    String PROVIDER_USER_LIST_BY_POST_IDS = PROVIDER + "/user/getListByPostIds";

    /**
     * 根据岗位ids查询用户列表
     */
    String PROVIDER_USER_LIST_BY_ROLE_IDS = PROVIDER + "/user/getListByRoleIds";

    /**
     * 根据username查询用户信息
     */
    String PROVIDER_USER_USERNAME = PROVIDER + "/user/username";

    /**
     * 根据手机号查询用户信息
     */
    String PROVIDER_USER_MOBILE = PROVIDER + "/user/mobile";

    // 根据ids验证用户
    String PROVIDER_USER_VALID_IDS = PROVIDER + "/user/valid/ids";



    // ================================================== 角色

    String PROVIDER_ROLE_VALID_IDS = PROVIDER + "/role/valid/ids";



    // ================================================== 菜单
    /**
     * 根据角色Id查询菜单列表
     */
    String PROVIDER_MENU_BY_ROLE_ID = PROVIDER + "/menu/getListByRoleId";


    // ================================================== 字典
    /**
     * 查询字典值
     */
    String PROVIDER_DICT_VALUE = PROVIDER + "/dict/value";

    /**
     * 查询字典列表
     */
    String PROVIDER_DICT_LIST = PROVIDER + "/dict/list";


    // ================================================== 部门

    // 根据id获取部门信息
    String PROVIDER_DEPT_ID = PROVIDER + "/dept/id";

    // 根据ids获取部门列表
    String PROVIDER_DEPT_IDS = PROVIDER + "/dept/ids";

    // 根据ids验证部门
    String PROVIDER_DEPT_VALID_IDS = PROVIDER + "/dept/valid/ids";

    // 根据ids验证岗位
    String PROVIDER_POST_VALID_IDS = PROVIDER + "/post/valid/ids";



}
