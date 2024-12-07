package com.fly.bpm.task.mapper;

import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.bpm.api.domain.BpmProcessInstanceCopy;
import com.fly.bpm.api.domain.vo.BpmProcessInstanceCopyVo;

import java.util.List;

/**
 * BPM 流程实例抄送Mapper接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface BpmProcessInstanceCopyMapper extends BaseMapperPlus<BpmProcessInstanceCopyMapper, BpmProcessInstanceCopy, BpmProcessInstanceCopyVo> {


    default List<BpmProcessInstanceCopy> selectListByProcessInstanceIdAndActivityId(String processInstanceId, String activityId) {
        return selectList(BpmProcessInstanceCopy::getProcessInstanceId, processInstanceId,
                BpmProcessInstanceCopy::getActivityId, activityId);
    }
}
