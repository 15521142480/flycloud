package com.fly.system.api.report.domain.bo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * GoView 项目业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class GoViewProjectBo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "项目名称不能为空")
    private String name;

    private String picUrl;

    private String content;

    /**
     * 发布状态；创建项目时按业务默认未发布，更新时才会使用前端传入值。
     */
    private Integer status;

    private String remark;

}
