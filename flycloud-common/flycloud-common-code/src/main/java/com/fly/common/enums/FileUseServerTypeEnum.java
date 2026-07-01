package com.fly.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件服务器类型枚举
 *
 * @author lxs
 * @date 2026/07/01
 */
@Getter
@AllArgsConstructor
public enum FileUseServerTypeEnum {


    LOCAL(1, "本地服务"),
    FTP(2, "ftp服务器"),
    SFTP(3, "sftp服务器"),
    OSS(4, "阿里云obs对象存储"),
    OBS(5, "华为云obs对象存储"),
    SMB(9, "smb共享服务器"),
    ;


    final int serverType;
    final String serverName;


    /**
     * 获取服务类型名称
     */
    public static String getServerName(int serverType) {

        String serverName = "";
        for (FileUseServerTypeEnum enums : FileUseServerTypeEnum.values()) {
            if (enums.getServerType() == serverType) {
                serverName = enums.getServerName();
                break;
            }
        }

        return serverName;
    }

    /**
     * 根据类型获取枚举
     */
    public static FileUseServerTypeEnum getEnum(int serverType) {

        for (FileUseServerTypeEnum enums : FileUseServerTypeEnum.values()) {
            if (enums.getServerType() == serverType) {
                return enums;
            }
        }

        return null;
    }


}
