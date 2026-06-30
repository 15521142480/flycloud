package com.fly.system.api.report.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GoView 项目。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "report_go_view_project", autoResultMap = true)
@Schema(name = "GoView 项目对象", description = "GoView 项目")
public class GoViewProject extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String picUrl;

    private String content;

    private Integer status;

    private String remark;

}
