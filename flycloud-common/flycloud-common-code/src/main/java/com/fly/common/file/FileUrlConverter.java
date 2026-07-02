package com.fly.common.file;

import cn.hutool.core.util.StrUtil;
import com.fly.common.config.properties.FileConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 文件展示 URL 和数据库存储 path 的转换器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Component
@RequiredArgsConstructor
public class FileUrlConverter {

    private static final int LOCAL_SERVER_TYPE = 1;
    private static final int SFTP_SERVER_TYPE = 3;

    private final FileConfigProperties fileConfigProperties;


    /**
     * 获取baseUrl
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
        return StrUtil.isBlank(baseUrl) ? StrUtil.EMPTY : StrUtil.addSuffixIfNot(baseUrl, StrUtil.SLASH);
    }


    /**
     * 构建url
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


    /**
     * 值是否是url
     */
    public boolean isAbsoluteUrl(String value) {
        String lowerValue = StrUtil.toString(value).toLowerCase();
        return StrUtil.startWithAny(lowerValue, "http://", "https://", "//", "data:", "blob:");
    }

}
