package com.fly.bpm.common.controller;

import cn.hutool.core.collection.CollUtil;

import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.vo.model.*;
import com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelNodeVO;
import com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelUpdateReqVO;
import com.fly.bpm.common.service.BpmModelService;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.common.service.IBpmCategoryService;
import com.fly.bpm.common.service.IBpmFormService;
import com.fly.bpm.flowable.convert.BpmModelConvert;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.model.R;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysUserApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Stream;

import static com.fly.common.utils.collection.CollectionUtils.*;
import static com.fly.common.security.util.UserUtils.getCurUserId;

/**
 * 流程模型
 *
 * @author lxs
 */
@Tag(name = "管理后台 - 流程模型")
@RestController
@RequestMapping("/model")
@Validated
@RequiredArgsConstructor
public class BpmModelController {

    private final BpmModelService modelService;

    private final IBpmFormService formService;

    private final IBpmCategoryService categoryService;

    private final BpmProcessDefinitionService processDefinitionService;


    private final ISysUserApi iSysUserProvider;


    /**
     * 获得模型列表
     *
     */
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:list')")
    @GetMapping("/list")
    public R<List<BpmModelRespVO>> getModelPage(@RequestParam(value = "name", required = false) String name) {

        List<Model> list = modelService.getModelList(name);
        if (CollUtil.isEmpty(list)) {
            return R.ok(Collections.emptyList());
        }

        // 获得 Form 表单
        Set<Long> formIds = convertSet(list, model -> {
            BpmModelMetaInfoVO metaInfo = BpmModelConvert.INSTANCE.parseMetaInfo(model);
            return metaInfo != null ? metaInfo.getFormId() : null;
        });
        Map<Long, BpmForm> formMap = formService.getFormMap(formIds);
        // 获得 Category Map
        Map<String, BpmCategory> categoryMap = categoryService.getCategoryMap(
                convertSet(list, Model::getCategory));
        // 获得 Deployment Map
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(
                convertSet(list, Model::getDeploymentId));
        // 获得 ProcessDefinition Map
        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(
                deploymentMap.keySet());
        Map<String, ProcessDefinition> processDefinitionMap = convertMap(processDefinitions, ProcessDefinition::getDeploymentId);
        // 获得 User Map
        Set<Long> userIds = convertSetByFlatMap(list, model -> {
            BpmModelMetaInfoVO metaInfo = BpmModelConvert.INSTANCE.parseMetaInfo(model);
            return metaInfo != null ? metaInfo.getStartUserIds().stream() : Stream.empty();
        });
        Map<Long, SysUserVo> userMap = iSysUserProvider.getUserMapByIds(userIds);
        return R.ok(BpmModelConvert.INSTANCE.buildModelList(list,
                formMap, categoryMap, deploymentMap, processDefinitionMap, userMap));
    }


    /**
     * 获得模型分页
     */
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:list')")
    @GetMapping("/page")
    public R<PageVo<BpmModelRespVO>> getModelPage(BpmModelPageReqVO pageVO) {

        PageVo<Model> pageVo = modelService.getModelPage(pageVO);
        if (CollUtil.isEmpty(pageVo.getList())) {
            return R.ok(PageVo.empty(pageVo.getTotal()));
        }

        // 拼接数据, 获得 Form 表单
        Set<Long> formIds = CollectionUtils.convertSet(pageVo.getList(), model -> {
            BpmModelMetaInfoVO metaInfo = BpmModelConvert.INSTANCE.parseMetaInfo(model);
            return metaInfo != null ? metaInfo.getFormId() : null;
        });

        // 获得 Category Map
        Map<Long, BpmForm> formMap = formService.getFormMap(formIds);
        Map<String, BpmCategory> categoryMap = categoryService.getCategoryMap(
                convertSet(pageVo.getList(), Model::getCategory));

        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        pageVo.getList().forEach(model -> CollectionUtils.addIfNotNull(deploymentIds, model.getDeploymentId()));
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);

        // 获得 ProcessDefinition Map
        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinition> processDefinitionMap = convertMap(processDefinitions, ProcessDefinition::getDeploymentId);

        // 获得 User Map
        Set<Long> userIds = convertSetByFlatMap(pageVo.getList(), model -> {
            BpmModelMetaInfoVO metaInfo = BpmModelConvert.INSTANCE.parseMetaInfo(model);
            return metaInfo != null ? metaInfo.getStartUserIds().stream() : Stream.empty();
        });
        Map<Long, SysUserVo> userMap = iSysUserProvider.getUserMapByIds(userIds);

        return R.ok(BpmModelConvert.INSTANCE.buildModelPage(pageVo,
                formMap, categoryMap, deploymentMap, processDefinitionMap, userMap));
    }


    /**
     * 获得模型
     *
     */
    @GetMapping("/get/{id}")
    public R<BpmModelRespVO> getModel(@PathVariable String id) {

        Model model = modelService.getModel(id);
        if (model == null) {
            return null;
        }
        byte[] bpmnBytes = modelService.getModelBpmnXML(id);
        return R.ok(BpmModelConvert.INSTANCE.buildModel(model, bpmnBytes));
    }


    /**
     * 新建模型
     *
     */
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:create')")
    @PostMapping("/create")
    public R<String> createModel(@Valid @RequestBody BpmModelSaveReqVO createRetVO) {
        return R.ok(modelService.createModel(createRetVO));
    }


    /**
     * 修改模型
     *
     */
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:update')")
    @PutMapping("/update")
    public R<Boolean> updateModel(@Valid @RequestBody BpmModelSaveReqVO modelVO) {
        modelService.updateModel(getCurUserId(), modelVO);
        return R.ok(true);
    }


    /**
     * 部署模型
     *
     */
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:publish')")
    @PostMapping("/deploy/{id}")
    public R<Boolean> deployModel(@PathVariable("id") String id) {

        modelService.deployModel(getCurUserId(), id);
        return R.ok(true);
    }


    /**
     * 修改模型的状态
     *
     * 注意: 实际更新的部署的流程定义的状态
     */
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:enable')")
    @PutMapping("/update-state")
    public R<Boolean> updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(getCurUserId(), reqVO.getId(), reqVO.getState());
        return R.ok(true);
    }

    /**
     * 修改模型的 BPMN XML
     */
    @PutMapping("/update-bpmn")
    @Operation(summary = "修改模型的 BPMN")
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:designModel')")
    public R<Boolean> updateModelBpmn(@Valid @RequestBody BpmModeUpdateBpmnReqVO reqVO) {
        modelService.updateModelBpmnXml(reqVO.getId(), reqVO.getBpmnXml());
        return R.ok(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('bpm:manage:model:delete')")
    public R<Boolean> deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(getCurUserId(), id);
        return R.ok(true);
    }

    // ========== 仿钉钉/飞书的精简模型 =========

    @GetMapping("/simple/get")
    @Operation(summary = "获得仿钉钉流程设计模型")
    @Parameter(name = "modelId", description = "流程模型编号", required = true, example = "a2c5eee0-eb6c-11ee-abf4-0c37967c420a")
    public R<BpmSimpleModelNodeVO> getSimpleModel(@RequestParam("id") String modelId){
        return R.ok(modelService.getSimpleModel(modelId));
    }

    @PostMapping("/simple/update")
    @Operation(summary = "保存仿钉钉流程设计模型")
    @PreAuthorize("@pms.hasPermission('bpm:model:update')")
    public R<Boolean> updateSimpleModel(@Valid @RequestBody BpmSimpleModelUpdateReqVO reqVO) {
        modelService.updateSimpleModel(getCurUserId(), reqVO);
        return R.ok(Boolean.TRUE);
    }

}
