package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付应用视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayAppVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonLongId
    private Long id;
    private String appKey;
    private String name;
    private Integer status;
    private String remark;
    private String orderNotifyUrl;
    private String refundNotifyUrl;
    private String transferNotifyUrl;
    private LocalDateTime createTime;
}
