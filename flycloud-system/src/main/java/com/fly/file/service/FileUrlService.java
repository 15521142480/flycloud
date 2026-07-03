package com.fly.file.service;

import cn.hutool.core.util.StrUtil;
import com.fly.common.config.properties.FileConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 文件访问地址处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@RequiredArgsConstructor
public class FileUrlService {

    private static final int LOCAL_SERVER_TYPE = 1;
    private static final int SFTP_SERVER_TYPE = 3;

    private final FileConfigProperties fileConfigProperties;


    /**
     * 获取当前启用文件存储的基础路径。
     */
    public String getBasePath() {
        String basePath = null;
        if (fileConfigProperties.getUseServerType() == LOCAL_SERVER_TYPE
                && fileConfigProperties.getLocalServerConfig() != null) {
            basePath = fileConfigProperties.getLocalServerConfig().getBasePath();
        } else if (fileConfigProperties.getUseServerType() == SFTP_SERVER_TYPE
                && fileConfigProperties.getSftpServerConfig() != null) {
            basePath = fileConfigProperties.getSftpServerConfig().getBasePath();
        }
        if (StrUtil.isBlank(basePath) && fileConfigProperties.getLocalServerConfig() != null) {
            basePath = fileConfigProperties.getLocalServerConfig().getBasePath();
        }
        if (StrUtil.isBlank(basePath) && fileConfigProperties.getSftpServerConfig() != null) {
            basePath = fileConfigProperties.getSftpServerConfig().getBasePath();
        }
        return normalizePath(basePath);
    }

    /**
     * 获取当前启用文件存储的访问基础地址。
     */
    public String getBaseUrl() {
        String baseUrl = null;
        if (fileConfigProperties.getUseServerType() == LOCAL_SERVER_TYPE
                && fileConfigProperties.getLocalServerConfig() != null) {
            baseUrl = fileConfigProperties.getLocalServerConfig().getBaseUrl();
        } else if (fileConfigProperties.getUseServerType() == SFTP_SERVER_TYPE
                && fileConfigProperties.getSftpServerConfig() != null) {
            baseUrl = fileConfigProperties.getSftpServerConfig().getBaseUrl();
        }
        if (StrUtil.isBlank(baseUrl) && fileConfigProperties.getLocalServerConfig() != null) {
            baseUrl = fileConfigProperties.getLocalServerConfig().getBaseUrl();
        }
        if (StrUtil.isBlank(baseUrl) && fileConfigProperties.getSftpServerConfig() != null) {
            baseUrl = fileConfigProperties.getSftpServerConfig().getBaseUrl();
        }
        return normalizePath(baseUrl);
    }

    /**
     * 统一补齐配置路径末尾斜杠。
     */
    private String normalizePath(String path) {
        if (StrUtil.isBlank(path)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.addSuffixIfNot(path, StrUtil.SLASH);
    }

}
