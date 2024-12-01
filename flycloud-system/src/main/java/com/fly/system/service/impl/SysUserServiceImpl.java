package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.constant.CommonConstants;
import com.fly.common.enums.ErrorCodeConstants;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.SysTypeEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.exception.utils.ServiceExceptionUtils;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.CryptoUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.domain.SysUserRole;
import com.fly.system.api.domain.vo.UserDetailInfoVo;
import com.fly.system.mapper.SysUserRoleMapper;
import com.fly.system.service.ISysMenuService;
import com.fly.system.service.ISysRoleService;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysUserBo;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.domain.SysUser;
import com.fly.system.mapper.SysUserMapper;

import java.time.LocalDateTime;
import java.util.*;

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

    private final ISysRoleService sysRoleService;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final ISysMenuService sysMenuService;



    /**
     * 查询用户列表
     */
    @Override
    public PageVo<SysUserVo> queryPageList(SysUserBo bo, PageBo pageBo) {

        LambdaQueryWrapper<SysUser> lqw = buildQueryWrapper(bo);
        Page<SysUserVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);

        PageVo<SysUserVo> pageVo = this.build(result);
        List<SysUserVo> sysUserVoList = pageVo.getList();
        for (SysUserVo sysUserVo : sysUserVoList) {

            sysUserVo.setUserTypeName(SysTypeEnum.getSysTypeNameByType(sysUserVo.getUserType()));
            sysUserVo.setRoleIds(String.join(",", sysRoleService.getRoleIdListByUserId(sysUserVo.getId())));
            sysUserVo.setRoleNames(String.join(",", sysRoleService.getRoleNameListByUserId(sysUserVo.getId())));
        }
        pageVo.setList(sysUserVoList);

        return pageVo;
    }


    /**
     * 查询所有用户精简版
     *
     */
    @Override
    public List<SysUserVo> queryAllListSimple(SysUserBo bo) {

        return baseMapper.selectAllListSimple(bo);
    }

    /**
     * 查询用户
     */
    @Override
    public UserDetailInfoVo getDetailInfo(Long id) {

        UserDetailInfoVo userDetailInfoVo = new UserDetailInfoVo();

        // 用户基本信息
        userDetailInfoVo.setUser(this.queryById(id));
        userDetailInfoVo.getUser().setPassword(null);

        // 权限信息
        userDetailInfoVo.setPermissionList(sysRoleService.getPermissionListByUserId(id));

        // 菜单信息
        userDetailInfoVo.setMenuTreeList(sysMenuService.getMenuTreeListByUserId(id));

        return userDetailInfoVo;
    }

    /**
     * 查询用户
     */
    @Override
    public SysUserVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 用户新增/修改
     */
    @Override
    public int saveOrUpdate(SysUserBo bo) {

        SysUser sysUser = BeanUtil.toBean(bo, SysUser.class);
        FlyUser curUser = UserUtils.getCurUser();
        boolean isUpdate = sysUser.getId() != null;

        // 用户账号/昵称 判重
        int accountCount = baseMapper.selectCountByAccount(sysUser.getAccount(), isUpdate ? sysUser.getId() : null);
        if (accountCount > 0) {
            throw new ServiceException("账号重复！");
        }
        if (StringUtils.isNotBlank(sysUser.getName())) {
            int nameCount = baseMapper.selectCountByName(sysUser.getName(), isUpdate ? sysUser.getId() : null);
            if (nameCount > 0) {
                throw new ServiceException("昵称重复！");
            }
        }

        if (!isUpdate) { // 新增

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String newPassword = encoder.encode(sysUser.getPassword());
            sysUser.setPassword(newPassword);

            sysUser.setCreateBy(curUser.getId().toString());
            sysUser.setCreateTime(LocalDateTime.now());
            baseMapper.insert(sysUser);

        } else { // 修改

            // 是否有修改密码
            SysUser o_sysUser = baseMapper.selectById(sysUser.getId());
            if (!o_sysUser.getPassword().equals(sysUser.getPassword())) {
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                String newPassword = encoder.encode(sysUser.getPassword());
                sysUser.setPassword(newPassword);
            }

            sysUser.setUpdateBy(curUser.getId().toString());
            sysUser.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(sysUser);

            // 移除用户角色权限
            LambdaQueryWrapper<SysUserRole> lqw = Wrappers.lambdaQuery();
            lqw.eq(SysUserRole::getUserId, sysUser.getId());
            sysUserRoleMapper.delete(lqw);
        }

        // 初始化用户角色权限
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (String roleId : bo.getRoleIds().split(",")) {

            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(Long.valueOf(roleId));
            sysUserRoleList.add(sysUserRole);
        }
        sysUserRoleMapper.insertBatch(sysUserRoleList);

        return 1;
    }


    /**
     * 重置密码
     */
    @Override
    public int resetPassword(Long id) {

        SysUser sysUser = baseMapper.selectById(id);

        String md5str = CryptoUtils.encodeMD5(CommonConstants.INIT_USER_PASSWORD);
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String initPassword = encoder.encode(md5str);
        sysUser.setPassword(initPassword);

        return baseMapper.updateById(sysUser);
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
        lqw.eq(bo.getDeptId() != null, SysUser::getDeptId, bo.getDeptId());
        lqw.eq(bo.getStatus() != null, SysUser::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDeleted() != null, SysUser::getIsDeleted, bo.getIsDeleted());
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
    public SysUser queryOneUserByUser(SysUser sysUser) {
        return this.baseMapper.selectOneUserByUser(sysUser);
    }

    /**
     * 忽略租户查询用户信息
     */
    @Override
    public List<SysUser> getByIds(Collection<Long> ids) {

        if (ids == null || ids.isEmpty()){
            return new ArrayList<>();
        }
        return this.baseMapper.selectBatchIds(ids);
    }

    /**
     * 根据ids验证用户
     */
    @Override
    public Boolean validateDeptByIds(Collection<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }

        // 获得用户信息
        List<SysUser> userList = this.getByIds(ids);
        Map<Long, SysUser> userMap = CollectionUtils.convertMap(userList, SysUser::getId);

        // 校验
        ids.forEach(id -> {

            SysUser user = userMap.get(id);
            if (user == null) {
                throw ServiceExceptionUtils.exception(ErrorCodeConstants.USER_NOT_EXISTS);
            }
            if (!StatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw ServiceExceptionUtils.exception(ErrorCodeConstants.USER_IS_DISABLE, user.getName());
            }
        });

        return true;
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
