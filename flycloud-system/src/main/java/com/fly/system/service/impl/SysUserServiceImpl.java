package com.fly.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.common.constant.SystemConstants;
import com.fly.common.database.entity.Search;
import com.fly.common.database.enums.OrderTypeEnum;
import com.fly.common.database.util.PageUtils;
import com.fly.common.exception.base.BaseException;
import com.fly.common.utils.CollectionUtils;
import com.fly.system.mapper.SysUserMapper;
import com.fly.system.service.ISysDepartService;
import com.fly.system.service.ISysDictService;
import com.fly.system.service.ISysRoleService;
import com.fly.system.service.ISysUserService;
import com.flycloud.system.api.entity.SysUser;
import com.flycloud.system.api.poi.SysUserPOI;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
 *
 * @author: lxs
 * @date: 2024/8/12
 */
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    private final ISysDepartService sysDepartService;
    private final ISysDictService dictService;
    private final ISysRoleService sysRoleService;


    /**
     * 用户状态设置
     */
    @Override
    public boolean status(String ids, String status) {
        Collection<? extends Serializable> collection = CollectionUtils.stringToCollection(ids);

        if (ObjectUtils.isEmpty(collection)) {
            throw new BaseException("传入的ID值不能为空！");
        }

        collection.forEach(id -> {
            SysUser sysUser = this.baseMapper.selectById(CollectionUtils.objectToLong(id, 0L));
            sysUser.setStatus(status);
            this.baseMapper.updateById(sysUser);
        });
        return true;
    }


    /**
     * 业务分页
     */
    @Override
    public IPage<SysUser> listPage(Search search, SysUser user) {

        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(StrUtil.isNotBlank(search.getStartDate()), SysUser::getCreateTime, search.getStartDate(), search.getEndDate());
        boolean isKeyword = StrUtil.isNotBlank(search.getKeyword());
        queryWrapper.like(isKeyword, SysUser::getName, search.getKeyword()).or(isKeyword)
                .like(isKeyword, SysUser::getId, search.getKeyword());

        queryWrapper.eq(user.getDepartId() != null, SysUser::getDepartId, user.getDepartId());

        // 根据排序字段进行排序
        if (StrUtil.isNotBlank(search.getProp())) {
            if (OrderTypeEnum.ASC.getValue().equalsIgnoreCase(search.getOrder())) {
                queryWrapper.orderByAsc(SysUser::getId);
            } else {
                queryWrapper.orderByDesc(SysUser::getId);
            }
        }
        // 分页查询
        IPage<SysUser> sysUserPage = this.baseMapper.selectPage(PageUtils.getPage(search), queryWrapper);

        // 拼装转换为中文字段数据
        List<SysUser> sysUserList = sysUserPage.getRecords().stream().peek(sysUser -> {
            sysUser.setDepartName(sysDepartService.getById(sysUser.getDepartId()).getName());
            sysUser.setStatusName(dictService.getValue("status", sysUser.getStatus()).getData());
            // 判断如果roleId为0，则赋值一个默认值
            if (SystemConstants.ROLE_DEFAULT_ID.equals(sysUser.getRoleId())) {
                sysUser.setRoleName(SystemConstants.ROLE_DEFAULT_VALUE);
            } else {
                sysUser.setRoleName(sysRoleService.getById(sysUser.getRoleId()).getRoleName());
            }
        }).collect(Collectors.toList());
        sysUserPage.setRecords(sysUserList);
        return sysUserPage;
    }


    /**
     * 忽略租户查询用户信息
     */
    @Override
    public SysUser getOneIgnoreTenant(SysUser sysUser) {
        return this.baseMapper.selectOneIgnoreTenant(sysUser);
    }


    /**
     * 导出列表
     */
    @Override
    public List<SysUserPOI> export() {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getIsDeleted, "0");
        List<SysUser> sysUsers = this.baseMapper.selectList(queryWrapper);
        return sysUsers.stream().map(sysUser -> {
            SysUserPOI sysUserPoi = new SysUserPOI();
            BeanUtils.copyProperties(sysUser, sysUserPoi);
            sysUserPoi.setDepartName(sysDepartService.getById(sysUser.getDepartId()).getName());
            sysUserPoi.setRoleName(sysRoleService.getById(sysUser.getRoleId()).getRoleName());
            sysUserPoi.setStatusName(dictService.getValue("status", sysUser.getStatus()).getData());
            return sysUserPoi;
        }).collect(Collectors.toList());
    }
}
