package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章分类 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ArticleCategoryBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "picUrl")
    private String picUrl;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "sort")
    private Integer sort;

}
