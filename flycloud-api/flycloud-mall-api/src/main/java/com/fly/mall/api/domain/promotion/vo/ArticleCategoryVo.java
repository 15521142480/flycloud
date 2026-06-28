package com.fly.mall.api.domain.promotion.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章分类 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ArticleCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String picUrl;

    private Integer status;

    private Integer sort;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
