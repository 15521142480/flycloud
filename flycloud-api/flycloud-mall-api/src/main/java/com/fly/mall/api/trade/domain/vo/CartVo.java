package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 购物车 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CartVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long spuId;

    private Long skuId;

    private Integer count;

    private Boolean selected;

    private String spuName;

    private String picUrl;

    private Integer price;

    private Integer stock;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
