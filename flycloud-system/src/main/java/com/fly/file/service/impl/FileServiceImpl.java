package com.fly.file.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.file.factory.FileClientFactory;
import com.fly.file.factory.service.FileClientService;
import com.fly.common.utils.file.FilePathUtils;
import com.fly.common.utils.file.FileTypeUtils;
import com.fly.file.mapper.FileMapper;
import com.fly.file.service.FileService;
import com.fly.system.api.file.domain.File;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.PURE_DATE_PATTERN;

/**
 * 文件服务-实现类
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    // 上传文件的前缀，是否包含日期（yyyyMMdd）; 目的：按照日期，进行分目录
    static boolean PATH_PREFIX_DATE_ENABLE = true;

    // 上传文件的后缀，是否启用
    // 算法：当前时间戳（毫秒）+ 5 位随机数；目的是保证文件的唯一性，避免覆盖
    // 定制：可按需调整成 UUID、或者其他方式
    static boolean PATH_SUFFIX_TIMESTAMP_ENABLE = true;

    // 后缀是否作为上级目录; true保留原文件名, false:后缀拼到文件名
    static boolean PATH_SUFFIX_AS_DIRECTORY = false;

    // 上面三个属性如果设置为：true、true、false，推荐使用，则文件路径为：/20260701/xxx_178290301301859703.png
    // 上面三个属性如果设置为：true、true、true，则文件路径为：/20260701/178290301301859703/xxx.png
    // 上面三个属性如果设置为：true、false、false，则文件路径为：/20260701/xxx.png

    private final FileMapper fileMapper;


    @Override
    public String uploadFile(byte[] content, String originalFilename, String directory, String type) throws Exception {

        // 1.1 处理 name 的合法性，禁止携带目录路径
        String name = FilePathUtils.validateFileName(originalFilename);

        // 1.2.1 处理 type 为空的情况
        if (StrUtil.isEmpty(type)) {
            type = FileTypeUtils.getMineType(content, name);
        }
        // 1.2.2 处理 name 为空的情况
        if (StrUtil.isEmpty(name)) {
            name = DigestUtil.sha256Hex(content);
        }
        if (StrUtil.isEmpty(FileUtil.extName(name))) {
            // 如果 name 没有后缀 type，则补充后缀
            String extension = FileTypeUtils.getExtension(type);
            if (StrUtil.isNotEmpty(extension)) {
                name = name + extension;
            }
        }

        // 1.3 生成上传的 path，需要保证唯一
        String path = generateUploadPath(name, directory);

        // 2 上传到文件存储器
//        FileClient client = fileConfigService.getMasterFileClient();
//        Assert.notNull(client, "客户端(master) 不能为空");
//        String url = client.upload(content, path, type);

        FileClientService fileClientService = FileClientFactory.getFileService();
        String url = fileClientService.executeUploadFile(content, path, type);

        // 3 保存到数据库
        File file = new File();
        file.setConfigId(1L)
//                .setConfigId(client.getId())
                .setName(name)
                .setPath(path)
                .setUrl(url)
                .setType(type)
                .setSize((long) content.length)
                .setCreateBy(String.valueOf(UserUtils.getCurUserId()))
                .setCreateTime(LocalDateTime.now())
                .setUpdateBy(String.valueOf(UserUtils.getCurUserId()))
                .setUpdateTime(LocalDateTime.now());
        fileMapper.insert(file);

        return url;
    }


    /**
     * 生成上传路径
     *
     * @param name
     * @param directory
    */
    @VisibleForTesting
    String generateUploadPath(String name, String directory) {

        // 1.1 处理 name 和 directory 的合法性
        name = FilePathUtils.validateFileName(name);
        FilePathUtils.validatePath(name);
        FilePathUtils.validateDirectory(directory);
        // 1.2 生成前缀、后缀
        String prefix = null;
        if (PATH_PREFIX_DATE_ENABLE) {
            prefix = LocalDateTimeUtil.format(LocalDateTimeUtil.now(), PURE_DATE_PATTERN);
        }
        String suffix = null;
        if (PATH_SUFFIX_TIMESTAMP_ENABLE) {
            // 5 位随机数，避免同一毫秒内的重复
            suffix = String.valueOf(System.currentTimeMillis()) + RandomUtil.randomInt(10000, 100000);
        }

        // 2.1 先拼接 suffix 后缀
        if (StrUtil.isNotEmpty(suffix)) {
            if (PATH_SUFFIX_AS_DIRECTORY) {
                name = suffix + StrUtil.SLASH + name;
            } else {
                String ext = FileUtil.extName(name);
                if (StrUtil.isNotEmpty(ext)) {
                    name = FileUtil.mainName(name) + StrUtil.C_UNDERLINE + suffix + StrUtil.DOT + ext;
                } else {
                    name = name + StrUtil.C_UNDERLINE + suffix;
                }
            }
        }
        // 2.2 再拼接 prefix 前缀
        if (StrUtil.isNotEmpty(prefix)) {
            name = prefix + StrUtil.SLASH + name;
        }
        // 2.3 最后拼接 directory 目录
        if (StrUtil.isNotEmpty(directory)) {
            name = directory + StrUtil.SLASH + name;
        }
        return name;
    }

}
