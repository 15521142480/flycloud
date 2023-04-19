package com.fly.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author lxs
 * @date 2023/4/17
 */
@Data
@TableName("user")
public class User {

    /**
     * uuid 用户唯一id
     */
    private int uuid;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * email
     */
    private String email;

    private int status;
    private int sysType;
    private int tenantId;
    private int isAdmin;
    private int createIp;
    private Date createTime;
    private Date updateTime;


}
