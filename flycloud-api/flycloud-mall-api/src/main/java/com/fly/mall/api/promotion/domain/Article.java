package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_article")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文章对象", description = "文章表")
public class Article extends BaseEntity {

    @TableId
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

}
