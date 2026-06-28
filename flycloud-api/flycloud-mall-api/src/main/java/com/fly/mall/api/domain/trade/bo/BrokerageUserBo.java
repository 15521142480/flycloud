package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 分销用户 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BrokerageUserBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "bindUserId")
    private Long bindUserId;

    @Schema(description = "bindUserTime")
    private LocalDateTime bindUserTime;

    @Schema(description = "brokerageEnabled")
    private Boolean brokerageEnabled;

    @Schema(description = "brokerageTime")
    private LocalDateTime brokerageTime;

    @Schema(description = "brokeragePrice")
    private Integer brokeragePrice;

    @Schema(description = "frozenPrice")
    private Integer frozenPrice;

}
