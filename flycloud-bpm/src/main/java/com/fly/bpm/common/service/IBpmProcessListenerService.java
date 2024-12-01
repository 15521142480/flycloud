package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.vo.BpmProcessListenerVo;
import com.fly.bpm.api.domain.bo.BpmProcessListenerBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * BPM 流程监听器Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmProcessListenerService {

    /**
     * 查询BPM 流程监听器
     */
    BpmProcessListenerVo queryById(Long id);

    /**
     * 查询BPM 流程监听器列表
     */
    PageVo<BpmProcessListenerVo> queryPageList(BpmProcessListenerBo bo, PageBo pageBo);

    /**
     * 查询BPM 流程监听器列表
     */
    List<BpmProcessListenerVo> queryList(BpmProcessListenerBo bo);

    /**
     * 修改BPM 流程监听器
     */
    Boolean insertByBo(BpmProcessListenerBo bo);

    /**
     * 修改BPM 流程监听器
     */
    Boolean updateByBo(BpmProcessListenerBo bo);

    /**
     * 校验并批量删除BPM 流程监听器信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
