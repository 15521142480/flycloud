package com.fly.bpm.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.enums.StatusEnum;
import com.fly.common.security.util.UserUtils;
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
import com.fly.bpm.api.domain.bo.BpmProcessExpressionBo;
import com.fly.bpm.api.domain.vo.BpmProcessExpressionVo;
import com.fly.bpm.api.domain.BpmProcessExpression;
import com.fly.bpm.common.mapper.BpmProcessExpressionMapper;
import com.fly.bpm.common.service.IBpmProcessExpressionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * BPM 流程达式Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
public class BpmProcessExpressionServiceImpl extends BaseServiceImpl<BpmProcessExpressionMapper, BpmProcessExpression> implements IBpmProcessExpressionService {

    private final BpmProcessExpressionMapper baseMapper;

    /**
     * 查询BPM 流程达式
     */
    @Override
    public BpmProcessExpressionVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询BPM 流程达式列表
     */
    @Override
    public PageVo<BpmProcessExpressionVo> queryPageList(BpmProcessExpressionBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BpmProcessExpression> lqw = buildQueryWrapper(bo);
        Page<BpmProcessExpressionVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 查询BPM 流程达式列表
     */
    @Override
    public List<BpmProcessExpressionVo> queryList(BpmProcessExpressionBo bo) {
        LambdaQueryWrapper<BpmProcessExpression> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BpmProcessExpression> buildQueryWrapper(BpmProcessExpressionBo bo) {

        bo.setIsDeleted(false);
        bo.setStatus(StatusEnum.ENABLE.getStatus());

        LambdaQueryWrapper<BpmProcessExpression> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), BpmProcessExpression::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, BpmProcessExpression::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getExpression()), BpmProcessExpression::getExpression, bo.getExpression());
        lqw.eq(bo.getIsDeleted() != null, BpmProcessExpression::getIsDeleted, bo.getIsDeleted());

        Object val1 = ArrayUtils.get(bo.getCreateTime(), 0);
        Object val2 = ArrayUtils.get(bo.getCreateTime(), 1);
        if (val1 != null && val2 != null) {
            return lqw.between(BpmProcessExpression::getCreateTime, val1, val2);
        }
        if (val1 != null) {
            return lqw.ge(BpmProcessExpression::getCreateTime, val1);
        }
        if (val2 != null) {
            return lqw.le(BpmProcessExpression::getCreateTime, val2);
        }

        lqw.orderByDesc(BpmProcessExpression::getId);
        return lqw;
    }


    /**
     * 新增BPM 流程达式
     */
    @Override
    public Boolean insertByBo(BpmProcessExpressionBo bo) {

        BpmProcessExpression add = BeanUtil.toBean(bo, BpmProcessExpression.class);
        validEntityBeforeSave(add);
        add.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        add.setCreateTime(LocalDateTime.now());

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改BPM 流程达式
     */
    @Override
    public Boolean updateByBo(BpmProcessExpressionBo bo) {

        BpmProcessExpression update = BeanUtil.toBean(bo, BpmProcessExpression.class);
        validEntityBeforeSave(update);
        update.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        update.setCreateTime(LocalDateTime.now());

        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BpmProcessExpression entity){
        //TODO 做一些数据校验,如唯一约束
    }


    /**
     * 批量删除BPM 流程达式
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
