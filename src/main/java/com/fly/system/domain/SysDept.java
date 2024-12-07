package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 部门对象 sys_dept
 *
 * @author fly
 * @date 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 部门id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父部门id
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Long sort;
    /**
     * 负责人
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态（0正常 1停用）
     */
    private Integer status;

}
