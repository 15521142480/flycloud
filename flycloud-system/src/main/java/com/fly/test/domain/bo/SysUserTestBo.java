package com.fly.test.domain.bo;

import lombok.Data;

/**
 * 用户信息业务对象
 *
 * @author fly
 * @date 2023-04-22
 */

@Data
public class SysUserTestBo {

    /**
     * 用户ID
     */
//    @NotNull(message = "用户ID不能为空", groups = { EditGroup.class })
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;



}
