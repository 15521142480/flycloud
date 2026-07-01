package com.fly.file.factory.service;

/**
 * 文件 - 接口层
 *
 * @author: lxs
 * @date: 2026/7/1
 */
public interface FileClientService {



    /**
     * 执行文件上传
     *
     * @param content 文件流
     * @param path    相对路径
     * @return 完整路径，即 HTTP 访问地址
     * @throws Exception 上传文件时，抛出 Exception 异常
     */
    String executeUploadFile(byte[] content, String path, String type) throws Exception;

}
