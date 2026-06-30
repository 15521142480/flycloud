package com.fly.system.api.report.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * GoView SQL 数据查询业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class GoViewDataGetBySqlBo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "SQL 不能为空")
    private String sql;

}
