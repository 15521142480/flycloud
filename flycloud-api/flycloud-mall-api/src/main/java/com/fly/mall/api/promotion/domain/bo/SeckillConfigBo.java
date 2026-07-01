package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 秒杀时段配置 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class SeckillConfigBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "startTime")
    private String startTime;

    @Schema(description = "endTime")
    private String endTime;

    @Schema(description = "sliderPicUrls")
    private List<String> sliderPicUrls;

    @Schema(description = "status")
    private Integer status;

}
