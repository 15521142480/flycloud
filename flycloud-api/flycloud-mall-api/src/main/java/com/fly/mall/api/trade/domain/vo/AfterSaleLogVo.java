package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 售后日志 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AfterSaleLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Integer userType;

    private Long afterSaleId;

    private Integer beforeStatus;

    private Integer afterStatus;

    private Integer operateType;

    private String content;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
