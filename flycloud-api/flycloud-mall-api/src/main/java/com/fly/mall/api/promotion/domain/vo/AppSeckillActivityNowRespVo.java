package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端当前秒杀活动返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppSeckillActivityNowRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private AppSeckillConfigRespVo config;

    private List<AppSeckillActivityRespVo> activities;

}
