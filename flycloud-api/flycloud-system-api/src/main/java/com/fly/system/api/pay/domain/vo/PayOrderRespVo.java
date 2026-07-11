package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付订单返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayOrderRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long appId;

    @JsonLongId
    private Long channelId;

    private String channelCode;

    @JsonLongId
    private Long userId;

    private Integer userType;

    private String merchantOrderId;

    private String subject;

    private String body;

    private Integer price;

    private Integer status;

    private String userIp;

    private LocalDateTime expireTime;

    private LocalDateTime successTime;

    @JsonLongId
    private Long extensionId;

    private String no;

    private Integer refundPrice;

    private String channelUserId;

    private String channelOrderNo;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
