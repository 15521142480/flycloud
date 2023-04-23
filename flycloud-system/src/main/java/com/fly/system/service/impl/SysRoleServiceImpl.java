package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.utils.StringUtils;
import com.fly.system.domain.SysRole;
import com.fly.system.domain.bo.SysRoleBo;
import com.fly.system.domain.vo.SysRoleVo;
import com.fly.system.mapper.SysRoleMapper;
import com.fly.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 角色信息Service业务层处理
 *
 * @author fly
 * @date 2023-04-23
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper baseMapper;

    /**
     * 查询角色信息
     */
    @Override
    public SysRoleVo queryById(Long roleId){
        return baseMapper.selectVoById(roleId);
    }


    /**
     * 查询角色信息列表
     */
    @Override
    public PageVo<SysRoleVo> queryPageList(SysRoleBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysRole> lqw = buildQueryWrapper(bo);
        Page<SysRoleVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询角色信息列表
     */
    @Override
    public List<SysRoleVo> queryList(SysRoleBo bo) {
        LambdaQueryWrapper<SysRole> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysRole> buildQueryWrapper(SysRoleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysRole> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getRoleName()), SysRole::getRoleName, bo.getRoleName());
        lqw.eq(StringUtils.isNotBlank(bo.getRoleKey()), SysRole::getRoleKey, bo.getRoleKey());
        lqw.eq(bo.getRoleSort() != null, SysRole::getRoleSort, bo.getRoleSort());
        lqw.eq(StringUtils.isNotBlank(bo.getDataScope()), SysRole::getDataScope, bo.getDataScope());
        lqw.eq(bo.getMenuCheckStrictly() != null, SysRole::getMenuCheckStrictly, bo.getMenuCheckStrictly());
        lqw.eq(bo.getDeptCheckStrictly() != null, SysRole::getDeptCheckStrictly, bo.getDeptCheckStrictly());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysRole::getStatus, bo.getStatus());
        return lqw;
    }


    /**
     * 新增角色信息
     */
    @Override
    public Boolean insertByBo(SysRoleBo bo) {
        SysRole add = BeanUtil.toBean(bo, SysRole.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRoleId(add.getRoleId());
        }
        return flag;
    }


    /**
     * 修改角色信息
     */
    @Override
    public Boolean updateByBo(SysRoleBo bo) {
        SysRole update = BeanUtil.toBean(bo, SysRole.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysRole entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除角色信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
