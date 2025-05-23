package com.fly.system.api.domain.vo;

import java.io.Serializable;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 用户视图对象
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@ExcelIgnoreUnannotated
public class SysUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 账号
     */
    @ExcelProperty(value = "账号")
    private String account;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    private String password;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * 用户类型名称
     */
    private String userTypeName;

    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
     */
    private int loginType;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    private String name;

    /**
     * 真名
     */
    @ExcelProperty(value = "真名")
    private String realName;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    private String avatar;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String email;

    /**
     * 手机
     */
    @ExcelProperty(value = "手机")
    private String telephone;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日")
    private Date birthday;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    private Integer sex;

    /**
     * 部门id
     */
    @ExcelProperty(value = "部门id")
    private Long deptId;

    /**
     * 部门id
     */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Integer status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 逻辑删除
     */
    @ExcelProperty(value = "逻辑删除")
    private String isDeleted;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 用户角色
     */
    @ExcelProperty(value = "用户角色")
    private String roleIds;


    /**
     * 用户角色名称
     */
    @ExcelProperty(value = "用户角色名称")
    private String roleNames;


}
