package com.fly.mall.api.statistics.domain.bo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 统计时间范围查询对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class StatisticsTimeRangeBo {

    /**
     * 统计时间范围。
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] times;

}
