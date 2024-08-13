//package com.fly.test.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.fly.common.utils.StringUtils;
//import com.fly.common.database.web.domain.vo.PageVo;
//import com.fly.common.database.web.domain.bo.PageBo;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.fly.common.database.web.service.impl.BaseServiceImpl;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import com.fly.test.domain.bo.SysUserTestBo;
//import com.fly.test.domain.vo.SysUserTestVo;
//import com.fly.test.domain.SysUserTest;
//import com.fly.test.mapper.SysUserTestMapper;
//import com.fly.test.service.ISysUserTestService;
//
//import java.util.List;
//import java.util.Collection;
//
///**
// * 用户信息Service业务层处理
// *
// * @author fly
// * @date 2023-04-22
// */
//@RequiredArgsConstructor
//@Service
//public class SysUserServiceTestImpl extends BaseServiceImpl<SysUserTestMapper, SysUserTest> implements ISysUserTestService {
//
//    private final SysUserTestMapper baseMapper;
//
//    /**
//     * 查询用户信息
//     */
//    @Override
//    public SysUserTestVo queryById(Long userId){
//        return baseMapper.selectVoById(userId);
//    }
//
//
//    /**
//     * 查询用户信息列表
//     */
//    @Override
//    public PageVo<SysUserTestVo> queryPageList(SysUserTestBo bo, PageBo pageBo) {
//
//        // todo 法1 (传统mybatis查询):
////        Page<SysUserVo> pageList = baseMapper.xxx(this.buildIPage(pageBo), SysUserBo);
////        return this.buildPageVo(pageList);
//
//        LambdaQueryWrapper<SysUserTest> lqw = buildQueryWrapper(bo);
//        Page<SysUserTestVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
//        return this.build(result);
//    }
//
//
//    /**
//     * 查询用户信息列表
//     */
//    @Override
//    public List<SysUserTestVo> queryList(SysUserTestBo bo) {
//        LambdaQueryWrapper<SysUserTest> lqw = buildQueryWrapper(bo);
//        return baseMapper.selectVoList(lqw);
//    }
//
//    private LambdaQueryWrapper<SysUserTest> buildQueryWrapper(SysUserTestBo bo) {
//        LambdaQueryWrapper<SysUserTest> lqw = Wrappers.lambdaQuery();
//        lqw.eq(bo.getDeptId() != null, SysUserTest::getDeptId, bo.getDeptId());
//        lqw.like(StringUtils.isNotBlank(bo.getUserName()), SysUserTest::getUserName, bo.getUserName());
//        lqw.like(StringUtils.isNotBlank(bo.getNickName()), SysUserTest::getNickName, bo.getNickName());
//        lqw.eq(StringUtils.isNotBlank(bo.getUserType()), SysUserTest::getUserType, bo.getUserType());
//        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), SysUserTest::getEmail, bo.getEmail());
//        lqw.eq(StringUtils.isNotBlank(bo.getPhonenumber()), SysUserTest::getPhonenumber, bo.getPhonenumber());
//        lqw.eq(StringUtils.isNotBlank(bo.getSex()), SysUserTest::getSex, bo.getSex());
//        return lqw;
//    }
//
//
//    /**
//     * 新增用户信息
//     */
//    @Override
//    public Boolean insertByBo(SysUserTestBo bo) {
//        SysUserTest add = BeanUtil.toBean(bo, SysUserTest.class);
//        validEntityBeforeSave(add);
//        boolean flag = baseMapper.insert(add) > 0;
//        if (flag) {
//            bo.setUserId(add.getUserId());
//        }
//        return flag;
//    }
//
//
//    /**
//     * 修改用户信息
//     */
//    @Override
//    public Boolean updateByBo(SysUserTestBo bo) {
//        SysUserTest update = BeanUtil.toBean(bo, SysUserTest.class);
//        validEntityBeforeSave(update);
//        return baseMapper.updateById(update) > 0;
//    }
//
//
//    /**
//     * 保存前的数据校验
//     */
//    private void validEntityBeforeSave(SysUserTest entity){
//        //TODO 做一些数据校验,如唯一约束
//    }
//
//
//    /**
//     * 批量删除用户信息
//     */
//    @Override
//    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
//        if(isValid){
//            //TODO 做一些业务上的校验,判断是否需要校验
//        }
//        return baseMapper.deleteBatchIds(ids) > 0;
//    }
//
//
//}
