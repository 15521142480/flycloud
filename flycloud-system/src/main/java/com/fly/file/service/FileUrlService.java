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
     * 获取baseUrl；如 http://localhost:8080/static/
     *
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

        return normalizeBaseUrl(baseUrl);
    }

    
    /**
     * 构建url
     * 
     * @param path
    */
    public String buildUrl(String path) {
        if (StrUtil.isBlank(path) || isAbsoluteUrl(path)) {
            return path;
        }
        return getBaseUrl() + StrUtil.removePrefix(path, StrUtil.SLASH);
    }

    public String toPath(String urlOrPath) {
        if (StrUtil.isBlank(urlOrPath)) {
            return urlOrPath;
        }
        String baseUrl = getBaseUrl();
        if (StrUtil.isNotBlank(baseUrl) && StrUtil.startWith(urlOrPath, baseUrl)) {
            return StrUtil.removePrefix(urlOrPath, baseUrl);
        }
        return urlOrPath;
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (StrUtil.isBlank(baseUrl)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.addSuffixIfNot(baseUrl, StrUtil.SLASH);
    }

    
    /**
     * 判断值是否是url
     * 
     * @param value
    */
    private boolean isAbsoluteUrl(String value) {
        String lowerValue = StrUtil.toString(value).toLowerCase();
        return StrUtil.startWithAny(lowerValue, "http://", "https://", "//", "data:", "blob:");
    }

}
