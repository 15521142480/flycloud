package com.fly.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件工具类
 *
 * @author lxs
 * @date 2023/1/11
 */
@Slf4j
public class FileUtils extends FileUtil {



    /**
     * @description: inputStream 转 MultipartFile
     * @author: lxs
     * @date: 2023/1/11 11:36
     * @param inputStream
     * @param fileName
     * @return: org.springframework.web.multipart.MultipartFile
    */
    public static MultipartFile getMultipartFileByInputStream(InputStream inputStream, String fileName) {

        MultipartFile multipartFile = null;
        try {

            multipartFile = new InputStreamMultipartFile("file", fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    IOUtils.toByteArray(inputStream));

        } catch (Exception e) {
            log.error("=====inputStream转MultipartFile出错!", e);
        }

        return multipartFile;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    private record InputStreamMultipartFile(String name, String originalFilename, String contentType, byte[] content)
            implements MultipartFile {

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            FileUtil.writeBytes(content, dest);
        }

    }

    /**
     * 创建临时文件
     * 该文件会在 JVM 退出时，进行删除
     *
     * @param data 文件内容
     * @return 文件
     */
    @SneakyThrows
    public static File createTempFile(String data) {
        File file = createTempFile();
        // 写入内容
        FileUtil.writeUtf8String(data, file);
        return file;
    }

    /**
     * 创建临时文件
     * 该文件会在 JVM 退出时，进行删除
     *
     * @param data 文件内容
     * @return 文件
     */
    @SneakyThrows
    public static File createTempFile(byte[] data) {
        File file = createTempFile();
        // 写入内容
        FileUtil.writeBytes(data, file);
        return file;
    }

    /**
     * 创建临时文件，无内容
     * 该文件会在 JVM 退出时，进行删除
     *
     * @return 文件
     */
    @SneakyThrows
    public static File createTempFile() {
        // 创建文件，通过 UUID 保证唯一
        File file = File.createTempFile(IdUtil.simpleUUID(), null);
        // 标记 JVM 退出时，自动删除
        file.deleteOnExit();
        return file;
    }

    public static String encodeUrlPath(String path) {
        if (StrUtil.isEmpty(path)) {
            return path;
        }
        String[] segments = path.split(StrUtil.SLASH, -1);
        StringBuilder result = new StringBuilder(path.length());
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                result.append(StrUtil.SLASH);
            }
            result.append(encodeUrlPathSegment(segments[i]));
        }
        return result.toString();
    }

    /**
     * 编码 URL 路径段
     *
     * @param segment URL 路径段
     * @return 编码后的路径段
     */
    public static String encodeUrlPathSegment(String segment) {
        return UriUtils.encodePathSegment(segment, StandardCharsets.UTF_8);
    }

    public static String removeUrlPathQueryAndFragment(String path) {
        if (StrUtil.isEmpty(path)) {
            return path;
        }
        int endIndex = path.length();
        int queryIndex = path.indexOf('?');
        if (queryIndex >= 0) {
            endIndex = queryIndex;
        }
        int fragmentIndex = path.indexOf('#');
        if (fragmentIndex >= 0 && fragmentIndex < endIndex) {
            endIndex = fragmentIndex;
        }
        return path.substring(0, endIndex);
    }

}
