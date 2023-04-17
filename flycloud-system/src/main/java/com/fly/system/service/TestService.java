package com.fly.system.service;

import com.fly.system.domain.User;

import java.util.List;

/**
 * 测试-业务接口层
 *
 * @author lxs
 * @date 2023/4/17
 */
public interface TestService {



    List<User> getUserPageList();


    List<User> getUserList();


    User getUserInfo();


}
