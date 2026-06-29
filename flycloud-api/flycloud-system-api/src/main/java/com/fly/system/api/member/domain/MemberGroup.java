package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员分组。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("member_group")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员分组对象", description = "会员分组")
public class MemberGroup extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String remark;

    private Integer status;

}
