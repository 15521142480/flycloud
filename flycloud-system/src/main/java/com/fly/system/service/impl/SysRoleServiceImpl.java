package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.fly.common.enums.RoleCodeEnum;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.SysTypeEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.system.api.domain.SysRoleMenu;
import com.fly.system.api.domain.bo.SysMenuBo;
import com.fly.system.api.domain.bo.SysRoleMenuBo;
import com.fly.system.api.domain.vo.*;
import com.fly.system.mapper.SysRoleMenuMapper;
import com.fly.system.service.ISysMenuService;
import com.fly.system.service.ISysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysRoleBo;
import com.fly.system.api.domain.SysRole;
import com.fly.system.mapper.SysRoleMapper;
import com.fly.system.service.ISysRoleService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 角色Service业务层处理
 *
 * @author fly
 * @date 2024-08-31
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper baseMapper;
    private final ISysMenuService sysMenuService;
    private final ISysRoleMenuService sysRoleMenuService;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 查询角色
     */
    @Override
    public SysRoleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询角色列表
     */
    @Override
    public PageVo<SysRoleVo> queryPageList(SysRoleBo bo, PageBo pageBo) {

        LambdaQueryWrapper<SysRole> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(SysRole::getSort);
        Page<SysRoleVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询角色菜单列表 - 树型
     */
    @Override
    public List<SysMenuTreeVo> getRoleMenuTreeList(Long roleId) {

        // 所有菜单
        List<SysMenuTreeVo> sysMenuTreeDataList = sysMenuService.getAllList(new SysMenuBo().setType(SysTypeEnum.fly_platform.getCode()));

        // 角色含有的菜单
        List<SysRoleMenuVo> sysRoleMenuDataList = new ArrayList<>();
        if (roleId != null) {
            sysRoleMenuDataList = sysRoleMenuService.queryList(new SysRoleMenuBo().setRoleId(roleId));
        }

        // 菜单树型数据组装
        List<SysMenuTreeVo> resultList = new ArrayList<>();
        Map<Long, List<SysMenuTreeVo>> sysMenuMap = new HashMap<>();

        for (SysMenuTreeVo sysMenuTreeData : sysMenuTreeDataList) {

            if (sysMenuMap.get(sysMenuTreeData.getParentId()) == null) {
                List<SysMenuTreeVo> sysMenuTreeInitList = new ArrayList<>();
                sysMenuMap.put(sysMenuTreeData.getParentId(), sysMenuTreeInitList);
            }
            sysMenuMap.get(sysMenuTreeData.getParentId()).add(sysMenuTreeData);

            // 处理按钮权限
            // 1.查询菜单的全部按钮列表
            List<SysMenuButtonPermissionVo> buttonPermissionList = JSON.parseArray(sysMenuTreeData.getButtonPermission(), SysMenuButtonPermissionVo.class);

            // 2.查询角色的按钮权限列表
            if (roleId != null && buttonPermissionList != null && !buttonPermissionList.isEmpty()) {

                Map<Long, Map<String,String>> roleMenuPermissionMap = new HashMap<>();
                for (SysRoleMenuVo sysRoleMenuData : sysRoleMenuDataList) {

                    Long menuId = sysRoleMenuData.getMenuId();
                    String permission = sysRoleMenuData.getPermission();
                    if (roleMenuPermissionMap.containsKey(menuId)) {
                        Map<String,String> permissionMap = roleMenuPermissionMap.get(menuId);
                        permissionMap.put(permission, permission);
                        roleMenuPermissionMap.put(menuId, permissionMap);
                    } else {
                        Map<String,String> permissionMap = new HashMap<>();
                        permissionMap.put(permission, permission);
                        roleMenuPermissionMap.put(menuId, permissionMap);
                    }
                }

                // 3. 通过当前的菜单所有权限和角色拥有的菜单权限对比，如有设值为是
                if (roleMenuPermissionMap.containsKey(sysMenuTreeData.getId())) {
                    Map<String,String> permissionMap = roleMenuPermissionMap.get(sysMenuTreeData.getId());
                    for (SysMenuButtonPermissionVo sysMenuButtonPermissionData : buttonPermissionList) {
                        if (permissionMap.containsKey(sysMenuButtonPermissionData.getBtnPermission())) {
//                            sysMenuButtonPermissionData.setFlag("1");
                            sysMenuButtonPermissionData.setChecked(true); // 新版
                        }
                    }
                }

                // 4.赋值
                sysMenuTreeData.setButtonPermission(JSON.toJSONString(buttonPermissionList));
            }
        }

        for (SysMenuTreeVo sysMenuTreeData : sysMenuTreeDataList) {

            if (sysMenuTreeData.getParentId().toString().equals("0")) {
                sysMenuTreeData.setChildren(this.handleSysMenuChild(sysMenuMap, sysMenuTreeData.getId()));
                resultList.add(sysMenuTreeData);
            }
        }

        return resultList;
    }

    /**
     * 递归处理子菜单数据
     *
     * @param sysMenuMap 父菜单集合数据
     * @param menuId 菜单id
     */
    public List<SysMenuTreeVo> handleSysMenuChild (Map<Long, List<SysMenuTreeVo>> sysMenuMap, Long menuId){

        List<SysMenuTreeVo> sysMenuTreeList = sysMenuMap.get(menuId);
        if (sysMenuTreeList != null) {
            for (SysMenuTreeVo sysMenuTreeData : sysMenuTreeList) {

                sysMenuTreeData.setChildren(this.handleSysMenuChild(sysMenuMap, sysMenuTreeData.getId()));
            }
        }

        return sysMenuTreeList;
    }


    /**
     * 新增/修改角色
     */
    @Override
    @Transactional
    public int saveOrUpdate(SysRoleBo bo) {

        int rowBaseCount = 0;
        SysRole sysRole = BeanUtil.toBean(bo, SysRole.class);
        FlyUser flyUser = UserUtils.getCurUser();
        if (bo.getId() == null) {  // 新增

            // 基本信息
//            sysRole.setId(UUIDUtils.generateUUID());
//            sysRole.setType(SysTypeEnum.fly_platform.getCode());
            sysRole.setStatus(StatusEnum.ENABLE.getStatus());
            sysRole.setCreateBy(flyUser.getId().toString());
            sysRole.setCreateTime(LocalDateTime.now());
            rowBaseCount = baseMapper.insert(sysRole);

        } else { // 修改

            // 基础信息
            sysRole.setUpdateBy(flyUser.getId().toString());
            sysRole.setUpdateTime(LocalDateTime.now());
            rowBaseCount = baseMapper.updateById(sysRole);

        }

        // 权限信息；先删后新增 todo 权限功能已拆分
        if (StringUtils.isNotBlank(bo.getRoleMenuPermissionJson())) {

            LambdaQueryWrapper<SysRoleMenu> lqw = Wrappers.lambdaQuery();
            lqw.eq(SysRoleMenu::getRoleId, sysRole.getId());
            sysRoleMenuMapper.delete(lqw);

            List<SysRoleMenuVo> sysRoleMenuDataList = JSON.parseArray(bo.getRoleMenuPermissionJson(), SysRoleMenuVo.class);
            List<SysRoleMenu> roleMenuList = new ArrayList<SysRoleMenu>();
            for (SysRoleMenuVo sysRoleMenuData : sysRoleMenuDataList){

                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenu.setMenuId(sysRoleMenuData.getMenuId());
                sysRoleMenu.setPermission(sysRoleMenuData.getPermission());
                roleMenuList.add(sysRoleMenu);
            }
            sysRoleMenuMapper.insertBatch(roleMenuList);
        }


        return rowBaseCount;
    }


    /**
     * 修改角色信息
     */
    @Override
    public int updateById(SysRoleBo bo) {

        SysRole sysRole = BeanUtil.toBean(bo, SysRole.class);
        if (StringUtils.isBlank(sysRole.getId().toString())) {
            throw new ServiceException("id为空！");
        }
        baseMapper.updateById(sysRole);

        return 1;
    }

    @Override
    public int updateMenuPermission(SysRoleBo bo) {

        LambdaQueryWrapper<SysRoleMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysRoleMenu::getRoleId, bo.getId());
        sysRoleMenuMapper.delete(lqw);

        List<SysRoleMenuVo> sysRoleMenuDataList = JSON.parseArray(bo.getRoleMenuPermissionJson(), SysRoleMenuVo.class);
        List<SysRoleMenu> roleMenuList = new ArrayList<SysRoleMenu>();
        for (SysRoleMenuVo sysRoleMenuData : sysRoleMenuDataList){

            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(bo.getId());
            sysRoleMenu.setMenuId(sysRoleMenuData.getMenuId());
            sysRoleMenu.setPermission(sysRoleMenuData.getPermission());
            roleMenuList.add(sysRoleMenu);
        }
        sysRoleMenuMapper.insertBatch(roleMenuList);

        return 1;
    }

    /**
     * 查询角色列表
     */
    @Override
    public List<SysRoleVo> queryList(SysRoleBo bo) {
        LambdaQueryWrapper<SysRole> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(SysRole::getSort);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysRole> buildQueryWrapper(SysRoleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysRole> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getType() != null, SysRole::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysRole::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), SysRole::getCode, bo.getCode());
        lqw.eq(bo.getSort() != null, SysRole::getSort, bo.getSort());
        lqw.eq(bo.getStatus() != null, SysRole::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDeleted() != null, SysRole::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增角色
     */
    @Override
    public Boolean insertByBo(SysRoleBo bo) {
        SysRole add = BeanUtil.toBean(bo, SysRole.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改角色
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
     * 批量删除角色
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 根据用户查询权限信息
     */
    @Override
    public List<String> getPermissionListByUserId(Long userId) {

        // 超级管理员展示全部权限, 否则显示用户角色拥有的权限
        List<String> resultList = new ArrayList<>();
        int count = baseMapper.getRoleCountByUserAndCode(userId, SysTypeEnum.fly_platform.getCode(), RoleCodeEnum.SUPER_ADMIN.getCode());
        if (count > 0) {

            List<SysMenuVo> sysMenuVoList = sysMenuService.queryList(new SysMenuBo().setType(SysTypeEnum.fly_platform.getCode()).setStatus(StatusEnum.ENABLE.getStatus()));
            for (SysMenuVo sysMenuVo : sysMenuVoList) {

                if (StringUtils.isNotBlank(sysMenuVo.getPermission())) {

                    resultList.add(sysMenuVo.getPermission()); // 1. 菜单权限
                    if (StringUtils.isNotBlank(sysMenuVo.getButtonPermission())) {

                        List<SysMenuButtonPermissionVo> buttonPermissionList = JSON.parseArray(sysMenuVo.getButtonPermission(), SysMenuButtonPermissionVo.class);
                        for (SysMenuButtonPermissionVo sysMenuButtonPermissionVo : buttonPermissionList) {

                            // 2. 菜单按钮权限
                            resultList.add(sysMenuVo.getPermission() + ":" + sysMenuButtonPermissionVo.getBtnPermission());
                        }
                    }
                }
            }

        } else {
            resultList = baseMapper.selectPermissionListByUserId(userId);
        }

        return resultList;
    }



}
