package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员经验记录。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("member_experience_record")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员经验记录对象", description = "会员经验记录")
public class MemberExperienceRecord extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Integer bizType;

    private String bizId;

    private String title;

    private String description;

    private Integer experience;

    private Integer totalExperience;

}
