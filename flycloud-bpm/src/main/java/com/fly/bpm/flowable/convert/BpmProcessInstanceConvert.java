package com.fly.bpm.flowable.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import com.fly.bpm.api.domain.vo.instance.BpmApprovalDetailRespVO;
import com.fly.bpm.api.domain.vo.instance.BpmProcessInstanceRespVO;
import com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelNodeVO;
import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.api.domain.vo.task.BpmProcessInstanceBpmnModelViewRespVO;
import com.fly.bpm.api.domain.vo.task.BpmTaskRespVO;
import com.fly.bpm.api.domain.vo.user.SysUserBpmVO;
import com.fly.bpm.flowable.event.BpmProcessInstanceStatusEvent;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.common.utils.collection.MapUtils;
import com.fly.common.utils.collection.SetUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.vo.SysUserVo;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

/**
 * 流程实例 Convert
 *
 */
@Mapper
public interface BpmProcessInstanceConvert {

    BpmProcessInstanceConvert INSTANCE = Mappers.getMapper(BpmProcessInstanceConvert.class);

    default PageVo<BpmProcessInstanceRespVO> buildProcessInstancePage(PageVo<HistoricProcessInstance> pageResult,
                                                                          Map<String, ProcessDefinition> processDefinitionMap,
                                                                          Map<String, BpmCategory> categoryMap,
                                                                          Map<String, List<Task>> taskMap,
                                                                          Map<Long, SysUserVo> userMap,
                                                                          Map<Long, SysDeptVo> deptMap) 
    {
        
        PageVo<BpmProcessInstanceRespVO> vpPageResult = BeanUtils.toBean(pageResult, BpmProcessInstanceRespVO.class);
        for (int i = 0; i < pageResult.getList().size(); i++) {
            BpmProcessInstanceRespVO respVO = vpPageResult.getList().get(i);
            respVO.setStatus(FlowableUtils.getProcessInstanceStatus(pageResult.getList().get(i)));
            MapUtils.findAndThen(processDefinitionMap, respVO.getProcessDefinitionId(),
                    processDefinition -> respVO.setCategory(processDefinition.getCategory())
                            .setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class)));
            MapUtils.findAndThen(categoryMap, respVO.getCategory(), category -> respVO.setCategoryName(category.getName()));
            respVO.setTasks(BeanUtils.toBean(taskMap.get(respVO.getId()), BpmProcessInstanceRespVO.Task.class));
            // user
            if (userMap != null) {
                SysUserVo startUser = userMap.get(NumberUtils.parseLong(pageResult.getList().get(i).getStartUserId()));
                if (startUser != null) {
                    respVO.setStartUser(BeanUtils.toBean(startUser, SysUserBpmVO.class));
                    MapUtils.findAndThen(deptMap, startUser.getDeptId(), dept -> respVO.getStartUser().setDeptName(dept.getName()));
                }
            }
        }
        return vpPageResult;
    }

    default BpmProcessInstanceRespVO buildProcessInstance(HistoricProcessInstance processInstance,
                                                          ProcessDefinition processDefinition,
                                                          BpmProcessDefinitionInfo processDefinitionInfo,
                                                          SysUserVo startUser,
                                                          SysDeptVo dept) {
        BpmProcessInstanceRespVO respVO = BeanUtils.toBean(processInstance, BpmProcessInstanceRespVO.class);
        respVO.setStatus(FlowableUtils.getProcessInstanceStatus(processInstance))
                .setFormVariables(FlowableUtils.getProcessInstanceFormVariable(processInstance));
        // definition
        respVO.setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class));
        copyTo(processDefinitionInfo, respVO.getProcessDefinition());
        // user
        if (startUser != null) {
            respVO.setStartUser(BeanUtils.toBean(startUser, SysUserBpmVO.class));
            if (dept != null) {
                respVO.getStartUser().setDeptName(dept.getName());
            }
        }
        return respVO;
    }

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionInfo from, @MappingTarget BpmProcessDefinitionRespVO to);

    default BpmProcessInstanceStatusEvent buildProcessInstanceStatusEvent(Object source, ProcessInstance instance, Integer status) {
        return new BpmProcessInstanceStatusEvent(source).setId(instance.getId()).setStatus(status)
                .setProcessDefinitionKey(instance.getProcessDefinitionKey()).setBusinessKey(instance.getBusinessKey());
    }

    default BpmMessageSendWhenProcessInstanceApproveReqDTO buildProcessInstanceApproveMessage(ProcessInstance instance) {
        return new BpmMessageSendWhenProcessInstanceApproveReqDTO()
                .setStartUserId(NumberUtils.parseLong(instance.getStartUserId()))
                .setProcessInstanceId(instance.getId())
                .setProcessInstanceName(instance.getName());
    }

    default BpmMessageSendWhenProcessInstanceRejectReqDTO buildProcessInstanceRejectMessage(ProcessInstance instance, String reason) {
        return new BpmMessageSendWhenProcessInstanceRejectReqDTO()
                .setProcessInstanceName(instance.getName())
                .setProcessInstanceId(instance.getId())
                .setReason(reason)
                .setStartUserId(NumberUtils.parseLong(instance.getStartUserId()));
    }

    default BpmProcessInstanceBpmnModelViewRespVO buildProcessInstanceBpmnModelView(HistoricProcessInstance processInstance,
                                                                                    List<HistoricTaskInstance> taskInstances,
                                                                                    BpmnModel bpmnModel,
                                                                                    BpmSimpleModelNodeVO simpleModel,
                                                                                    Set<String> unfinishedTaskActivityIds,
                                                                                    Set<String> finishedTaskActivityIds,
                                                                                    Set<String> finishedSequenceFlowActivityIds,
                                                                                    Set<String> rejectTaskActivityIds,
                                                                                    Map<Long, SysUserVo> userMap,
                                                                                    Map<Long, SysDeptVo> deptMap) {
        BpmProcessInstanceBpmnModelViewRespVO respVO = new BpmProcessInstanceBpmnModelViewRespVO();
        // 基本信息
        respVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmProcessInstanceRespVO.class, o -> o
                        .setStatus(FlowableUtils.getProcessInstanceStatus(processInstance)))
                .setStartUser(buildUser(processInstance.getStartUserId(), userMap, deptMap)));
        respVO.setTasks(convertList(taskInstances, task -> BeanUtils.toBean(task, BpmTaskRespVO.class)
                .setStatus(FlowableUtils.getTaskStatus(task)).setReason(FlowableUtils.getTaskReason(task))
                .setAssigneeUser(buildUser(task.getAssignee(), userMap, deptMap))
                .setOwnerUser(buildUser(task.getOwner(), userMap, deptMap))));
        respVO.setBpmnXml(BpmnModelUtils.getBpmnXml(bpmnModel));
        respVO.setSimpleModel(simpleModel);
        // 进度信息
        respVO.setUnfinishedTaskActivityIds(unfinishedTaskActivityIds)
                .setFinishedTaskActivityIds(finishedTaskActivityIds)
                .setFinishedSequenceFlowActivityIds(finishedSequenceFlowActivityIds)
                .setRejectedTaskActivityIds(rejectTaskActivityIds);
        return respVO;
    }

    default SysUserBpmVO buildUser(String userIdStr,
                                       Map<Long, SysUserVo> userMap,
                                       Map<Long, SysDeptVo> deptMap) {
        if (StrUtil.isEmpty(userIdStr)) {
            return null;
        }
        Long userId = NumberUtils.parseLong(userIdStr);
        return buildUser(userId, userMap, deptMap);
    }

    default SysUserBpmVO buildUser(Long userId,
                                       Map<Long, SysUserVo> userMap,
                                       Map<Long, SysDeptVo> deptMap) {
        if (userId == null) {
            return null;
        }
        SysUserVo user = userMap.get(userId);
        if (user == null) {
            return null;
        }
        SysUserBpmVO userVO = BeanUtils.toBean(user, SysUserBpmVO.class);
        SysDeptVo dept = user.getDeptId() != null ? deptMap.get(user.getDeptId()) : null;
        if (dept != null) {
            userVO.setDeptName(dept.getName());
        }
        return userVO;
    }

    default BpmApprovalDetailRespVO.ActivityNodeTask buildApprovalTaskInfo(HistoricTaskInstance task) {
        if (task == null) {
            return null;
        }
        return BeanUtils.toBean(task, BpmApprovalDetailRespVO.ActivityNodeTask.class)
                .setStatus(FlowableUtils.getTaskStatus(task)).setReason(FlowableUtils.getTaskReason(task));
    }

    default Set<Long> parseUserIds(HistoricProcessInstance processInstance,
                                   List<BpmApprovalDetailRespVO.ActivityNode> activityNodes,
                                   BpmTaskRespVO todoTask) {
        Set<Long> userIds = new HashSet<>();
        if (processInstance != null) {
            userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        }
        for (BpmApprovalDetailRespVO.ActivityNode activityNode : activityNodes) {
            CollUtil.addAll(userIds, convertSet(activityNode.getTasks(), BpmApprovalDetailRespVO.ActivityNodeTask::getAssignee));
            CollUtil.addAll(userIds, convertSet(activityNode.getTasks(), BpmApprovalDetailRespVO.ActivityNodeTask::getOwner));
            CollUtil.addAll(userIds, activityNode.getCandidateUserIds());
        }
        if (todoTask != null) {
            CollUtil.addIfAbsent(userIds, todoTask.getAssignee());
            CollUtil.addIfAbsent(userIds, todoTask.getOwner());
            if (CollUtil.isNotEmpty(todoTask.getChildren())) {
                CollUtil.addAll(userIds, convertSet(todoTask.getChildren(), BpmTaskRespVO::getAssignee));
                CollUtil.addAll(userIds, convertSet(todoTask.getChildren(), BpmTaskRespVO::getOwner));
            }
        }
        return userIds;
    }

    default Set<Long> parseUserIds02(HistoricProcessInstance processInstance,
                                     List<HistoricTaskInstance> tasks) {
        Set<Long> userIds = SetUtils.asSet(Long.valueOf(processInstance.getStartUserId()));
        tasks.forEach(task -> {
            CollUtil.addIfAbsent(userIds, NumberUtils.parseLong((task.getAssignee())));
            CollUtil.addIfAbsent(userIds, NumberUtils.parseLong((task.getOwner())));
        });
        return userIds;
    }

    default BpmApprovalDetailRespVO buildApprovalDetail(BpmnModel bpmnModel,
                                                        ProcessDefinition processDefinition,
                                                        BpmProcessDefinitionInfo processDefinitionInfo,
                                                        HistoricProcessInstance processInstance,
                                                        Integer processInstanceStatus,
                                                        List<BpmApprovalDetailRespVO.ActivityNode> activityNodes,
                                                        BpmTaskRespVO todoTask,
                                                        Map<String, String> formFieldsPermission,
                                                        Map<Long, SysUserVo> userMap,
                                                        Map<Long, SysDeptVo> deptMap) {
        // 1.1 流程实例
        BpmProcessInstanceRespVO processInstanceResp = null;
        if (processInstance != null) {
            SysUserVo startUser = userMap.get(NumberUtils.parseLong(processInstance.getStartUserId()));
            SysDeptVo dept = startUser != null ? deptMap.get(startUser.getDeptId()) : null;
            processInstanceResp = buildProcessInstance(processInstance, null, null, startUser, dept);
        }

        // 1.2 流程定义
        BpmProcessDefinitionRespVO definitionResp = BpmProcessDefinitionConvert.INSTANCE.buildProcessDefinition(
                processDefinition, null, processDefinitionInfo, null, null, bpmnModel);

        // 1.3 流程节点
        activityNodes.forEach(approveNode -> {
            if (approveNode.getTasks() != null) {
                approveNode.getTasks().forEach(task -> {
                    task.setAssigneeUser(buildUser(task.getAssignee(), userMap, deptMap));
                    task.setOwnerUser(buildUser(task.getOwner(), userMap, deptMap));
                });
            }
            approveNode.setCandidateUsers(convertList(approveNode.getCandidateUserIds(), userId -> buildUser(userId, userMap, deptMap)));
        });

        // 1.4 待办任务
        if (todoTask != null) {
            todoTask.setAssigneeUser(buildUser(todoTask.getAssignee(), userMap, deptMap));
            todoTask.setOwnerUser(buildUser(todoTask.getOwner(), userMap, deptMap));
            if (CollUtil.isNotEmpty(todoTask.getChildren())) {
                todoTask.getChildren().forEach(childTask -> {
                    childTask.setAssigneeUser(buildUser(childTask.getAssignee(), userMap, deptMap));
                    childTask.setOwnerUser(buildUser(childTask.getOwner(), userMap, deptMap));
                });
            }
        }

        // 2. 拼接起来
        return new BpmApprovalDetailRespVO().setStatus(processInstanceStatus)
                .setProcessDefinition(definitionResp)
                .setProcessInstance(processInstanceResp)
                .setFormFieldsPermission(formFieldsPermission)
                .setTodoTask(todoTask)
                .setActivityNodes(activityNodes);
    }

}
