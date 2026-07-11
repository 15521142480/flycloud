package com.fly.system.api.report.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GoView 项目视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class GoViewProjectVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private String picUrl;

    private String content;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

}
