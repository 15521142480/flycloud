package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fly.common.database.web.domain.BaseEntity;
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
    private Long departId;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除
     */
    private String isDeleted;

}
