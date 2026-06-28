package com.fly.mall.api.domain.trade.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 快递公司 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryExpressVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String code;

    private String name;

    private String logo;

    private Integer sort;

    private Integer status;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
