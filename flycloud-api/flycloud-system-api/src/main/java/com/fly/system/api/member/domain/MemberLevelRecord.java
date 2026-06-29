package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级记录。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_level_record")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员等级记录对象", description = "会员等级记录")
public class MemberLevelRecord extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Long levelId;

    private Integer level;

    private Integer discountPercent;

    private Integer experience;

    private Integer userExperience;

    private String remark;

    private String description;

}
