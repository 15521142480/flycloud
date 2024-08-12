package com.fly.system.domain.bo;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.fly.common.database.web.domain.BaseEntity;

/**
 * 参数配置业务对象
 *
 * @author fly
 * @date 2024-06-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigBo extends BaseEntity {

    /**
     * 参数主键
     */
    @NotNull(message = "参数主键不能为空", groups = { EditGroup.class })
    private Long configId;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String configName;

    /**
     * 参数键名
     */
    @NotBlank(message = "参数键名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String configKey;

    /**
     * 参数键值
     */
    @NotBlank(message = "参数键值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @NotBlank(message = "系统内置（Y是 N否）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String configType;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
