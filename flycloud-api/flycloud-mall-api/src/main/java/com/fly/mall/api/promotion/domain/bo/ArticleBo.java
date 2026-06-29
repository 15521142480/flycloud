package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ArticleBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "categoryId")
    private Long categoryId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "title")
    private String title;

    @Schema(description = "author")
    private String author;

    @Schema(description = "picUrl")
    private String picUrl;

    @Schema(description = "introduction")
    private String introduction;

    @Schema(description = "browseCount")
    private Integer browseCount;

    @Schema(description = "sort")
    private Integer sort;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "recommendHot")
    private Boolean recommendHot;

    @Schema(description = "recommendBanner")
    private Boolean recommendBanner;

    @Schema(description = "content")
    private String content;

}
