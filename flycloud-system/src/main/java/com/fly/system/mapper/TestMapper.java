package com.fly.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.mapper.BaseMapperPlus;
import com.fly.system.domain.User;
import com.fly.system.domain.dto.UserDto;
import com.fly.system.domain.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试-业务dao层
 *
 * @author lxs
 * @date 2023/4/17
 */
@Mapper
public interface TestMapper extends BaseMapperPlus<TestMapper, User, UserVo> {


    Page<UserVo> selectUserPageList(IPage<User> page, @Param("dto") UserDto userDto);


    List<UserVo> selectUserList();


    UserVo selectUserInfo(int uuid);


}
