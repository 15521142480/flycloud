package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 佣金记录 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BrokerageRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
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

    @JsonLongId
    private Long sourceUserId;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
