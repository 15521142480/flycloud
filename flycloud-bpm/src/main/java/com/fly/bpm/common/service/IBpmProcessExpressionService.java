package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.vo.BpmProcessExpressionVo;
import com.fly.bpm.api.domain.bo.BpmProcessExpressionBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * BPM 流程达式Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmProcessExpressionService {

    /**
     * 查询BPM 流程达式
     */
    BpmProcessExpressionVo queryById(Long id);

    /**
     * 查询BPM 流程达式列表
     */
    PageVo<BpmProcessExpressionVo> queryPageList(BpmProcessExpressionBo bo, PageBo pageBo);

    /**
     * 查询BPM 流程达式列表
     */
    List<BpmProcessExpressionVo> queryList(BpmProcessExpressionBo bo);

    /**
     * 修改BPM 流程达式
     */
    Boolean insertByBo(BpmProcessExpressionBo bo);

    /**
     * 修改BPM 流程达式
     */
    Boolean updateByBo(BpmProcessExpressionBo bo);

    /**
     * 校验并批量删除BPM 流程达式信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
