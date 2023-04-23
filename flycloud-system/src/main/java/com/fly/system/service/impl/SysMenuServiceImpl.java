package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.utils.StringUtils;
import com.fly.system.domain.SysMenu;
import com.fly.system.domain.bo.SysMenuBo;
import com.fly.system.domain.vo.SysMenuVo;
import com.fly.system.mapper.SysMenuMapper;
import com.fly.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 菜单权限Service业务层处理
 *
 * @author fly
 * @date 2023-04-23
 */
@RequiredArgsConstructor
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final SysMenuMapper baseMapper;

    /**
     * 查询菜单权限
     */
    @Override
    public SysMenuVo queryById(Long menuId){
        return baseMapper.selectVoById(menuId);
    }


    /**
     * 查询菜单权限列表
     */
    @Override
    public PageVo<SysMenuVo> queryPageList(SysMenuBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysMenu> lqw = buildQueryWrapper(bo);
        Page<SysMenuVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询菜单权限列表
     */
    @Override
    public List<SysMenuVo> queryList(SysMenuBo bo) {
        LambdaQueryWrapper<SysMenu> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(SysMenuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getMenuName()), SysMenu::getMenuName, bo.getMenuName());
        lqw.eq(bo.getParentId() != null, SysMenu::getParentId, bo.getParentId());
        lqw.eq(bo.getOrderNum() != null, SysMenu::getOrderNum, bo.getOrderNum());
        lqw.eq(StringUtils.isNotBlank(bo.getPath()), SysMenu::getPath, bo.getPath());
        lqw.eq(StringUtils.isNotBlank(bo.getComponent()), SysMenu::getComponent, bo.getComponent());
        lqw.eq(StringUtils.isNotBlank(bo.getQueryParam()), SysMenu::getQueryParam, bo.getQueryParam());
        lqw.eq(bo.getIsFrame() != null, SysMenu::getIsFrame, bo.getIsFrame());
        lqw.eq(bo.getIsCache() != null, SysMenu::getIsCache, bo.getIsCache());
        lqw.eq(StringUtils.isNotBlank(bo.getMenuType()), SysMenu::getMenuType, bo.getMenuType());
        lqw.eq(StringUtils.isNotBlank(bo.getVisible()), SysMenu::getVisible, bo.getVisible());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysMenu::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getPerms()), SysMenu::getPerms, bo.getPerms());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), SysMenu::getIcon, bo.getIcon());
        return lqw;
    }


    /**
     * 新增菜单权限
     */
    @Override
    public Boolean insertByBo(SysMenuBo bo) {
        SysMenu add = BeanUtil.toBean(bo, SysMenu.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setMenuId(add.getMenuId());
        }
        return flag;
    }


    /**
     * 修改菜单权限
     */
    @Override
    public Boolean updateByBo(SysMenuBo bo) {
        SysMenu update = BeanUtil.toBean(bo, SysMenu.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysMenu entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除菜单权限
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
