package com.fly.file.admin.domain.bo;

import lombok.Data;

/**
 * 连接ftp的参数
 *
 * @author lxs
 * @date 2023/4/24
 */
@Data
public class ConnectFtpBo {

    // 账号
    private String user; // root

    // 主机ip
    private String host; // 47.111.71.133

    // 密码
    private String password; // Wwlxs@#$

    // 端口
    private int port; // 22

    //上传地址
    private String directory; // /project-file

    // 下载目录
    private String saveFile; // /Users/laixueshi/Desktop/


}
