package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 佣金记录 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BrokerageRecordBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "bizId")
    private String bizId;

    @Schema(description = "bizType")
    private Integer bizType;

    @Schema(description = "title")
    private String title;

    @Schema(description = "description")
    private String description;

    @Schema(description = "price")
    private Integer price;

    @Schema(description = "totalPrice")
    private Integer totalPrice;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "frozenDays")
    private Integer frozenDays;

    @Schema(description = "unfreezeTime")
    private LocalDateTime unfreezeTime;

    @Schema(description = "sourceUserLevel")
    private Integer sourceUserLevel;

    @Schema(description = "sourceUserId")
    private Long sourceUserId;

}
