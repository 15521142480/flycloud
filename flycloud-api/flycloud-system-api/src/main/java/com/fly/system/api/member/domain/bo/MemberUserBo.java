package com.fly.system.api.member.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员用户业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员用户业务对象")
public class MemberUserBo extends BaseEntity {

    private Long id;
    private String mobile;
    private String email;
    private Integer status;
    private String nickname;
    private String avatar;
    private String name;
    private Integer sex;
    private String mark;
    private Integer point;
    private String tagIds;
    private Long levelId;
    private Integer experience;
    private Long groupId;

}
