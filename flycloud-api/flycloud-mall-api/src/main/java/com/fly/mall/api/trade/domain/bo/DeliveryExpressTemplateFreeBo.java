package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 运费模板包邮规则 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DeliveryExpressTemplateFreeBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "templateId")
    private Long templateId;

    @Schema(description = "areaIds")
    private List<Integer> areaIds;

    @Schema(description = "freePrice")
    private Integer freePrice;

    @Schema(description = "freeCount")
    private Integer freeCount;

}
