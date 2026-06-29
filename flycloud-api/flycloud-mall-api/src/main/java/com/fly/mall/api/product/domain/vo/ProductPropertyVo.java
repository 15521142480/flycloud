package com.fly.mall.api.product.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品属性 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductPropertyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String remark;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
