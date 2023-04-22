package com.fly.system.domain.bo;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.fly.common.database.web.domain.BaseEntity;

/**
 * 用户信息业务对象
 *
 * @author fly
 * @date 2023-04-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserBo extends BaseEntity {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { EditGroup.class })
    private Long userId;

    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deptId;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    @NotBlank(message = "用户类型（sys_user系统用户）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userType;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "用户邮箱不能为空", groups = { AddGroup.class, EditGroup.class })
    private String email;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @NotBlank(message = "用户性别（0男 1女 2未知）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sex;

    /**
     * 头像地址
     */
    @NotBlank(message = "头像地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String avatar;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @NotBlank(message = "帐号状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 最后登录IP
     */
    @NotBlank(message = "最后登录IP不能为空", groups = { AddGroup.class, EditGroup.class })
    private String loginIp;

    /**
     * 最后登录时间
     */
    @NotNull(message = "最后登录时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date loginDate;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
