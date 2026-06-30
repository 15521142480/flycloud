package com.fly.system.api.report.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * GoView 数据视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class GoViewDataVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> dimensions;

    private List<Map<String, Object>> source;

}
