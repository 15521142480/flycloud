package com.fly.mall.api.product.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品属性值 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductPropertyValueVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long propertyId;

    private String name;

    private String remark;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
