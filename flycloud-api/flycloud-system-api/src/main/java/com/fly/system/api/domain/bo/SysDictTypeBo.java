package com.fly.system.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import com.fly.common.domain.BaseEntity;

/**
 * 字典类型业务对象
 *
 * @author fly
 * @date 2024-12-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeBo extends BaseEntity {

    /**
     * 字典主键
     */
    // @NotNull(message = "字典主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 字典名称
     */
    // @NotBlank(message = "字典名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 字典类型
     */
    // @NotBlank(message = "字典类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    // @NotNull(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 备注
     */
    // @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 删除时间
     */
    // @NotNull(message = "删除时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private LocalDateTime deletedTime;


}
