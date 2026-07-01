package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会员用户。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("member_user")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员用户对象", description = "会员用户")
public class MemberUser extends BaseEntity {

    @TableId
    private Long id;

    private String mobile;

    private String email;

    private String password;

    private Integer status;

    private String registerIp;

    private Integer registerTerminal;

    private String loginIp;

    private LocalDateTime loginDate;

    private String nickname;

    private String avatar;

    private String name;

    private Integer sex;

    private LocalDateTime birthday;

    private Integer areaId;

    private String mark;

    private Integer point;

    private String tagIds;

    private Long levelId;

    private Integer experience;

    private Long groupId;

}
