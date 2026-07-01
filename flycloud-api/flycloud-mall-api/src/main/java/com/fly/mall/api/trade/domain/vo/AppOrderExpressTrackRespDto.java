package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 移动端 - 订单物流轨迹响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppOrderExpressTrackRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime time;

    private String content;
}
