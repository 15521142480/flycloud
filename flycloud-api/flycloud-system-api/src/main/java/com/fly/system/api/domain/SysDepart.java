package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.database.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织机构表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_depart")
@Schema(name = "SysDepart对象", description = "组织机构表")
public class SysDepart extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String name;
    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;
    /**
     * 删除标识
     */
    @Schema(description = "删除标识")
    private String isDeleted;
    /**
     * 上级ID
     */
    @Schema(description = "上级ID")
    private Long parentId;
    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;

}
