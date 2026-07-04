package com.fly.file.factory.service.impl;

import cn.hutool.core.io.FileUtil;
import com.fly.common.constant.CacheConstants;
import com.fly.common.exception.ServiceException;
import com.fly.common.config.properties.FileConfigProperties;
import com.fly.common.utils.file.FilePathUtils;
import com.fly.file.factory.service.FileClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件服务-实现层
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LocalFileClientServiceImpl implements FileClientService {


    private final FileConfigProperties fileConfigProperties;



    @Override
    public String executeUploadFile(byte[] content, String path, String type) {

        // 执行写入
        String filePath = getFilePath(path);
        FileUtil.writeBytes(content, filePath);

        // 拼接返回路径; 实现方式:
        // Spring Security 放行 /static/**
        // 静态资源映射 /static/** -> basePath

        return fileConfigProperties.getLocalServerConfig().getBaseUrl() + path;
    }



    private String getFilePath(String path) {

        FilePathUtils.validatePath(path);
        Path basePath = Paths.get(fileConfigProperties.getLocalServerConfig().getBasePath()).toAbsolutePath().normalize();
        Path filePath = basePath.resolve(path).normalize();
        if (!filePath.startsWith(basePath)) {
            throw new ServiceException(CacheConstants.FileErrorConstant.FILE_PATH_INVALID.getMsg());
        }
        return filePath.toString();
    }

    /**
     * 格式化文件的 URL 访问地址
     * 使用场景：local、ftp、db，通过 FileController 的 getFile 来获取文件内容
     *
     * @param domain 自定义域名
     * @param path 文件路径
     * @return URL 访问地址
     */
//    protected String formatFileUrl(String domain, String path) {
//        return StrUtil.format("{}/admin-api/file/{}/get/{}", domain, getId(), encodeUrlPath(path));
//    }


}
