package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_level")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员等级对象", description = "会员等级")
public class MemberLevel extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private Integer level;

    private Integer experience;

    private Integer discountPercent;

    private String icon;

    private String backgroundUrl;

    private Integer status;

}
