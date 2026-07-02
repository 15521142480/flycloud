package com.fly.file.service;

import com.fly.system.api.file.domain.vo.FileUploadRespVo;

/**
 * 文件服务 - 接口层
 *
 * @author: lxs
 * @date: 2026/7/1
 */
public interface FileService {


    /**
     * 上传并保存文件，最后返回文件访问信息
     *
     * @param content
     * @param originalFilename
     * @param directory
     * @param contentType
    */
    FileUploadRespVo uploadFile(byte[] content, String originalFilename, String directory, String contentType) throws Exception;

}
