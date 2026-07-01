package com.fly.system.api.pay.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付渠道视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayChannelVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
    private Integer status;
    private Double feeRate;
    private String remark;
    private Long appId;
    private String config;
    private LocalDateTime createTime;
}
