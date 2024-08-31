package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.fly.common.utils.StringUtils;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysUserBo;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.domain.SysUser;
import com.fly.system.mapper.SysUserMapper;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户Service业务层处理
 *
 * @author fly
 * @date 2024-08-31
 */
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    private final SysUserMapper baseMapper;

    /**
     * 查询用户
     */
    @Override
    public SysUserVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询用户列表
     */
    @Override
    public PageVo<SysUserVo> queryPageList(SysUserBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SysUser> lqw = buildQueryWrapper(bo);
        Page<SysUserVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询用户列表
     */
    @Override
    public List<SysUserVo> queryList(SysUserBo bo) {
        LambdaQueryWrapper<SysUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getAccount()), SysUser::getAccount, bo.getAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getPassword()), SysUser::getPassword, bo.getPassword());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysUser::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), SysUser::getRealName, bo.getRealName());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), SysUser::getAvatar, bo.getAvatar());
        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), SysUser::getEmail, bo.getEmail());
        lqw.eq(StringUtils.isNotBlank(bo.getTelephone()), SysUser::getTelephone, bo.getTelephone());
        lqw.eq(bo.getBirthday() != null, SysUser::getBirthday, bo.getBirthday());
        lqw.eq(bo.getSex() != null, SysUser::getSex, bo.getSex());
        lqw.eq(bo.getDepartId() != null, SysUser::getDepartId, bo.getDepartId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysUser::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getIsDeleted()), SysUser::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }


    /**
     * 新增用户
     */
    @Override
    public Boolean insertByBo(SysUserBo bo) {
        SysUser add = BeanUtil.toBean(bo, SysUser.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改用户
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
     * 批量删除用户
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }



    // ==============================================================================

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
//    @Override
//    public List<SysUserPOI> export() {
//        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUser::getIsDeleted, "0");
//        List<SysUser> sysUsers = this.baseMapper.selectList(queryWrapper);
//        return sysUsers.stream().map(sysUser -> {
//            SysUserPOI sysUserPoi = new SysUserPOI();
//            BeanUtils.copyProperties(sysUser, sysUserPoi);
//            sysUserPoi.setDepartName(sysDepartService.getById(sysUser.getDepartId()).getName());
//            sysUserPoi.setRoleName(sysRoleService.getById(sysUser.getRoleId()).getRoleName());
//            sysUserPoi.setStatusName(dictService.getValue("status", sysUser.getStatus()).getData());
//            return sysUserPoi;
//        }).collect(Collectors.toList());
//    }
}
