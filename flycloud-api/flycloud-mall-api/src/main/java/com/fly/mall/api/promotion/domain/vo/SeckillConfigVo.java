package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 秒杀时段配置 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class SeckillConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private String startTime;

    private String endTime;

    private List<String> sliderPicUrls;

    private Integer status;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
