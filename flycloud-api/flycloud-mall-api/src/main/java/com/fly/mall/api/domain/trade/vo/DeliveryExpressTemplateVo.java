package com.fly.mall.api.domain.trade.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 运费模板 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryExpressTemplateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer chargeMode;

    private Integer sort;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
