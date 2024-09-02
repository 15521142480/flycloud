package com.fly.auth.domain.vo;

import com.fly.system.api.domain.vo.SysMenuTreeVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户登录信息 - vo
 *
 * @author: lxs
 * @date: 2024/9/1
 */
@Data
public class UserLoginVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * jti
     */
    private String jti;


    // ================================================================================================

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * 用户名称
     */
    private String userName;


    /**
     * 用户头像
     */
    private String avatar;


    // ================================================================================================
    /**
     * 系统权限标识组id
     */
    private String roleIds;


}
