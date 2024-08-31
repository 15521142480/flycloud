package com.fly.system.api.domain.bo;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.fly.common.database.web.domain.BaseEntity;

/**
 * 用户业务对象
 *
 * @author fly
 * @date 2024-08-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 真名
     */
    @NotBlank(message = "真名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String realName;

    /**
     * 头像
     */
    @NotBlank(message = "头像不能为空", groups = { AddGroup.class, EditGroup.class })
    private String avatar;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, EditGroup.class })
    private String email;

    /**
     * 手机
     */
    @NotBlank(message = "手机不能为空", groups = { AddGroup.class, EditGroup.class })
    private String telephone;

    /**
     * 生日
     */
    @NotNull(message = "生日不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date birthday;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer sex;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long departId;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 逻辑删除
     */
    @NotBlank(message = "逻辑删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isDeleted;


}
