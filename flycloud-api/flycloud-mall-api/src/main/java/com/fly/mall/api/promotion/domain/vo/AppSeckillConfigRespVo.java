package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端秒杀时间段返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppSeckillConfigRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String startTime;

    private String endTime;

    private List<String> sliderPicUrls;

}
