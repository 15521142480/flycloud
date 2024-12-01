package com.fly.bpm.task.service.impl;

import com.fly.bpm.task.service.BpmActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * BPM 活动实例 Service 实现类
 *
 * @author: lxs
 * @date: 2024/11/30
 */
@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class BpmActivityServiceImpl implements BpmActivityService {

    private final HistoryService historyService;



    /**
     * 获得指定流程实例的活动实例列表
     *
     */
    @Override
    public List<HistoricActivityInstance> getActivityListByProcessInstanceId(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();
    }


    /**
     * 获得执行编号对应的活动实例
     *
     */
    @Override
    public List<HistoricActivityInstance> getHistoricActivityListByExecutionId(String executionId) {
        return historyService.createHistoricActivityInstanceQuery().executionId(executionId).list();
    }
}
