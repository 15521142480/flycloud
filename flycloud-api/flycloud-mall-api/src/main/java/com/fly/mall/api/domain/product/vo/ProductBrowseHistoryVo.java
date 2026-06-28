package com.fly.mall.api.domain.product.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品浏览记录 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductBrowseHistoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long spuId;

    private Long userId;

    private Boolean userDeleted;

    private String spuName;

    private String picUrl;

    private Integer price;

    private Integer salesCount;

    private Integer stock;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
