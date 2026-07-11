package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端积分商城活动列表返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppPointActivityRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long spuId;

    private Integer status;

    private Integer stock;

    private Integer totalStock;

    private String spuName;

    private String picUrl;

    private Integer marketPrice;

    private Integer point;

    private Integer price;

}
