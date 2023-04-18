package com.fly.system.domain.dto;

import com.fly.common.database.domain.dto.PageDto;
import lombok.Data;

/**
 * 用户-dto
 *
 * @author lxs
 * @date 2023/4/18
 */
@Data
public class UserDto extends PageDto {

    private int uuid;
    private String userName;
    private String nickName;
    private String phone;
    private int status;

}
