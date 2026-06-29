package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 轮播图 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BannerVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String url;

    private String picUrl;

    private Integer sort;

    private Integer status;

    private Integer position;

    private String memo;

    private Integer browseCount;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
