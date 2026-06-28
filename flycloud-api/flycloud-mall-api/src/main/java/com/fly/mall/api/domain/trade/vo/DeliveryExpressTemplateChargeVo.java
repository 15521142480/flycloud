package com.fly.mall.api.domain.trade.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 运费模板计费规则 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryExpressTemplateChargeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long templateId;

    private List<Integer> areaIds;

    private Integer chargeMode;

    private Double startCount;

    private Integer startPrice;

    private Double extraCount;

    private Integer extraPrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
