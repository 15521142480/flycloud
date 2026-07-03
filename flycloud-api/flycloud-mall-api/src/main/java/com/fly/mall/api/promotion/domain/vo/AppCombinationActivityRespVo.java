package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端拼团活动列表返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppCombinationActivityRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer userSize;

    private Long spuId;

    private String spuName;

    private String picUrl;

    private Integer marketPrice;

    private Integer combinationPrice;

}
