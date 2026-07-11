package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章分类 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ArticleCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private String picUrl;

    private Integer status;

    private Integer sort;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
