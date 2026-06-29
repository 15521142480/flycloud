package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员配置。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_config")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员配置对象", description = "会员配置")
public class MemberConfig extends BaseEntity {

    @TableId
    private Long id;

    private Boolean pointTradeDeductEnable;

    private Integer pointTradeDeductUnitPrice;

    private Integer pointTradeDeductMaxPrice;

    private Integer pointTradeGivePoint;

}
