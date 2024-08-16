package com.fly.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.auth.mapper.SysUserMapper;
import com.fly.auth.service.ISysUserService;
import com.flycloud.system.api.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户表 服务实现类
 *
 * @author: lxs
 * @date: 2024/8/12
 */
@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    /**
     * 忽略租户查询用户信息
     */
    @Override
    public SysUser getOneIgnoreTenant(SysUser sysUser) {
        return this.baseMapper.selectOneIgnoreTenant(sysUser);
    }

}
