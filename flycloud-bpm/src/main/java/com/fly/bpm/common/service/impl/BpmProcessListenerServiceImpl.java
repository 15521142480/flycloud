package com.fly.bpm.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.enums.StatusEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fly.bpm.api.domain.bo.BpmProcessListenerBo;
import com.fly.bpm.api.domain.vo.BpmProcessListenerVo;
import com.fly.bpm.api.domain.BpmProcessListener;
import com.fly.bpm.common.mapper.BpmProcessListenerMapper;
import com.fly.bpm.common.service.IBpmProcessListenerService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * BPM 流程监听器Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmProcessListenerServiceImpl extends BaseServiceImpl<BpmProcessListenerMapper, BpmProcessListener> implements IBpmProcessListenerService {

    private final BpmProcessListenerMapper baseMapper;

    /**
     * 查询BPM 流程监听器
     */
    @Override
    public BpmProcessListenerVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询BPM 流程监听器列表
     */
    @Override
    public PageVo<BpmProcessListenerVo> queryPageList(BpmProcessListenerBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmProcessListener> lqw = buildQueryWrapper(bo);
        Page<BpmProcessListenerVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询BPM 流程监听器列表
     */
    @Override
    public List<BpmProcessListenerVo> queryList(BpmProcessListenerBo bo) {
        LambdaQueryWrapper<BpmProcessListener> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmProcessListener> buildQueryWrapper(BpmProcessListenerBo bo) {

        bo.setIsDeleted(false);
        bo.setStatus(StatusEnum.ENABLE.getStatus());

        LambdaQueryWrapper<BpmProcessListener> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), BpmProcessListener::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), BpmProcessListener::getType, bo.getType());
        lqw.eq(bo.getStatus() != null, BpmProcessListener::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getEvent()), BpmProcessListener::getEvent, bo.getEvent());
        lqw.eq(StringUtils.isNotBlank(bo.getValueType()), BpmProcessListener::getValueType, bo.getValueType());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), BpmProcessListener::getValue, bo.getValue());
        lqw.eq(bo.getIsDeleted() != null, BpmProcessListener::getIsDeleted, bo.getIsDeleted());

        lqw.orderByDesc(BpmProcessListener::getId);

        return lqw;
    }


    /**
     * 新增BPM 流程监听器
     */
    @Override
    public Boolean insertByBo(BpmProcessListenerBo bo) {
        BpmProcessListener add = BeanUtil.toBean(bo, BpmProcessListener.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改BPM 流程监听器
     */
    @Override
    public Boolean updateByBo(BpmProcessListenerBo bo) {
        BpmProcessListener update = BeanUtil.toBean(bo, BpmProcessListener.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmProcessListener entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除BPM 流程监听器
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
