package com.fly.mall.api.domain.trade.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 佣金记录 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BrokerageRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String bizId;

    private Integer bizType;

    private String title;

    private String description;

    private Integer price;

    private Integer totalPrice;

    private Integer status;

    private Integer frozenDays;

    private LocalDateTime unfreezeTime;

    private Integer sourceUserLevel;

    private Long sourceUserId;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
