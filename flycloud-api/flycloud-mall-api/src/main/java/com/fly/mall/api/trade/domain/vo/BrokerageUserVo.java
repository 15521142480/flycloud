package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 分销用户 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BrokerageUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long bindUserId;

    private LocalDateTime bindUserTime;

    private Boolean brokerageEnabled;

    private LocalDateTime brokerageTime;

    private Integer brokeragePrice;

    private Integer frozenPrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
