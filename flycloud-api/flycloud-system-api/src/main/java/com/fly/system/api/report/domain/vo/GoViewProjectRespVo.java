package com.fly.system.api.report.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GoView 项目响应视图对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoViewProjectRespVo extends GoViewProjectVo {
    private static final long serialVersionUID = 1L;

    /**
     * 创建人编号。
     */
    private String creator;
}
