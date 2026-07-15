package com.fly.common.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件配置
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@Data
@ConfigurationProperties(prefix = "flycloud.upload.config")
public class FileConfigProperties {

    /**
     * 使用的服务器类型 (1:本地服务, 2:ftp服务器, 3:sftp服务器, 4:阿里云oss对象存储, 5:华为云obs对象存储, 9:smb共享文件夹)
     */
    private int useServerType;


    /**
     * sftp服务器配置
     */
    private LocalServerConfig localServerConfig;


    /**
     * sftp服务器配置
     */
    private SftpServerConfig sftpServerConfig;


    @Data
    @NoArgsConstructor
    public static class LocalServerConfig {

        // 文件上传和读取的前缀真实路径；如 /Users/lxs/applicationFile/file/person/flycloud-server/
        private String basePath;

        // 拼给前端或客户端展示的前缀url路径；如http://localhost:8080/static/
        private String baseUrl;
    }


    @Data
    @NoArgsConstructor
    public static class SftpServerConfig {

        // (ip、用户名、密码、端口、上传文件夹的前缀)
        private String ip;
        private String user;
        private String password;
        private int port;
        private String basePath;
        private String baseUrl;
    }

}
