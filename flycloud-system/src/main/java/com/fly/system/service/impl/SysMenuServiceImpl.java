package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.enums.RoleCodeEnum;
import com.fly.common.enums.SysTypeEnum;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import com.fly.system.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysMenuBo;
import com.fly.system.api.domain.vo.SysMenuVo;
import com.fly.system.api.domain.SysMenu;
import com.fly.system.mapper.SysMenuMapper;
import com.fly.system.service.ISysMenuService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 菜单Service业务层处理
 *
 * @author fly
 * @date 2024-08-31
 */
@RequiredArgsConstructor
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final SysMenuMapper baseMapper;
    private final SysRoleMapper sysRoleMapper;


    /**
     * 查询菜单列表
     */
    @Override
    public List<SysMenuVo> queryList(SysMenuBo bo) {

        LambdaQueryWrapper<SysMenu> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }


    /**
     * 菜单列表 - 全部
     */
    public List<SysMenuTreeVo> getAllList(SysMenuBo bo) {
        return baseMapper.selectAllList(bo);
    }


    /**
     * 菜单列表-树型
     */
    @Override
    public List<SysMenuTreeVo> getTreeList(SysMenuBo bo) {

        List<SysMenuTreeVo> sysMenuTreeDataList = this.getAllList(bo);

        // 菜单树型数据组装
        List<SysMenuTreeVo> resultList = new ArrayList<>();
        Map<Long, List<SysMenuTreeVo>> sysMenuMap = new HashMap<>();

        for (SysMenuTreeVo sysMenuTreeData : sysMenuTreeDataList) {

            if (sysMenuMap.get(sysMenuTreeData.getParentId()) == null) {
                List<SysMenuTreeVo> sysMenuTreeInitList = new ArrayList<>();
                sysMenuMap.put(sysMenuTreeData.getParentId(), sysMenuTreeInitList);
            }

            sysMenuMap.get(sysMenuTreeData.getParentId()).add(sysMenuTreeData);
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
     * 根据用户获取菜单树数据列表
     *
     */
    @Override
    public List<SysMenuTreeVo> getMenuTreeListByUserId(Long userId) {

        List<SysMenuTreeVo> resultList = new ArrayList<>();

        // 超级管理员展示全部菜单, 否则显示用户角色拥有的菜单
        List<SysMenuTreeVo> sysMenuTreeDataList = new ArrayList<>();
        int count = sysRoleMapper.getRoleCountByUserAndCode(userId, SysTypeEnum.fly_platform.getCode(), RoleCodeEnum.SUPER_ADMIN.getCode());
        if (count > 0) {
            sysMenuTreeDataList = this.getAllList(new SysMenuBo().setType(SysTypeEnum.fly_platform.getCode()));
        } else {
            sysMenuTreeDataList = baseMapper.selectMenuListByUserId(userId, SysTypeEnum.fly_platform.getCode());
        }

        Map<Long, List<SysMenuTreeVo>> sysMenuMap = new HashMap<>();

        // 菜单树型数据组装
        for (SysMenuTreeVo sysMenuTreeData : sysMenuTreeDataList) {

            if (sysMenuMap.get(sysMenuTreeData.getParentId()) == null) {
                List<SysMenuTreeVo> sysMenuTreeInitList = new ArrayList<>();
                sysMenuMap.put(sysMenuTreeData.getParentId(), sysMenuTreeInitList);
            }

            sysMenuMap.get(sysMenuTreeData.getParentId()).add(sysMenuTreeData);
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
     * 新增/修改菜单
     */
    @Override
    @Transactional
    public int saveOrUpdate(SysMenuBo bo) {

        FlyUser loginUser = UserUtils.getCurUser();
        SysMenu sysMenu = BeanUtil.toBean(bo, SysMenu.class);
        if (bo.getId() == null) { // 新增

            // 判断重复名  ->  一级菜单直接判断  二三级菜单的拿当前的parentId判断
//            int menuNameCount = sysMenuExpandMapper.isMenuNameExists(smSysMenuTreeBean);
//            if (menuNameCount > 0) {
//                Assert.error("SM_SYS_MENU_NAME_HAS_EXIST",smSysMenuTreeBean.getMenuName());
//            }

            // 有序的加入排序号
//            Integer maxSortNo = sysMenuExpandMapper.getMaxSortNo();
//            int sortNo;
//            if (maxSortNo == null || maxSortNo == 0 || String.valueOf(maxSortNo).equals("")){
//                sortNo = 1;
//            } else {
//                sortNo = maxSortNo + 1;
//            }
//            sysMenu.setId(UUIDUtils.generateUUID());
            sysMenu.setCreateBy(loginUser.getId().toString());
            sysMenu.setCreateTime(LocalDateTime.now());
//            sysMenu.setType(SysType.fly_platform);
            baseMapper.insert(sysMenu);

        } else { // 修改

            sysMenu.setUpdateBy(loginUser.getId().toString());
            sysMenu.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(sysMenu);
        }

//        baseMapper.insertOrUpdate();

        return 1;
    }


    /**
     * 查询菜单
     */
    @Override
    public SysMenuVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询菜单列表
     */
    @Override
    public PageVo<SysMenuVo> queryPageList(SysMenuBo bo, PageBo pageBo) {

        LambdaQueryWrapper<SysMenu> lqw = buildQueryWrapper(bo);
        Page<SysMenuVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(SysMenuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getType() != null, SysMenu::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysMenu::getName, bo.getName());
        lqw.eq(bo.getParentId() != null, SysMenu::getParentId, bo.getParentId());
        lqw.eq(StringUtils.isNotBlank(bo.getPermission()), SysMenu::getPermission, bo.getPermission());
        lqw.eq(StringUtils.isNotBlank(bo.getButtonPermission()), SysMenu::getButtonPermission, bo.getButtonPermission());
        lqw.eq(StringUtils.isNotBlank(bo.getPath()), SysMenu::getPath, bo.getPath());
        lqw.eq(StringUtils.isNotBlank(bo.getComponent()), SysMenu::getComponent, bo.getComponent());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), SysMenu::getIcon, bo.getIcon());
        lqw.eq(bo.getStatus() != null, SysMenu::getStatus, bo.getStatus());
        lqw.eq(bo.getSort() != null, SysMenu::getSort, bo.getSort());
        lqw.eq(bo.getKeepAlive() != null, SysMenu::getKeepAlive, bo.getKeepAlive());
        lqw.eq(bo.getTarget() != null, SysMenu::getTarget, bo.getTarget());
        lqw.eq(bo.getIsDeleted() != null, SysMenu::getIsDeleted, bo.getIsDeleted());

        lqw.orderByAsc(SysMenu::getSort);
        return lqw;
    }


    /**
     * 新增菜单
     */
    @Override
    public Boolean insertByBo(SysMenuBo bo) {
        SysMenu add = BeanUtil.toBean(bo, SysMenu.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改菜单
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
     * 批量删除菜单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


}
