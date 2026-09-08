package com.fly.bpm.flowable.convert;

import cn.hutool.core.map.MapUtil;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenTaskCreatedReqDTO;
import com.fly.bpm.api.domain.vo.task.BpmTaskCommentRespVO;
import com.fly.bpm.api.domain.vo.task.BpmTaskRespVO;
import com.fly.bpm.api.domain.vo.user.SysUserBpmVO;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.constant.bpm.BpmnModelConstants;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.bpm.BpmCommentTypeEnum;
import com.fly.common.enums.bpm.BpmSimpleModelNodeType;
import com.fly.common.enums.bpm.BpmTaskStatusEnum;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.DateUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.system.domain.vo.SysDeptVo;
import com.fly.system.api.system.domain.vo.SysUserVo;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fly.common.utils.collection.CollectionUtils.*;
import static com.fly.common.utils.collection.MapUtils.findAndThen;

/**
 * Bpm 任务 Convert
 *
 */
@Mapper
public interface BpmTaskConvert {

    BpmTaskConvert INSTANCE = Mappers.getMapper(BpmTaskConvert.class);


    /**
     *
     */
    default PageVo<BpmTaskRespVO> buildTodoTaskPage(PageVo<Task> PageVo,
                                                    Map<String, ProcessInstance> processInstanceMap,
                                                    Map<Long, SysUserVo> userMap) {
        return BeanUtils.toBean(PageVo, BpmTaskRespVO.class, taskVO -> {
            ProcessInstance processInstance = processInstanceMap.get(taskVO.getProcessInstanceId());
            if (processInstance == null) {
                return;
            }
            taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
            SysUserVo startUser = userMap.get(NumberUtils.parseLong(processInstance.getStartUserId()));
            taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, SysUserBpmVO.class));
        });
    }


    /**
     *
     */
    default PageVo<BpmTaskRespVO> buildTaskPage(PageVo<HistoricTaskInstance> PageVo,
                                                    Map<String, HistoricProcessInstance> processInstanceMap,
                                                    Map<Long, SysUserVo> userMap,
                                                    Map<Long, SysDeptVo> deptMap) {
        List<BpmTaskRespVO> taskVOList = CollectionUtils.convertList(PageVo.getList(), task -> {

            BpmTaskRespVO taskVO = BeanUtils.toBean(task, BpmTaskRespVO.class);
            taskVO.setStatus(FlowableUtils.getTaskStatus(task)).setReason(FlowableUtils.getTaskReason(task));
            // 用户信息
            SysUserVo assignUser = userMap.get(NumberUtils.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, SysUserBpmVO.class));
                findAndThen(deptMap, assignUser.getDeptId(), dept -> taskVO.getAssigneeUser().setDeptName(dept.getName()));
            }
            // 流程实例
            HistoricProcessInstance processInstance = processInstanceMap.get(taskVO.getProcessInstanceId());
            if (processInstance != null) {
                SysUserVo startUser = userMap.get(NumberUtils.parseLong(processInstance.getStartUserId()));
                taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
                taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, SysUserBpmVO.class));
            }
            return taskVO;
        });
        return new PageVo<>(taskVOList, PageVo.getTotal());
    }


    default List<BpmTaskRespVO> buildTaskListByProcessInstanceId(List<HistoricTaskInstance> taskList,
                                                                 HistoricProcessInstance processInstance,
                                                                 List<Comment> commentList,
                                                                 Map<Long, BpmForm> formMap,
                                                                 Map<Long, SysUserVo> userMap,
                                                                 Map<Long, SysDeptVo> deptMap) {
        Map<String, List<Comment>> commentMap = commentList.stream()
                .filter(comment -> comment.getTaskId() != null)
                .collect(Collectors.groupingBy(Comment::getTaskId));
        List<BpmTaskRespVO> result = new ArrayList<>();
        BpmTaskRespVO startUserTask = buildStartUserTask(processInstance, userMap, deptMap);
        result.add(startUserTask);

        result.addAll(CollectionUtils.convertList(taskList, task -> {
            // 发起人统一使用上面根据流程实例生成的记录，避免简单流程模型重复展示发起人。
            if (BpmnModelConstants.START_USER_NODE_ID.equals(task.getTaskDefinitionKey())) {
                return null;
            }
            // 特殊：已取消的任务，不返回
            BpmTaskRespVO taskVO = BeanUtils.toBean(task, BpmTaskRespVO.class);
            Integer taskStatus = FlowableUtils.getTaskStatus(task);
            if (BpmTaskStatusEnum.isCancelStatus(taskStatus)) {
                return null;
            }
            taskVO.setStatus(taskStatus).setReason(FlowableUtils.getTaskReason(task));
            // 表单信息
            BpmForm form = MapUtil.get(formMap, NumberUtils.parseLong(task.getFormKey()), BpmForm.class);
            if (form != null) {
                taskVO.setFormId(form.getId()).setFormName(form.getName()).setFormConf(form.getConf())
                        .setFormFields(form.getFields()).setFormVariables(FlowableUtils.getTaskFormVariable(task));
            }
            // 用户信息
            this.buildTaskAssignee(taskVO, task.getAssignee(), userMap, deptMap);
            this.buildTaskOwner(taskVO, task.getOwner(), userMap, deptMap);
            taskVO.setComments(buildTaskCommentList(
                    commentMap.getOrDefault(task.getId(), Collections.emptyList()), userMap, deptMap));
            return taskVO;
        }));
        // 流程实例结束后补充结束节点。该行不是 Flowable 任务，只用于完整展示流转记录。
        if (processInstance.getEndTime() != null) {
            result.add(buildEndTask(processInstance));
        }
        return result;
    }

    default BpmTaskRespVO buildStartUserTask(HistoricProcessInstance processInstance,
                                             Map<Long, SysUserVo> userMap,
                                             Map<Long, SysDeptVo> deptMap) {
        BpmTaskRespVO taskVO = new BpmTaskRespVO()
                .setId(processInstance.getId() + "-start-user")
                .setName(BpmSimpleModelNodeType.START_USER_NODE.getName())
                .setTaskDefinitionKey(BpmnModelConstants.START_USER_NODE_ID)
                .setProcessInstanceId(processInstance.getId())
                .setCreateTime(DateUtils.of(processInstance.getStartTime()))
                .setEndTime(DateUtils.of(processInstance.getStartTime()))
                .setDurationInMillis(0L)
                .setStatus(BpmTaskStatusEnum.APPROVE.getStatus())
                .setReason("发起申请")
                .setComments(Collections.emptyList());
        buildTaskAssignee(taskVO, processInstance.getStartUserId(), userMap, deptMap);
        return taskVO;
    }

    default BpmTaskRespVO buildEndTask(HistoricProcessInstance processInstance) {
        return new BpmTaskRespVO()
                .setId(processInstance.getId() + "-end")
                .setName(BpmSimpleModelNodeType.END_NODE.getName())
                .setRecordType("end")
                .setProcessInstanceId(processInstance.getId())
                .setEndTime(DateUtils.of(processInstance.getEndTime()));
    }

    default List<BpmTaskCommentRespVO> buildTaskCommentList(List<Comment> commentList,
                                                            Map<Long, SysUserVo> userMap,
                                                            Map<Long, SysDeptVo> deptMap) {
        Map<String, BpmCommentTypeEnum> commentTypeMap = java.util.Arrays.stream(BpmCommentTypeEnum.values())
                .collect(Collectors.toMap(BpmCommentTypeEnum::getType, Function.identity()));
        return CollectionUtils.convertList(commentList, comment -> {
            BpmCommentTypeEnum commentType = commentTypeMap.get(comment.getType());
            BpmTaskCommentRespVO commentVO = new BpmTaskCommentRespVO()
                    .setId(comment.getId())
                    .setType(comment.getType())
                    .setTypeName(commentType != null ? commentType.getName() : "流程评论")
                    .setMessage(comment.getFullMessage())
                    .setCreateTime(DateUtils.of(comment.getTime()));
            SysUserVo user = userMap.get(NumberUtils.parseLong(comment.getUserId()));
            if (user != null) {
                commentVO.setUser(BeanUtils.toBean(user, SysUserBpmVO.class));
                findAndThen(deptMap, user.getDeptId(), dept -> commentVO.getUser().setDeptName(dept.getName()));
            }
            return commentVO;
        });
    }

    default void buildTaskAssignee(BpmTaskRespVO task, String taskAssignee,
                                   Map<Long, SysUserVo> userMap,
                                   Map<Long, SysDeptVo> deptMap) {
        SysUserVo assignUser = userMap.get(NumberUtils.parseLong(taskAssignee));
        if (assignUser != null) {
            task.setAssigneeUser(BeanUtils.toBean(assignUser, SysUserBpmVO.class));
            findAndThen(deptMap, assignUser.getDeptId(), dept -> task.getAssigneeUser().setDeptName(dept.getName()));
        }
    }

    default void buildTaskOwner(BpmTaskRespVO task, String taskOwner,
                                Map<Long, SysUserVo> userMap,
                                Map<Long, SysDeptVo> deptMap) {
        SysUserVo ownerUser = userMap.get(NumberUtils.parseLong(taskOwner));
        if (ownerUser != null) {
            task.setOwnerUser(BeanUtils.toBean(ownerUser, SysUserBpmVO.class));
            findAndThen(deptMap, ownerUser.getDeptId(), dept -> task.getOwnerUser().setDeptName(dept.getName()));
        }
    }

    default BpmTaskRespVO buildTodoTask(Task todoTask, List<Task> childrenTasks,
                                        Map<Integer, BpmTaskRespVO.OperationButtonSetting> buttonsSetting) {
        return BeanUtils.toBean(todoTask, BpmTaskRespVO.class)
                .setStatus(FlowableUtils.getTaskStatus(todoTask)).setReason(FlowableUtils.getTaskReason(todoTask))
                .setButtonsSetting(buttonsSetting)
                .setChildren(convertList(childrenTasks, childTask -> BeanUtils.toBean(childTask, BpmTaskRespVO.class)
                        .setStatus(FlowableUtils.getTaskStatus(childTask))));
    }


    /**
     *
     */
    default List<BpmTaskRespVO> buildTaskListByParentTaskId(List<Task> taskList,
                                                            Map<Long, SysUserVo> userMap,
                                                            Map<Long, SysDeptVo> deptMap) {
        return convertList(taskList, task -> BeanUtils.toBean(task, BpmTaskRespVO.class, taskVO -> {
            SysUserVo assignUser = userMap.get(NumberUtils.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, SysUserBpmVO.class));
                SysDeptVo dept = deptMap.get(assignUser.getDeptId());
                if (dept != null) {
                    taskVO.getAssigneeUser().setDeptName(dept.getName());
                }
            }
            SysUserVo ownerUser = userMap.get(NumberUtils.parseLong(task.getOwner()));
            if (ownerUser != null) {
                taskVO.setOwnerUser(BeanUtils.toBean(ownerUser, SysUserBpmVO.class));
                findAndThen(deptMap, ownerUser.getDeptId(), dept -> taskVO.getOwnerUser().setDeptName(dept.getName()));
            }
        }));
    }

    default BpmMessageSendWhenTaskCreatedReqDTO convert(ProcessInstance processInstance, SysUserVo startUser,
                                                        Task task) {
        BpmMessageSendWhenTaskCreatedReqDTO reqDTO = new BpmMessageSendWhenTaskCreatedReqDTO();
        reqDTO.setProcessInstanceId(processInstance.getProcessInstanceId())
                .setProcessInstanceName(processInstance.getName()).setStartUserId(startUser.getId())
                .setStartUserNickname(startUser.getName()).setTaskId(task.getId()).setTaskName(task.getName())
                .setAssigneeUserId(NumberUtils.parseLong(task.getAssignee()));
        return reqDTO;
    }

    /**
     * 将父任务的属性，拷贝到子任务（加签任务）
     * <p>
     * 为什么不使用 mapstruct 映射？因为 TaskEntityImpl 还有很多其他属性，这里我们只设置我们需要的。
     * 使用 mapstruct 会将里面嵌套的各个属性值都设置进去，会出现意想不到的问题。
     *
     * @param parentTask 父任务
     * @param childTask  加签任务
     */
    default void copyTo(TaskEntityImpl parentTask, TaskEntityImpl childTask) {
        childTask.setName(parentTask.getName());
        childTask.setDescription(parentTask.getDescription());
        childTask.setCategory(parentTask.getCategory());
        childTask.setParentTaskId(parentTask.getId());
        childTask.setProcessDefinitionId(parentTask.getProcessDefinitionId());
        childTask.setProcessInstanceId(parentTask.getProcessInstanceId());
        childTask.setTaskDefinitionKey(parentTask.getTaskDefinitionKey());
        childTask.setTaskDefinitionId(parentTask.getTaskDefinitionId());
        childTask.setPriority(parentTask.getPriority());
        childTask.setCreateTime(new Date());
        childTask.setTenantId(parentTask.getTenantId());
    }

}
