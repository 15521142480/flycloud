package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.system.domain.SysRoleMenu;
import com.fly.system.domain.bo.SysRoleMenuBo;
import com.fly.system.domain.vo.SysRoleMenuVo;
import com.fly.system.mapper.SysRoleMenuMapper;
import com.fly.system.service.ISysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 角色和菜单关联Service业务层处理
 *
 * @author fly
 * @date 2023-04-23
 */
@RequiredArgsConstructor
@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    private final SysRoleMenuMapper baseMapper;

    /**
     * 查询角色和菜单关联
     */
    @Override
    public SysRoleMenuVo queryById(Long roleId){
        return baseMapper.selectVoById(roleId);
    }


    /**
     * 查询角色和菜单关联列表
     */
    @Override
    public PageVo<SysRoleMenuVo> queryPageList(SysRoleMenuBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysRoleMenu> lqw = buildQueryWrapper(bo);
        Page<SysRoleMenuVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询角色和菜单关联列表
     */
    @Override
    public List<SysRoleMenuVo> queryList(SysRoleMenuBo bo) {
        LambdaQueryWrapper<SysRoleMenu> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysRoleMenu> buildQueryWrapper(SysRoleMenuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysRoleMenu> lqw = Wrappers.lambdaQuery();
        return lqw;
    }


    /**
     * 新增角色和菜单关联
     */
    @Override
    public Boolean insertByBo(SysRoleMenuBo bo) {
        SysRoleMenu add = BeanUtil.toBean(bo, SysRoleMenu.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRoleId(add.getRoleId());
        }
        return flag;
    }


    /**
     * 修改角色和菜单关联
     */
    @Override
    public Boolean updateByBo(SysRoleMenuBo bo) {
        SysRoleMenu update = BeanUtil.toBean(bo, SysRoleMenu.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysRoleMenu entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除角色和菜单关联
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
