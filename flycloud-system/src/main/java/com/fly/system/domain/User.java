package com.fly.system.domain;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author lxs
 * @date 2023/4/17
 */
@Data
public class User {

    private int uuid;
    private String userName;
    private String nickName;
    private String password;
    private String phone;
    private String Email;
    private int status;
    private int sysType;
    private int tenantId;
    private int isAdmin;
    private int createIp;
    private Date createTime;
    private Date updateTime;


}
