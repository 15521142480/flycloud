package com.fly.system.api.member.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员配置业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员配置业务对象")
public class MemberConfigBo extends BaseEntity {

    private Long id;
    private Boolean pointTradeDeductEnable;
    private Integer pointTradeDeductUnitPrice;
    private Integer pointTradeDeductMaxPrice;
    private Integer pointTradeGivePoint;

}
