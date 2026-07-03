package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 限时折扣活动 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DiscountActivityBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "startTime")
    private LocalDateTime startTime;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

    @Schema(description = "remark")
    private String remark;

    /**
     * 活动商品列表。
     */
    @Schema(description = "products")
    private List<DiscountProductBo> products;

}
