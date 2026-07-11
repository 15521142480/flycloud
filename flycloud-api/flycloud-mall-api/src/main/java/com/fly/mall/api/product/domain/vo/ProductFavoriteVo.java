package com.fly.mall.api.product.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品收藏 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductFavoriteVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long userId;

    @JsonLongId
    private Long spuId;

    private String spuName;

    private String picUrl;

    private Integer price;

    private Integer salesCount;

    private Integer stock;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
