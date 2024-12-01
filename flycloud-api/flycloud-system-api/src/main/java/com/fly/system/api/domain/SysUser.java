package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import com.fly.common.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * 用户对象 sys_user
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户类型
     */
    private int userType;
    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
     */
    private int loginType;
    /**
     * 昵称
     */
    private String name;
    /**
     * 真名
     */
    private String realName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String telephone;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
