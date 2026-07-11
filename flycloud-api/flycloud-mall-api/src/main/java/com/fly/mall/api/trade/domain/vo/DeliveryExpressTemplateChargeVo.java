package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 运费模板计费规则 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DeliveryExpressTemplateChargeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long templateId;

    @JsonLongId
    private List<Integer> areaIds;

    private Integer chargeMode;

    private Double startCount;

    private Integer startPrice;

    private Double extraCount;

    private Integer extraPrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
