package com.fly.system.api.constants;

/**
 * 系统常量
 *
 */
public interface SystemFeignApiConstants {


    /**
     * 远程调用公共前缀
     */
    String PROVIDER = "/feign/sys";


    // ================================================== 用户
    /**
     * 根据id查询用户信息
     */
    String PROVIDER_USER_ID = PROVIDER + "/user/id";

    /**
     * 根据username查询用户信息
     */
    String PROVIDER_USER_IDS = PROVIDER + "/user/ids";

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



    // ================================================== 菜单角色
    /**
     * 根据roleId查询menuId列表
     */
    String PROVIDER_ROLE_PERMISSION = PROVIDER + "/role-permission/permission";



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



}
