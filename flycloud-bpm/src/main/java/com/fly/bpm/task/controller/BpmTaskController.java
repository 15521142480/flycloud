package com.fly.bpm.task.controller;

import cn.hutool.core.collection.CollUtil;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.vo.task.*;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.common.service.IBpmFormService;
import com.fly.bpm.flowable.convert.BpmTaskConvert;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.bpm.task.service.BpmTaskService;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.fly.common.utils.collection.CollectionUtils.*;

/**
 * BPM 流程工作项 (任务实例)
 *
 * @author fly
 * @date 2024-11-24
 */
@Tag(name = "管理后台 - 流程工作项")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class BpmTaskController {

    
    private final BpmTaskService taskService;
    private final BpmInstanceService instanceService;
    private final IBpmFormService formService;
    private final BpmProcessDefinitionService bpmProcessDefinitionService;
    private final ISysUserApi sysUserApi;
    private final ISysDeptApi deptApi;


    /**
     * todo 获取待办任务分页
     *
     * @param pageVO 流程任务的的分页参数
    */
    @GetMapping("/todoPage")
    @Operation(summary = "获取 Todo 待办任务分页")
    @PreAuthorize("@pms.hasPermission('bpm:audit:todo:list')")
    public R<PageVo<BpmTaskRespVO>> getTaskTodoPage(@Valid BpmTaskPageReqVO pageVO) {

        PageVo<Task> pageVo = taskService.getTaskTodoPage(UserUtils.getCurUserId(), pageVO);
        if (CollUtil.isEmpty(pageVo.getList())) {
            return R.ok(PageVo.empty());
        }

        // 拼接数据
        Map<String, ProcessInstance> processInstanceMap = instanceService.getProcessInstanceMap(
                convertSet(pageVo.getList(), Task::getProcessInstanceId));
        Map<Long, SysUserVo> userMap = sysUserApi.getUserMapByIds(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));

        return R.ok(BpmTaskConvert.INSTANCE.buildTodoTaskPage(pageVo, processInstanceMap, userMap));
    }


    /**
     * todo 获取已办任务分页
     *
     * @param pageVO 流程任务的的分页参数
     */
    @GetMapping("/donePage")
    @PreAuthorize("@pms.hasPermission('bpm:audit:todo:list')")
    public R<PageVo<BpmTaskRespVO>> getTaskDonePage(@Valid BpmTaskPageReqVO pageVO) {

        PageVo<HistoricTaskInstance> pageVo = taskService.getTaskDonePage(UserUtils.getCurUserId(), pageVO);
        if (CollUtil.isEmpty(pageVo.getList())) {
            return R.ok(PageVo.empty());
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = instanceService.getHistoricProcessInstanceMap(
                convertSet(pageVo.getList(), HistoricTaskInstance::getProcessInstanceId));
        Map<Long, SysUserVo> userMap = sysUserApi.getUserMapByIds(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));

        return R.ok(BpmTaskConvert.INSTANCE.buildTaskPage(pageVo, processInstanceMap, userMap, null));
    }


    /**
     * todo 获取全部任务的分页
     *
     * 注意: 用于【流程任务】菜单
     *
     * @param pageVO 流程任务的的分页参数
     */
    @GetMapping("/managerPage")
    @PreAuthorize("@pms.hasPermission('bpm:manage:task:list')")
    public R<PageVo<BpmTaskRespVO>> getTaskManagerPage(@Valid BpmTaskPageReqVO pageVO) {

        PageVo<HistoricTaskInstance> pageVo = taskService.getTaskPage(UserUtils.getCurUserId(), pageVO);
        if (CollUtil.isEmpty(pageVo.getList())) {
            return R.ok(PageVo.empty());
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = instanceService.getHistoricProcessInstanceMap(
                convertSet(pageVo.getList(), HistoricTaskInstance::getProcessInstanceId));
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId()));
        userIds.addAll(convertSet(pageVo.getList(), task -> NumberUtils.parseLong(task.getAssignee())));
        Map<Long, SysUserVo> userMap = sysUserApi.getUserMapByIds(userIds);
        Map<Long, SysDeptVo> deptMap = deptApi.getDeptMapByIds(convertSet(userMap.values(), SysUserVo::getDeptId));

        return R.ok(BpmTaskConvert.INSTANCE.buildTaskPage(pageVo, processInstanceMap, userMap, deptMap));
    }


    /**
     * todo 获取工作项列表
     *
     * 注意: 包括完成的、未完成的
     *
     * @param processInstanceId 流程实例的编号
    */
    @GetMapping("/taskList")
    public R<List<BpmTaskRespVO>> getTaskListByProcessInstanceId(@RequestParam("processInstanceId") String processInstanceId) {

        List<HistoricTaskInstance> taskList = taskService.getTaskListByProcessInstanceId(processInstanceId, true);
        if (CollUtil.isEmpty(taskList)) {
            return R.ok(Collections.emptyList());
        }

        // 拼接数据
        HistoricProcessInstance processInstance = instanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSetByFlatMap(taskList, task ->
                Stream.of(NumberUtils.parseLong(task.getAssignee()), NumberUtils.parseLong(task.getOwner())));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        Map<Long, SysUserVo> userMap = sysUserApi.getUserMapByIds(userIds);
        Map<Long, SysDeptVo> deptMap = deptApi.getDeptMapByIds(
                convertSet(userMap.values(), SysUserVo::getDeptId));

        // 获得 Form Map
        Map<Long, BpmForm> formMap = formService.getFormMap(
                convertSet(taskList, task -> NumberUtils.parseLong(task.getFormKey())));

        return R.ok(BpmTaskConvert.INSTANCE.buildTaskListByProcessInstanceId(taskList, formMap, userMap, deptMap));
    }



    /**
     * todo 通过
     *
     * @param reqVO 通过流程任务参数
    */
    @PutMapping("/approve")
    public R<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(UserUtils.getCurUserId(), reqVO);
        return R.ok();
    }


    /**
     * todo 不通过
     *
     * @param reqVO 不通过流程任务参数
     */
    @PutMapping("/reject")
    public R<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(UserUtils.getCurUserId(), reqVO);
        return R.ok(true);
    }


    /**
     * todo 获取所有可回退的节点
     *
     *
     * @param id 工作项id
     */
    @GetMapping("/listByReturn/{id}")
    public R<List<BpmTaskRespVO>> getTaskListByReturn(@PathVariable String id) {

        List<UserTask> userTaskList = taskService.getUserTaskListByReturn(id);
        return R.ok(convertList(userTaskList, userTask -> // 只返回 id 和 name
                new BpmTaskRespVO().setName(userTask.getName()).setTaskDefinitionKey(userTask.getId())));
    }


    /**
     * todo 回退
     *
     * 注意: 用于【流程详情】的【回退】按钮
     *
     * @param reqVO 回退流程任务参数
     */
    @PutMapping("/return")
    @Operation(summary = "回退任务", description = "用于【流程详情】的【回退】按钮")
    public R<Boolean> returnTask(@Valid @RequestBody BpmTaskReturnReqVO reqVO) {
        taskService.returnTask(UserUtils.getCurUserId(), reqVO);
        return R.ok(true);
    }


    /**
     * todo 委派
     *
     * 注意: 用于【流程详情】的【委派】按钮
     *
     * @param reqVO 委派流程任参数
    */
    @PutMapping("/delegate")
    public R<Boolean> delegateTask(@Valid @RequestBody BpmTaskDelegateReqVO reqVO) {
        taskService.delegateTask(UserUtils.getCurUserId(), reqVO);
        return R.ok(true);
    }


    /**
     * todo 转派
     *
     * 注意: 用于【流程详情】的【转派】按钮
     *
     * @param reqVO 流程任务的转办参数
    */
    @PutMapping("/transfer")
    public R<Boolean> transferTask(@Valid @RequestBody BpmTaskTransferReqVO reqVO) {
        taskService.transferTask(UserUtils.getCurUserId(), reqVO);
        return R.ok(true);
    }


    /**
     * todo 加签
     *
     * 注意: before 前加签，after 后加签
     *
     * @param reqVO 加签任务的创建（加签）参数
    */
    @PutMapping("/createSign")
    public R<Boolean> createSignTask(@Valid @RequestBody BpmTaskSignCreateReqVO reqVO) {
        taskService.createSignTask(UserUtils.getCurUserId(), reqVO);
        return R.ok(true);
    }


    /**
     * todo 减签
     *
     * 注意: before 前加签，after 后加签
     *
     * @param reqVO 加签任务的删除（减签）参数
     */
    @DeleteMapping("/deleteSign")
    public R<Boolean> deleteSignTask(@Valid @RequestBody BpmTaskSignDeleteReqVO reqVO) {
        taskService.deleteSignTask(UserUtils.getCurUserId(), reqVO);
        return R.ok(true);
    }


    /**
     * todo 根据父级任务编号获取子任务列表
     *
     * 注意: 目前用于，减签的时候，获得子任务列表
     *
     * @param parentTaskId 父级任务编号
    */
    @GetMapping("/listByParentTaskId/{parentTaskId}")
    public R<List<BpmTaskRespVO>> getTaskListByParentTaskId(@PathVariable String parentTaskId) {

        List<Task> taskList = taskService.getTaskListByParentTaskId(parentTaskId);
        if (CollUtil.isEmpty(taskList)) {
            return R.ok(Collections.emptyList());
        }
        // 拼接数据
        Map<Long, SysUserVo> userMap = sysUserApi.getUserMapByIds(convertSetByFlatMap(taskList,
                user -> Stream.of(NumberUtils.parseLong(user.getAssignee()), NumberUtils.parseLong(user.getOwner()))));
        Map<Long, SysDeptVo> deptMap = deptApi.getDeptMapByIds(
                convertSet(userMap.values(), SysUserVo::getDeptId));

        return R.ok(BpmTaskConvert.INSTANCE.buildTaskListByParentTaskId(taskList, userMap, deptMap));
    }

}
