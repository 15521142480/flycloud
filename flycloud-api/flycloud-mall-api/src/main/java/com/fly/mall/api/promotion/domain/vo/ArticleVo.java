package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ArticleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long categoryId;

    private Long spuId;

    private String title;

    private String author;

    private String picUrl;

    private String introduction;

    private Integer browseCount;

    private Integer sort;

    private Integer status;

    private Boolean recommendHot;

    private Boolean recommendBanner;

    private String content;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
