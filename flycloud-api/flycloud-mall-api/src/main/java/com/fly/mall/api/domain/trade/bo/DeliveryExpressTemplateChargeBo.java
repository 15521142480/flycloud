package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 运费模板计费规则 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryExpressTemplateChargeBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "templateId")
    private Long templateId;

    @Schema(description = "areaIds")
    private List<Integer> areaIds;

    @Schema(description = "chargeMode")
    private Integer chargeMode;

    @Schema(description = "startCount")
    private Double startCount;

    @Schema(description = "startPrice")
    private Integer startPrice;

    @Schema(description = "extraCount")
    private Double extraCount;

    @Schema(description = "extraPrice")
    private Integer extraPrice;

}
