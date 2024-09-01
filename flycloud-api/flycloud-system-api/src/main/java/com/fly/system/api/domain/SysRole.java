package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.fly.common.database.web.domain.BaseEntity;

/**
 * 角色对象 sys_role
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 角色类型（sys_type）
     */
    private Long type;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 描述
     */
    private String remark;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 状态
     */
    private String status;
    /**
     * 删除标识
     */
    private Integer isDeleted;

}
