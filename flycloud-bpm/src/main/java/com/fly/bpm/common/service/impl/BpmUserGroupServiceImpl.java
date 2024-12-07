package com.fly.bpm.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.common.enums.StatusEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.utils.collection.ArrayUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.bpm.api.domain.bo.BpmUserGroupBo;
import com.fly.bpm.api.domain.vo.BpmUserGroupVo;
import com.fly.bpm.api.domain.BpmUserGroup;
import com.fly.bpm.common.mapper.BpmUserGroupMapper;
import com.fly.bpm.common.service.IBpmUserGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * BPM 用户组Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmUserGroupServiceImpl extends BaseServiceImpl<BpmUserGroupMapper, BpmUserGroup> implements IBpmUserGroupService {

    private final BpmUserGroupMapper baseMapper;

    /**
     * 查询BPM 用户组
     */
    @Override
    public BpmUserGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询BPM 用户组列表
     */
    @Override
    public PageVo<BpmUserGroupVo> queryPageList(BpmUserGroupBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmUserGroup> lqw = buildQueryWrapper(bo);
        Page<BpmUserGroupVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询BPM 用户组列表
     */
    @Override
    public List<BpmUserGroupVo> queryList(BpmUserGroupBo bo) {
        LambdaQueryWrapper<BpmUserGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmUserGroup> buildQueryWrapper(BpmUserGroupBo bo) {

        bo.setIsDeleted(false);
        bo.setStatus(StatusEnum.ENABLE.getStatus());

//        LambdaQueryWrapper<BpmUserGroup> lqw = new LambdaQueryWrapperX<>().;
//        lqw.like(StringUtils.isNotBlank(bo.getName()), BpmUserGroup::getName, bo.getName());
//        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), BpmUserGroup::getDescription, bo.getDescription());
//        lqw.eq(StringUtils.isNotBlank(bo.getUserIds()), BpmUserGroup::getUserIds, bo.getUserIds());
//        lqw.eq(bo.getStatus() != null, BpmUserGroup::getStatus, bo.getStatus());
//        lqw.eq(bo.getIsDeleted() != null, BpmUserGroup::getIsDeleted, bo.getIsDeleted());
//
//        Object val1 = ArrayUtils.get(bo.getCreateTime(), 0);
//        Object val2 = ArrayUtils.get(bo.getCreateTime(), 1);
//        if (val1 != null && val2 != null) {
//            return lqw.between(BpmUserGroup::getCreateTime, val1, val2);
//        }
//        if (val1 != null) {
//            return lqw.ge(BpmUserGroup::getCreateTime, val1);
//        }
//        if (val2 != null) {
//            return lqw.le(BpmUserGroup::getCreateTime, val2);
//        }

        return new LambdaQueryWrapperX<BpmUserGroup>()
                .likeIfPresent(BpmUserGroup::getName, bo.getName())
                .eqIfPresent(BpmUserGroup::getStatus, bo.getStatus())
                .eqIfPresent(BpmUserGroup::getIsDeleted, bo.getIsDeleted())
                .betweenIfPresent(BpmUserGroup::getCreateTime, bo.getCreateTime())
                .orderByDesc(BpmUserGroup::getId);
    }


    /**
     * 新增BPM 用户组
     */
    @Override
    public Boolean insertByBo(BpmUserGroupBo bo) {
        BpmUserGroup add = BeanUtil.toBean(bo, BpmUserGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改BPM 用户组
     */
    @Override
    public Boolean updateByBo(BpmUserGroupBo bo) {
        BpmUserGroup update = BeanUtil.toBean(bo, BpmUserGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmUserGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除BPM 用户组
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
