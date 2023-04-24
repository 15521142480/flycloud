package com.fly.file.admin.domain.bo;

import lombok.Data;

/**
 * 执行操作bo
 *
 * @author lxs
 * @date 2023/4/23
 */
@Data
public class ExecuteOptionBo {

    /**
     * 服务器当前路径
     */
    private String curPath;

    /**
     * 操作类型 (0:新建文件夹, 1:启动服务, 2:关闭服务, 3:文件/夹重命名, 4:删除文件/夹, 5:压缩文件/夹, 6:解压文件/夹, 10:执行文件)
     */
    private String type;

    /**
     * 新建文件夹名称
     */
    private String executeFileName;

    /**
     * 执行的文件名
     */
    private String newFolderName;

    /**
     * 旧文件名名称
     */
    private String oldFileName;

    /**
     * 新文件名名称
     */
    private String newFileName;

    /**
     * 删除文件名名称
     */
    private String deleteFileName;

    /**
     *  要压缩/解压文件/夹名称
     */
    private String ysOrJyFileName;

    /**
     * 压缩成包的名称
     */
    private String ysToFileName;
}
