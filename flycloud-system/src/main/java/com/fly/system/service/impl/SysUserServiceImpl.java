package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.utils.StringUtils;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.domain.bo.SysUserBo;
import com.fly.system.domain.vo.SysUserVo;
import com.fly.system.domain.SysUser;
import com.fly.system.mapper.SysUserMapper;
import com.fly.system.service.ISysUserService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户信息Service业务层处理
 *
 * @author fly
 * @date 2023-04-22
 */
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper baseMapper;

    /**
     * 查询用户信息
     */
    @Override
    public SysUserVo queryById(Long userId){
        return baseMapper.selectVoById(userId);
    }


    /**
     * 查询用户信息列表
     */
    @Override
    public PageVo<SysUserVo> queryPageList(SysUserBo bo, PageBo pageBo) {

        // todo 法1 (传统mybatis查询):
//        Page<SysUserVo> pageList = baseMapper.xxx(this.buildIPage(pageBo), SysUserBo);
//        return this.buildPageVo(pageList);

        LambdaQueryWrapper<SysUser> lqw = buildQueryWrapper(bo);
        Page<SysUserVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询用户信息列表
     */
    @Override
    public List<SysUserVo> queryList(SysUserBo bo) {
        LambdaQueryWrapper<SysUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUserBo bo) {
        LambdaQueryWrapper<SysUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDeptId() != null, SysUser::getDeptId, bo.getDeptId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), SysUser::getUserName, bo.getUserName());
        lqw.like(StringUtils.isNotBlank(bo.getNickName()), SysUser::getNickName, bo.getNickName());
        lqw.eq(StringUtils.isNotBlank(bo.getUserType()), SysUser::getUserType, bo.getUserType());
        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), SysUser::getEmail, bo.getEmail());
        lqw.eq(StringUtils.isNotBlank(bo.getPhonenumber()), SysUser::getPhonenumber, bo.getPhonenumber());
        lqw.eq(StringUtils.isNotBlank(bo.getSex()), SysUser::getSex, bo.getSex());
        return lqw;
    }


    /**
     * 新增用户信息
     */
    @Override
    public Boolean insertByBo(SysUserBo bo) {
        SysUser add = BeanUtil.toBean(bo, SysUser.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUserId(add.getUserId());
        }
        return flag;
    }


    /**
     * 修改用户信息
     */
    @Override
    public Boolean updateByBo(SysUserBo bo) {
        SysUser update = BeanUtil.toBean(bo, SysUser.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysUser entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除用户信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
