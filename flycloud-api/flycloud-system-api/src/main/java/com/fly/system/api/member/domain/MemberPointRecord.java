package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员积分记录。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("member_point_record")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员积分记录对象", description = "会员积分记录")
public class MemberPointRecord extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private String bizId;

    private Integer bizType;

    private String title;

    private String description;

    private Integer point;

    private Integer totalPoint;

}
