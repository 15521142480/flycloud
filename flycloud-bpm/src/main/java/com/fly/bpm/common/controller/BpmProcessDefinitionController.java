package com.fly.bpm.common.controller;

import cn.hutool.core.collection.CollUtil;
import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.common.service.IBpmCategoryService;
import com.fly.bpm.common.service.IBpmFormService;
import com.fly.bpm.flowable.candidate.strategy.dept.BpmTaskCandidateStartUserSelectStrategy;
import com.fly.bpm.flowable.convert.BpmProcessDefinitionConvert;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.collection.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.bpm.api.domain.bo.BpmProcessDefinitionInfoBo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * BPM 流程定义的信息控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/processDefinition")
public class BpmProcessDefinitionController extends BaseController {


    private final BpmProcessDefinitionService bpmProcessDefinitionService;

    private final IBpmCategoryService bpmCategoryService;

    private final IBpmFormService bpmFormService;


    /**
     * 获得流程定义分页
     */
    @GetMapping("/page")
    public R<PageVo<BpmProcessDefinitionRespVO>> list(BpmProcessDefinitionInfoBo bo, PageBo page) {

            PageVo<ProcessDefinition> pageResult = bpmProcessDefinitionService.queryPageList(bo, page);
            if (CollUtil.isEmpty(pageResult.getList())) {
                return R.ok(PageVo.empty(pageResult.getTotal()));
            }

            // 获得 Category Map
            Map<String, BpmCategory> categoryMap = bpmCategoryService.getCategoryMap(
                    CollectionUtils.convertSet(pageResult.getList(), ProcessDefinition::getCategory));

            // 获得 Deployment Map
            Map<String, Deployment> deploymentMap = bpmProcessDefinitionService.getDeploymentMap(
                    CollectionUtils.convertSet(pageResult.getList(), ProcessDefinition::getDeploymentId));

            // 获得 BpmProcessDefinitionInfoDO Map
            Map<String, BpmProcessDefinitionInfo> processDefinitionMap = bpmProcessDefinitionService.getProcessDefinitionInfoMap(
                    CollectionUtils.convertSet(pageResult.getList(), ProcessDefinition::getId));
            // 获得 Form Map
            Map<Long, BpmForm> formMap = bpmFormService.getFormMap(
                    CollectionUtils.convertSet(processDefinitionMap.values(), BpmProcessDefinitionInfo::getFormId));

            return R.ok(BpmProcessDefinitionConvert.INSTANCE.buildProcessDefinitionPage(
                    pageResult, deploymentMap, processDefinitionMap, formMap, categoryMap));
    }



    /**
     * 获得流程定义列表
     *
     * @param suspensionState 挂起状态; 参见 Flowable SuspensionState 枚举
     */
    @GetMapping("/list")
    public R<List<BpmProcessDefinitionRespVO>> getProcessDefinitionList(@RequestParam("suspensionState") Integer suspensionState) {

        // 1.1 获得开启的流程定义
        List<ProcessDefinition> list = bpmProcessDefinitionService.getProcessDefinitionListBySuspensionState(suspensionState);
        if (CollUtil.isEmpty(list)) {
            return R.ok(Collections.emptyList());
        }

        // 1.2 移除不可见的流程定义
        Long userId = UserUtils.getCurUserId();
        Map<String, BpmProcessDefinitionInfo> processDefinitionMap = bpmProcessDefinitionService.getProcessDefinitionInfoMap(
                CollectionUtils.convertSet(list, ProcessDefinition::getId));

        list.removeIf(processDefinition -> {
            BpmProcessDefinitionInfo processDefinitionInfo = processDefinitionMap.get(processDefinition.getId());
            return processDefinitionInfo == null // 不存在
                    || Boolean.FALSE.equals(processDefinitionInfo.getVisible()) // visible 不可见
                    || !bpmProcessDefinitionService.canUserStartProcessDefinition(processDefinitionInfo, userId); // 无权限发起
        });

        // 2. 拼接 VO 返回
        return R.ok(BpmProcessDefinitionConvert.INSTANCE.buildProcessDefinitionList(list, null, processDefinitionMap, null, null));
    }


    /**
     * 获得流程定义
     *
     * @param id 流程编号
     * @param key 流程定义标识
    */
    @GetMapping ("/get")
    public R<BpmProcessDefinitionRespVO> getProcessDefinition(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "key", required = false) String key)
    {

        ProcessDefinition processDefinition = id != null ? bpmProcessDefinitionService.getProcessDefinition(id)
                : bpmProcessDefinitionService.getActiveProcessDefinition(key);
        if (processDefinition == null) {
            return R.ok();
        }

        BpmProcessDefinitionInfo processDefinitionInfo = bpmProcessDefinitionService.getProcessDefinitionInfo(processDefinition.getId());
        BpmnModel bpmnModel = bpmProcessDefinitionService.getProcessDefinitionBpmnModel(processDefinition.getId());

        return R.ok(BpmProcessDefinitionConvert.INSTANCE.buildProcessDefinition(
                processDefinition, null, processDefinitionInfo, null, null, bpmnModel));
    }


}
