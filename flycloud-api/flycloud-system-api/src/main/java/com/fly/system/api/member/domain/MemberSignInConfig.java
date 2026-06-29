package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员签到规则。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_sign_in_config")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员签到规则对象", description = "会员签到规则")
public class MemberSignInConfig extends BaseEntity {

    @TableId
    private Long id;

    private Integer day;

    private Integer point;

    private Integer experience;

    private Integer status;

}
