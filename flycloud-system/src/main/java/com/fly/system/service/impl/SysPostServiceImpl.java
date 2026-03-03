package com.fly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.system.api.domain.bo.SysPostBo;
import com.fly.system.api.domain.vo.SysPostVo;
import com.fly.system.api.domain.SysPost;
import com.fly.system.mapper.SysPostMapper;
import com.fly.system.service.ISysPostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 岗位 业务层
 *
 * @author fly
 * @date 2026-03-02
 */
@RequiredArgsConstructor
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    private final SysPostMapper baseMapper;

    /**
     * 查询岗位
     */
    @Override
    public SysPostVo queryById(Long id) {

        return baseMapper.selectVoById(id);
    }


    /**
     * 查询岗位分页列表
     */
    @Override
    public PageVo<SysPostVo> queryPageList(SysPostBo bo, PageBo pageBo) {

        LambdaQueryWrapper<SysPost> lqw = buildQueryWrapper(bo);
        Page<SysPostVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询岗位列表
     */
    @Override
    public List<SysPostVo> queryList(SysPostBo bo) {

        LambdaQueryWrapper<SysPost> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }


    private LambdaQueryWrapper<SysPost> buildQueryWrapper(SysPostBo bo) {


        LambdaQueryWrapper<SysPost> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), SysPost::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysPost::getName, bo.getName());
        lqw.eq(bo.getSort() != null, SysPost::getSort, bo.getSort());
        lqw.eq(bo.getStatus() != null, SysPost::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDeleted() != null, SysPost::getIsDeleted, bo.getIsDeleted());

        lqw.orderByAsc(SysPost::getSort).orderByDesc(SysPost::getCreateTime);

        return lqw;
    }


    @Override
    public int saveOrUpdate(SysPostBo bo) {

        int rowBaseCount = 0;
        FlyUser flyUser = UserUtils.getCurUser();
        SysPost sysPost = BeanUtil.toBean(bo, SysPost.class);

        if (bo.getId() == null) {  // 新增

            sysPost.setStatus(StatusEnum.ENABLE.getStatus());
            sysPost.setCreateBy(flyUser.getId().toString());
            sysPost.setCreateTime(LocalDateTime.now());
            rowBaseCount = baseMapper.insert(sysPost);

        } else { // 修改

            sysPost.setUpdateBy(flyUser.getId().toString());
            sysPost.setUpdateTime(LocalDateTime.now());
            rowBaseCount = baseMapper.updateById(sysPost);
        }

        return rowBaseCount;
    }

    /**
     * 新增岗位
     */
    @Override
    public Boolean insertByBo(SysPostBo bo) {

        SysPost add = BeanUtil.toBean(bo, SysPost.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改岗位
     */
    @Override
    public Boolean updateByBo(SysPostBo bo) {

        SysPost update = BeanUtil.toBean(bo, SysPost.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysPost entity) {

        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除岗位
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {

        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
