package com.fly.mall.api.product.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品属性 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductPropertyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private String remark;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
