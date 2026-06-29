package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员标签。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_tag")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员标签对象", description = "会员标签")
public class MemberTag extends BaseEntity {

    @TableId
    private Long id;

    private String name;

}
