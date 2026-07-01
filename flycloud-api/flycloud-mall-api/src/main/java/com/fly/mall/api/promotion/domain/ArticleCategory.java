package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章分类 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_article_category")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "文章分类对象", description = "文章分类表")
public class ArticleCategory extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String picUrl;

    private Integer status;

    private Integer sort;

}
