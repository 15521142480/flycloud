package com.fly.bpm.api.domain.bo;

import com.fly.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * BPM 用户组业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
public class BpmUserGroupBo {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 组名
     */
    // @NotBlank(message = "组名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 描述
     */
    // @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 成员编号数组
     */
    // @NotBlank(message = "成员编号数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userIds;

    /**
     * 状态（0正常 1停用）
     */
    // @NotNull(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

}
