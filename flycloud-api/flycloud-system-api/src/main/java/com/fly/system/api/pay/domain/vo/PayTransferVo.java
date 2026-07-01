package com.fly.system.api.pay.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付转账单视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayTransferVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String no;
    private Long appId;
    private String channelCode;
    private Long userId;
    private String merchantTransferId;
    private String subject;
    private Integer price;
    private String userAccount;
    private String userName;
    private Integer status;
    private LocalDateTime successTime;
    private LocalDateTime createTime;
}
