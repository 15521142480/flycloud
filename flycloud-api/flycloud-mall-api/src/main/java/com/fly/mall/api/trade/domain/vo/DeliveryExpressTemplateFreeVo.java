package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 运费模板包邮规则 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DeliveryExpressTemplateFreeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long templateId;

    private List<Integer> areaIds;

    private Integer freePrice;

    private Integer freeCount;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
