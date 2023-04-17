package com.fly.system.service.impl;

import com.fly.system.domain.User;
import com.fly.system.mapper.TestMapper;
import com.fly.system.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试-业务业务层
 *
 * @author lxs
 * @date 2023/4/17
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestMapper testMapper;


    @Override
    public List<User> getUserPageList() {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return testMapper.selectUserList();
    }

    @Override
    public User getUserInfo() {
        return null;
    }
}
