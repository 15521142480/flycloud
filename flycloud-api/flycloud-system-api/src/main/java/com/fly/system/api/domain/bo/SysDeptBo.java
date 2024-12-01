package com.fly.system.api.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门-bo
 *
 * @author: lxs
 * @date: 2024/11/29
 */
@Data
public class SysDeptBo extends BaseEntity {

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "研发部")
    private String name;

    @Schema(description = "父部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long parentId;

    @Schema(description = "负责人的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long leaderUserId;

    @Schema(description = "部门状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status; // 参见 CommonStatusEnum 枚举

    private String code;

    /**
     * 显示顺序
     */
    // @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 联系电话
     */
    // @NotBlank(message = "联系电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 邮箱
     */
    // @NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, EditGroup.class })
    private String email;

}
