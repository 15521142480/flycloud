package com.fly.bpm.task.controller;

import cn.hutool.core.collection.CollUtil;
import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.api.domain.vo.instance.*;
import com.fly.bpm.api.domain.vo.task.BpmProcessInstanceBpmnModelViewRespVO;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.common.service.IBpmCategoryService;
import com.fly.bpm.flowable.convert.BpmProcessInstanceConvert;
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
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.fly.common.utils.collection.CollectionUtils.convertList;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

/**
 * BPM 流程实例
 *
 * @author fly
 * @date 2024-11-24
 */
@Tag(name = "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/instance")
@Validated
@RequiredArgsConstructor
public class BpmInstanceController {

    private final BpmInstanceService bpmInstanceService;
    private final BpmTaskService taskService;
    private final BpmProcessDefinitionService processDefinitionService;
    private final IBpmCategoryService categoryService;
    private final ISysUserApi sysUserApi;
    private final ISysDeptApi sysDeptApi;


    /**
     * todo 获得我的实例分页列表
     *
     * 注意: 在【我的流程】菜单中，进行调用
     *
     * @param pageReqVO 流程实例分页参数
    */
    @GetMapping("/myPage")
    @PreAuthorize("@pms.hasPermission('bpm:manage:my:list')")
    public R<PageVo<BpmProcessInstanceRespVO>> getProcessInstanceMyPage(@Valid BpmProcessInstancePageReqVO pageReqVO) {

        PageVo<HistoricProcessInstance> pageVo = bpmInstanceService.getProcessInstancePage(UserUtils.getCurUserId(), pageReqVO);
        if (CollUtil.isEmpty(pageVo.getList())) {
            return R.ok(PageVo.empty(pageVo.getTotal()));
        }

        // 拼接返回
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(
                convertList(pageVo.getList(), HistoricProcessInstance::getId));
        Map<String, ProcessDefinition> processDefinitionMap = processDefinitionService.getProcessDefinitionMap(
                convertSet(pageVo.getList(), HistoricProcessInstance::getProcessDefinitionId));
        Map<String, BpmCategory> categoryMap = categoryService.getCategoryMap(
                convertSet(processDefinitionMap.values(), ProcessDefinition::getCategory));

        return R.ok(BpmProcessInstanceConvert.INSTANCE.buildProcessInstancePage(pageVo, processDefinitionMap, categoryMap, taskMap, null, null));
    }


    /**
     * todo 获得管理流程实例的分页列表
     *
     * 注意: 在【流程实例】菜单中，进行调用
     *
     * @param pageReqVO 流程实例分页参数
    */
    @GetMapping("/managerPage")
    @PreAuthorize("@pms.hasPermission('bpm:manage:instance:list')")
    public R<PageVo<BpmProcessInstanceRespVO>> getProcessInstanceManagerPage(@Valid BpmProcessInstancePageReqVO pageReqVO) {

        PageVo<HistoricProcessInstance> pageVo = bpmInstanceService.getProcessInstancePage(null, pageReqVO);
        if (CollUtil.isEmpty(pageVo.getList())) {
            return R.ok(PageVo.empty(pageVo.getTotal()));
        }

        // 拼接返回
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(
                convertList(pageVo.getList(), HistoricProcessInstance::getId));
        Map<String, ProcessDefinition> processDefinitionMap = processDefinitionService.getProcessDefinitionMap(
                convertSet(pageVo.getList(), HistoricProcessInstance::getProcessDefinitionId));
        Map<String, BpmCategory> categoryMap = categoryService.getCategoryMap(
                convertSet(processDefinitionMap.values(), ProcessDefinition::getCategory));
        // 发起人信息
        Map<Long, SysUserVo> userMap = sysUserApi.getUserMapByIds(
                convertSet(pageVo.getList(), processInstance -> NumberUtils.parseLong(processInstance.getStartUserId())));
        Map<Long, SysDeptVo> deptMap = sysDeptApi.getDeptMapByIds(convertSet(userMap.values(), SysUserVo::getDeptId));

        return R.ok(BpmProcessInstanceConvert.INSTANCE.buildProcessInstancePage(pageVo, processDefinitionMap, categoryMap, taskMap, userMap, deptMap));
    }


    /**
     * todo 获得指定流程实例
     *
     * 注意: 在【流程详细】界面中，进行调用
     *
     * @param id 实例id
    */
    @GetMapping("/get/{id}")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    public R<BpmProcessInstanceRespVO> getProcessInstance(@PathVariable("id") String id) {

        HistoricProcessInstance processInstance = bpmInstanceService.getHistoricProcessInstance(id);
        if (processInstance == null) {
            return R.ok();
        }

        // 拼接返回
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinition(
                processInstance.getProcessDefinitionId());
        BpmProcessDefinitionInfo processDefinitionInfo = processDefinitionService.getProcessDefinitionInfo(
                processInstance.getProcessDefinitionId());

        SysUserVo startUser = sysUserApi.getUserById(NumberUtils.parseLong(processInstance.getStartUserId())).getCheckedData();
        SysDeptVo dept = null;
        if (startUser != null && startUser.getDeptId() != null) {
            dept = sysDeptApi.getDeptById(startUser.getDeptId()).getCheckedData();
        }

        return R.ok(BpmProcessInstanceConvert.INSTANCE.buildProcessInstance(processInstance, processDefinition, processDefinitionInfo, startUser, dept));
    }


    /**
     * todo 新建流程实例
     *
     *
     * @param createReqVO 流程实例的创建参数
    */
    @PostMapping("/create")
    @PreAuthorize("@pms.hasPermission('bpm:manage:create:create')")
    public R<String> createProcessInstance(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return R.ok(bpmInstanceService.createProcessInstance(UserUtils.getCurUserId(), createReqVO));
    }


    /**
     * todo 获得审批详情
     *
     * @param reqVO 审批详情参数
    */
    @GetMapping("/getApprovalDetail")
    @PreAuthorize("@pms.hasPermission('bpm:manage:instance:detail')")
    public R<BpmApprovalDetailRespVO> getApprovalDetail(@Valid BpmApprovalDetailReqVO reqVO) {
        return R.ok(bpmInstanceService.getApprovalDetail(UserUtils.getCurUserId(), reqVO));
    }


    /**
     * todo 用户取消流程实例
     *
     * @param cancelReqVO 流程实例的取消参数
    */
    @DeleteMapping("/cancelByStartUser")
    @PreAuthorize("@pms.hasPermission('bpm:manage:my:cancel')")
    @Operation(summary = "用户取消流程实例", description = "取消发起的流程")
    public R<Boolean> cancelProcessInstanceByStartUser(@Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        bpmInstanceService.cancelProcessInstanceByStartUser(UserUtils.getCurUserId(), cancelReqVO);
        return R.ok(true);
    }


    /**
     * todo 管理员取消流程实例
     *
     * @param cancelReqVO 流程实例的取消参数
     */
    @DeleteMapping("/cancelByAdmin")
    @PreAuthorize("@pms.hasPermission('bpm:manage:instance:cancel')")
    @Operation(summary = "管理员取消流程实例", description = "管理员撤回流程")
    public R<Boolean> cancelProcessInstanceByManager(@Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        bpmInstanceService.cancelProcessInstanceByAdmin(UserUtils.getCurUserId(), cancelReqVO);
        return R.ok(true);
    }


    /**
     * todo 获取流程实例的 BPMN 模型视图
     *
     * 注意: 在【流程详细】界面中，进行调用
     *
     * @param id 流程实例的编号
     */
    @GetMapping("/getBpmModeView")
    public R<BpmProcessInstanceBpmnModelViewRespVO> getProcessInstanceBpmnModelView(@RequestParam(value = "id") String id) {
        return R.ok(bpmInstanceService.getProcessInstanceBpmnModelView(id));
    }


}
