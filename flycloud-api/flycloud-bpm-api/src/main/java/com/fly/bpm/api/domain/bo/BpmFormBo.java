package com.fly.bpm.api.domain.bo;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.List;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 单定义业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmFormBo extends BaseEntity {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 表单名
     */
     @NotBlank(message = "表单名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 开启状态
     */
     @NotNull(message = "开启状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 表单的配置
     */
     @NotBlank(message = "表单的配置不能为空", groups = { AddGroup.class, EditGroup.class })
    private String conf;

    /**
     * 表单项的数组
     */
    @NotNull(message = "表单项的数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private List<String> fields;

    /**
     * 备注
     */
    // @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
