package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.vo.BpmProcessDefinitionInfoVo;
import com.fly.bpm.api.domain.bo.BpmProcessDefinitionInfoBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * BPM 流程定义的信息Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmProcessDefinitionInfoService {

    /**
     * 查询BPM 流程定义的信息
     */
    BpmProcessDefinitionInfoVo queryById(Long id);

    /**
     * 查询BPM 流程定义的信息列表
     */
    PageVo<BpmProcessDefinitionInfoVo> queryPageList(BpmProcessDefinitionInfoBo bo, PageBo pageBo);

    /**
     * 查询BPM 流程定义的信息列表
     */
    List<BpmProcessDefinitionInfoVo> queryList(BpmProcessDefinitionInfoBo bo);

    /**
     * 修改BPM 流程定义的信息
     */
    Boolean insertByBo(BpmProcessDefinitionInfoBo bo);

    /**
     * 修改BPM 流程定义的信息
     */
    Boolean updateByBo(BpmProcessDefinitionInfoBo bo);

    /**
     * 校验并批量删除BPM 流程定义的信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
