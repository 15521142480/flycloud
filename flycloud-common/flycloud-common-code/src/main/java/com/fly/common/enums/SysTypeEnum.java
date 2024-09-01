package com.fly.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统类型
 *
 * @author lxs
 */
@AllArgsConstructor
@Getter
public enum SysTypeEnum {

    fly_platform(0L, "平台管理系统"),
    fly_business(1L, "商家管理系统"),
    fly_music_platform(2L, "音乐平台管理系统"),
    fly_music_singer(3L, "音乐歌手管理系统")
    ;


    final Long code;
    final String name;


    /**
     * 根据系统类型获取系统名称
     *
     * @param sysType 系统类型
    */
    public static String getSysTypeNameByType (int sysType) {

        String str = "";
        for (SysTypeEnum enums : SysTypeEnum.values()) {
            if (enums.getCode() == sysType) {
                str = enums.getName();
                break;
            }
        }
        return str;
    }

}
