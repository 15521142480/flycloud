package com.fly.system.api.pay.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付示例订单创建请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayDemoOrderCreateReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 示例商品编号。
     */
    @NotNull(message = "商品编号不能为空")
    private Long spuId;

}
