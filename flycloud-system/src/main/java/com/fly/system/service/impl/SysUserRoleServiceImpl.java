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
import com.fly.system.api.domain.bo.SysUserRoleBo;
import com.fly.system.api.domain.vo.SysUserRoleVo;
import com.fly.system.api.domain.SysUserRole;
import com.fly.system.mapper.SysUserRoleMapper;
import com.fly.system.service.ISysUserRoleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户角色Service业务层处理
 *
 * @author fly
 * @date 2024-08-31
 */
@RequiredArgsConstructor
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    private final SysUserRoleMapper baseMapper;

    /**
     * 查询用户角色
     */
    @Override
    public SysUserRoleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询用户角色列表
     */
    @Override
    public PageVo<SysUserRoleVo> queryPageList(SysUserRoleBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysUserRole> lqw = buildQueryWrapper(bo);
        Page<SysUserRoleVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询用户角色列表
     */
    @Override
    public List<SysUserRoleVo> queryList(SysUserRoleBo bo) {
        LambdaQueryWrapper<SysUserRole> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysUserRole> buildQueryWrapper(SysUserRoleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysUserRole> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, SysUserRole::getUserId, bo.getUserId());
        lqw.eq(bo.getRoleId() != null, SysUserRole::getRoleId, bo.getRoleId());
        return lqw;
    }


    /**
     * 新增用户角色
     */
    @Override
    public Boolean insertByBo(SysUserRoleBo bo) {
        SysUserRole add = BeanUtil.toBean(bo, SysUserRole.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改用户角色
     */
    @Override
    public Boolean updateByBo(SysUserRoleBo bo) {
        SysUserRole update = BeanUtil.toBean(bo, SysUserRole.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysUserRole entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除用户角色
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
