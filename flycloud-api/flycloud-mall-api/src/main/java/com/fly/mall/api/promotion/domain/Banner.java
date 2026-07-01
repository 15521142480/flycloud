package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_banner")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "轮播图对象", description = "轮播图表")
public class Banner extends BaseEntity {

    @TableId
    private Long id;

    private String title;

    private String url;

    private String picUrl;

    private Integer sort;

    private Integer status;

    private Integer position;

    private String memo;

    private Integer browseCount;

}
