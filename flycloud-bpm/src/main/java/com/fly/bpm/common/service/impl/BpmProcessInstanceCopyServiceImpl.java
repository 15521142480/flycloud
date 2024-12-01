package com.fly.bpm.common.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.bpm.task.service.BpmTaskService;
import com.fly.common.constant.bpm.ErrorCodeConstants;
import com.fly.common.utils.StringUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.utils.collection.ArrayUtils;
import com.fly.common.utils.collection.CollectionUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.fly.bpm.api.domain.bo.BpmProcessInstanceCopyBo;
import com.fly.bpm.api.domain.vo.BpmProcessInstanceCopyVo;
import com.fly.bpm.api.domain.BpmProcessInstanceCopy;
import com.fly.bpm.common.mapper.BpmProcessInstanceCopyMapper;
import com.fly.bpm.common.service.IBpmProcessInstanceCopyService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Collection;
import java.util.Set;

import static com.fly.common.exception.utils.ServiceExceptionUtils.exception;
import static com.fly.common.utils.collection.CollectionUtils.convertList;

/**
 * BPM 流程实例抄送Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
//@RequiredArgsConstructor
@Service
public class BpmProcessInstanceCopyServiceImpl extends BaseServiceImpl<BpmProcessInstanceCopyMapper, BpmProcessInstanceCopy> implements IBpmProcessInstanceCopyService {

    @Resource
    private BpmProcessInstanceCopyMapper baseMapper;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmTaskService taskService;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmInstanceService processInstanceService;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmProcessDefinitionService processDefinitionService;



    /**
     * 获得抄送的流程的分页
     *
     */
    @Override
    public PageVo<BpmProcessInstanceCopyVo> getProcessInstanceCopyPage(Long userId, BpmProcessInstanceCopyBo bo, PageBo pageBo) {

        LambdaQueryWrapper<BpmProcessInstanceCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(userId != null, BpmProcessInstanceCopy::getUserId, userId);
        lqw.eq(bo.getStartUserId() != null, BpmProcessInstanceCopy::getStartUserId, bo.getStartUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessInstanceId()), BpmProcessInstanceCopy::getProcessInstanceId, bo.getProcessInstanceId());
        lqw.like(StringUtils.isNotBlank(bo.getProcessInstanceName()), BpmProcessInstanceCopy::getProcessInstanceName, bo.getProcessInstanceName());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), BpmProcessInstanceCopy::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getTaskId()), BpmProcessInstanceCopy::getTaskId, bo.getTaskId());
        lqw.like(StringUtils.isNotBlank(bo.getTaskName()), BpmProcessInstanceCopy::getTaskName, bo.getTaskName());
        lqw.eq(StringUtils.isNotBlank(bo.getActivityId()), BpmProcessInstanceCopy::getActivityId, bo.getActivityId());
        lqw.eq(BpmProcessInstanceCopy::getIsDeleted, false);

        Object startTime = ArrayUtils.get(bo.getCreateTime(), 0);
        Object endTime = ArrayUtils.get(bo.getCreateTime(), 1);
        if (startTime != null) {
            lqw.ge(BpmProcessInstanceCopy::getCreateTime, startTime);
        }
        if (endTime != null) {
            lqw.ge(BpmProcessInstanceCopy::getCreateTime, endTime);
        }
        lqw.orderByDesc(BpmProcessInstanceCopy::getId);

        Page<BpmProcessInstanceCopyVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }


    /**
     * 获得抄送的流程的分页
     */
    @Override
    public PageVo<BpmProcessInstanceCopy> getProcessInstanceCopyPageByEntity(Long userId, BpmProcessInstanceCopyBo bo, PageBo pageBo) {

        LambdaQueryWrapper<BpmProcessInstanceCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(userId != null, BpmProcessInstanceCopy::getUserId, userId);
        lqw.eq(bo.getStartUserId() != null, BpmProcessInstanceCopy::getStartUserId, bo.getStartUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessInstanceId()), BpmProcessInstanceCopy::getProcessInstanceId, bo.getProcessInstanceId());
        lqw.like(StringUtils.isNotBlank(bo.getProcessInstanceName()), BpmProcessInstanceCopy::getProcessInstanceName, bo.getProcessInstanceName());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), BpmProcessInstanceCopy::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getTaskId()), BpmProcessInstanceCopy::getTaskId, bo.getTaskId());
        lqw.like(StringUtils.isNotBlank(bo.getTaskName()), BpmProcessInstanceCopy::getTaskName, bo.getTaskName());
        lqw.eq(StringUtils.isNotBlank(bo.getActivityId()), BpmProcessInstanceCopy::getActivityId, bo.getActivityId());
        lqw.eq(BpmProcessInstanceCopy::getIsDeleted, false);

        Object startTime = ArrayUtils.get(bo.getCreateTime(), 0);
        Object endTime = ArrayUtils.get(bo.getCreateTime(), 1);
        if (startTime != null) {
            lqw.ge(BpmProcessInstanceCopy::getCreateTime, startTime);
        }
        if (endTime != null) {
            lqw.ge(BpmProcessInstanceCopy::getCreateTime, endTime);
        }
        lqw.orderByDesc(BpmProcessInstanceCopy::getId);

        Page<BpmProcessInstanceCopy> result = baseMapper.selectPage(pageBo.build(), lqw);

        return this.build(result);
    }


    @Override
    public void createProcessInstanceCopy(Collection<Long> userIds, String taskId) {

        Task task = taskService.getTask(taskId);
        if (ObjectUtil.isNull(task)) {
            throw exception(ErrorCodeConstants.TASK_NOT_EXISTS);
        }
        String processInstanceId = task.getProcessInstanceId();
        createProcessInstanceCopy(userIds, processInstanceId, task.getTaskDefinitionKey(), task.getId(), task.getName());
    }


    @Override
    public void createProcessInstanceCopy(Collection<Long> userIds, String processInstanceId, String activityId, String taskId, String taskName) {

        // 1.1 校验流程实例存在
        ProcessInstance processInstance = processInstanceService.getProcessInstance(processInstanceId);
        if (processInstance == null) {
            throw exception(ErrorCodeConstants.PROCESS_INSTANCE_NOT_EXISTS);
        }
        // 1.2 校验流程定义存在
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinition(
                processInstance.getProcessDefinitionId());
        if (processDefinition == null) {
            throw exception(ErrorCodeConstants.PROCESS_DEFINITION_NOT_EXISTS);
        }

        // 2. 创建抄送流程
        List<BpmProcessInstanceCopy> copyList = convertList(userIds, userId -> new BpmProcessInstanceCopy()
                .setUserId(userId).setStartUserId(Long.valueOf(processInstance.getStartUserId()))
                .setProcessInstanceId(processInstanceId).setProcessInstanceName(processInstance.getName())
                .setCategory(processDefinition.getCategory()).setActivityId(activityId)
                .setTaskId(taskId).setTaskName(taskName));
        baseMapper.insertBatch(copyList);
    }


    @Override
    public Set<Long> getCopyUserIds(String processInstanceId, String activityId) {
        return CollectionUtils.convertSet(baseMapper.selectListByProcessInstanceIdAndActivityId(processInstanceId, activityId),
                BpmProcessInstanceCopy::getUserId);
    }

}
