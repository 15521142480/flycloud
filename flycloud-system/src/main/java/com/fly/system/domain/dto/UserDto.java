package com.fly.system.domain.dto;

import com.fly.common.database.domain.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户-dto
 *
 * @author lxs
 * @date 2023/4/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends PageDto {

    /**
     * uuid 用户唯一id
     */
    private int uuid;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 状态
     */
    private int status;

}
