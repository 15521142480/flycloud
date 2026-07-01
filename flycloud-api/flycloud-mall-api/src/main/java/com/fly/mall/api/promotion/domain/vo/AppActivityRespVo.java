package com.fly.mall.api.promotion.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端营销活动响应对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppActivityRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer type;

    private String name;

    private Long spuId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
