package com.fly.system.service;

import com.fly.common.database.domain.vo.PageVo;
import com.fly.system.domain.dto.UserDto;
import com.fly.system.domain.vo.UserVo;

import java.util.List;

/**
 * 测试-业务接口层
 *
 * @author lxs
 * @date 2023/4/17
 */
public interface TestService {



    PageVo<UserVo> getUserPageList(UserDto userDto);


    List<UserVo> getUserList();


    UserVo getUserInfo(int uuid);


}
