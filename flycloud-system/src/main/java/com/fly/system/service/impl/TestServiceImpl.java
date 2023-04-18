package com.fly.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.domain.vo.PageVo;
import com.fly.common.database.service.impl.BaseServiceImpl;
import com.fly.common.utils.StringUtils;
import com.fly.system.domain.User;
import com.fly.system.domain.dto.UserDto;
import com.fly.system.domain.vo.UserVo;
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
//@RequiredArgsConstructor
public class TestServiceImpl extends BaseServiceImpl<TestMapper, User> implements TestService {

//    private final TestMapper testMapper;


    @Override
    public PageVo<UserVo> getUserPageList(UserDto query) {

        // todo 法1:
//        IPage<UserVo> pageList = baseMapper.selectVoPage(buildIPage(query), this.getWrapper(query));
//        return this.buildPageVo(pageList);

        // todo 法2:
        Page<UserVo> pageList = baseMapper.selectUserPageList(this.buildIPage(query), query);
        return this.buildPageVo(pageList);
    }

    private Wrapper<User> getWrapper(UserDto query) {
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(query.getUserName()), User::getUserName, query.getUserName());
        lqw.eq(StringUtils.isNotBlank(query.getNickName()), User::getNickName, query.getNickName());
        return lqw;
    }

    @Override
    public List<UserVo> getUserList() {
        return baseMapper.selectUserList();
    }

    @Override
    public UserVo getUserInfo(int uuid) {
        return baseMapper.selectUserInfo(uuid);
    }


}
