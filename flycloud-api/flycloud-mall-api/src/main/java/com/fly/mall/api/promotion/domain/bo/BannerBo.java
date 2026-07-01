package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 轮播图 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BannerBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "title")
    private String title;

    @Schema(description = "url")
    private String url;

    @Schema(description = "picUrl")
    private String picUrl;

    @Schema(description = "sort")
    private Integer sort;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "position")
    private Integer position;

    @Schema(description = "memo")
    private String memo;

    @Schema(description = "browseCount")
    private Integer browseCount;

}
