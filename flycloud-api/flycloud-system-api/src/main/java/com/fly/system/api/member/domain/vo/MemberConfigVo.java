package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员配置视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberConfigVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Boolean pointTradeDeductEnable;
    private Integer pointTradeDeductUnitPrice;
    private Integer pointTradeDeductMaxPrice;
    private Integer pointTradeGivePoint;
    private Boolean isDeleted;
    private LocalDateTime createTime;
}
