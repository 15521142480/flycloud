package com.fly.system.api.report.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * GoView 项目业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class GoViewProjectBo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "项目名称不能为空")
    private String name;

    private String picUrl;

    private String content;

    @NotNull(message = "发布状态不能为空")
    private Integer status;

    private String remark;

}
