package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社交用户绑定关系。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("system_social_user_bind")
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialUserBind extends BaseEntity {

    @TableId
    private Long id;

    /**
     * 业务用户编号。
     */
    private Long userId;

    /**
     * 用户类型。
     */
    private Integer userType;

    /**
     * 社交用户编号。
     */
    private Long socialUserId;

    /**
     * 社交平台类型。
     */
    private Integer socialType;
}
