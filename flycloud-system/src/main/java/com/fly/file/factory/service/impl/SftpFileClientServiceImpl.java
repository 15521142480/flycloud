package com.fly.file.factory.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.Sftp;
import com.fly.common.config.properties.FileConfigProperties;
import com.fly.common.utils.FileUtils;
import com.fly.file.factory.service.FileClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * sfp文件服务-实现层
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SftpFileClientServiceImpl implements FileClientService {

    private final FileConfigProperties fileConfigProperties;

    private Sftp sftp;

    static {
        // 某些旧的 sftp 服务器仅支持 ssh-dss 协议，该协议并不安全，默认不支持该协议，按需添加
//        JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-dss");
    }


    @PostConstruct
    protected void doInit() {

        FileConfigProperties.SftpServerConfig config = fileConfigProperties.getSftpServerConfig();

        // 初始化 Sftp 对象
        FtpConfig ftpConfig = new FtpConfig(
                config.getIp(),
                config.getPort(),
                config.getUser(),
                config.getPassword(),
                CharsetUtil.CHARSET_UTF_8);

        this.sftp = new Sftp(ftpConfig);
    }


    @Override
    public String executeUploadFile(byte[] content, String path, String type) {

        // 执行写入
        String filePath = fileConfigProperties.getSftpServerConfig().getBasePath() + File.separator + path;
        String fileName = FileUtil.getName(filePath);
        String dir = StrUtil.removeSuffix(filePath, fileName);
        File file = FileUtils.createTempFile(content);

        reconnectIfTimeout();
        sftp.mkDirs(dir); // 需要创建父目录，不然会报错
        boolean success = sftp.upload(filePath, file);
        if (!success) {
            throw new JschRuntimeException(StrUtil.format("上传文件到目标目录 ({}) 失败", filePath));
        }

        // 删除临时文件夹
        try {
            FileUtils.del(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 拼接返回路径
        return fileConfigProperties.getSftpServerConfig().getBasePath() + path;
    }



    private synchronized void reconnectIfTimeout() {
        sftp.reconnectIfTimeout();
    }




}
