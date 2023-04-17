package com.fly.system.mapper;

import com.fly.system.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测试-业务dao层
 *
 * @author lxs
 * @date 2023/4/17
 */
@Mapper
public interface TestMapper {


    List<User> selectUserList();


}
