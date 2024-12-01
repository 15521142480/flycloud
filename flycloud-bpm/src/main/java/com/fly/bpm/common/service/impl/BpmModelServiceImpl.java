package com.fly.bpm.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.vo.model.BpmModelMetaInfoVO;
import com.fly.bpm.api.domain.vo.model.BpmModelPageReqVO;
import com.fly.bpm.api.domain.vo.model.BpmModelSaveReqVO;
import com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelNodeVO;
import com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelUpdateReqVO;
import com.fly.bpm.common.mapper.BpmFormMapper;
import com.fly.bpm.common.service.BpmModelService;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
//import com.fly.bpm.common.service.IBpmFormService;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateInvoker;
import com.fly.bpm.flowable.convert.BpmModelConvert;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.bpm.flowable.utils.SimpleModelUtils;
import com.fly.common.database.util.PageUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.bpm.BpmModelFormTypeEnum;
import com.fly.common.utils.json.JsonUtils;
import com.fly.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.fly.common.exception.utils.ServiceExceptionUtils.exception;
import static com.fly.common.constant.bpm.ErrorCodeConstants.*;

/**
 * Flowable流程模型 - 实现层
 *
 * @author: lxs
 * @date: 2024/11/29
 */
@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class BpmModelServiceImpl implements BpmModelService {


    private final RepositoryService repositoryService;

    private final BpmProcessDefinitionService processDefinitionService;

//    private final IBpmFormService bpmFormService;
    private final BpmFormMapper bpmFormMapper;

    private final BpmTaskCandidateInvoker taskCandidateInvoker;


    /**
     * 获得流程模型分页
     *
     * @param pageVO
    */
    @Override
    public PageVo<Model> getModelPage(BpmModelPageReqVO pageVO) {

        ModelQuery modelQuery = repositoryService.createModelQuery();
        modelQuery.modelTenantId(FlowableUtils.getTenantId());

        if (StrUtil.isNotBlank(pageVO.getKey())) {
            modelQuery.modelKey(pageVO.getKey());
        }
        if (StrUtil.isNotBlank(pageVO.getName())) {
            modelQuery.modelNameLike("%" + pageVO.getName() + "%"); // 模糊匹配
        }
        if (StrUtil.isNotBlank(pageVO.getCategory())) {
            modelQuery.modelCategory(pageVO.getCategory());
        }

        // 执行查询
        long count = modelQuery.count();
        if (count == 0) {
            return PageVo.empty(count);
        }
        List<Model> models = modelQuery
                .orderByCreateTime().desc()
                .listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());

        return new PageVo<>(models, count);
    }


    /**
     * 创建流程模型
     *
     * @param createReqVO
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createModel(@Valid BpmModelSaveReqVO createReqVO) {

        if (!ValidationUtils.isXmlNCName(createReqVO.getKey())) {
            throw exception(MODEL_KEY_VALID);
        }

        // 1. 校验流程标识已经存在
        Model keyModel = getModelByKey(createReqVO.getKey());
        if (keyModel != null) {
            throw exception(MODEL_KEY_EXISTS, createReqVO.getKey());
        }

        // 2.1 创建流程定义
        Model model = repositoryService.newModel();
        BpmModelConvert.INSTANCE.copyToModel(model, createReqVO);
        model.setTenantId(FlowableUtils.getTenantId());

        // 2.2 保存流程定义
        repositoryService.saveModel(model);

        return model.getId();
    }


    /**
     *
     *
     * @param userId
     * @param updateReqVO
    */
    @Override
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public void updateModel(Long userId, @Valid BpmModelSaveReqVO updateReqVO) {

        // 1. 校验流程模型存在
        Model model = validateModelManager(updateReqVO.getId(), userId);

        // 修改流程定义
        BpmModelConvert.INSTANCE.copyToModel(model, updateReqVO);

        // 更新模型
        repositoryService.saveModel(model);
    }

    /**
     *
     */
    private Model validateModelExists(String id) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        return model;
    }


    /**
     * 校验是否有流程模型的管理权限
     *
     * @param id     流程模型编号
     * @param userId 用户编号
     * @return 流程模型
     */
    private Model validateModelManager(String id, Long userId) {

        Model model = validateModelExists(id);
        BpmModelMetaInfoVO metaInfo = BpmModelConvert.INSTANCE.parseMetaInfo(model);
        if (metaInfo == null || !CollUtil.contains(metaInfo.getManagerUserIds(), userId)) {
            throw exception(MODEL_UPDATE_FAIL_NOT_MANAGER);
        }
        return model;
    }


    /**
     * 发布模型
     *
     * @param userId
     * @param id
    */
    @Override
    @Transactional(rollbackFor = Exception.class) // 因为进行多个操作，所以开启事务
    public void deployModel(Long userId, String id) {

        // 1.1 校验流程模型存在
        Model model = validateModelManager(id, userId);
        // 1.2 校验流程图
        byte[] bpmnBytes = getModelBpmnXML(model.getId());
        validateBpmnXml(bpmnBytes);
        // 1.3 校验表单已配
        BpmModelMetaInfoVO metaInfo = BpmModelConvert.INSTANCE.parseMetaInfo(model);
        BpmForm form = validateFormConfig(metaInfo);
        // 1.4 校验任务分配规则已配置
        taskCandidateInvoker.validateBpmnConfig(bpmnBytes);
        // 1.5 获取仿钉钉流程设计器模型数据
        byte[] simpleBytes = getModelSimpleJson(model.getId());

        // 2.1 创建流程定义
        String definitionId = processDefinitionService.createProcessDefinition(model, metaInfo, bpmnBytes, simpleBytes, form);

        // 2.2 将老的流程定义进行挂起。也就是说，只有最新部署的流程定义，才可以发起任务。
        updateProcessDefinitionSuspended(model.getDeploymentId());

        // 2.3 更新 model 的 deploymentId，进行关联
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(definitionId);
        model.setDeploymentId(definition.getDeploymentId());
        repositoryService.saveModel(model);
    }

    /**
     * 模型校验
     */
    private void validateBpmnXml(byte[] bpmnBytes) {
        BpmnModel bpmnModel = BpmnModelUtils.getBpmnModel(bpmnBytes);
        if (bpmnModel == null) {
            throw exception(MODEL_NOT_EXISTS);
        }
        // 1. 没有 StartEvent
        StartEvent startEvent = BpmnModelUtils.getStartEvent(bpmnModel);
        if (startEvent == null) {
            throw exception(MODEL_DEPLOY_FAIL_BPMN_START_EVENT_NOT_EXISTS);
        }
        // 2. 校验 UserTask 的 name 都配置了
        List<UserTask> userTasks = BpmnModelUtils.getBpmnModelElements(bpmnModel, UserTask.class);
        userTasks.forEach(userTask -> {
            if (StrUtil.isEmpty(userTask.getName())) {
                throw exception(MODEL_DEPLOY_FAIL_BPMN_USER_TASK_NAME_NOT_EXISTS, userTask.getId());
            }
        });
    }


    /**
     *
     *
     * @param userId
     * @param id
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(Long userId, String id) {
        // 校验流程模型存在
        Model model = validateModelManager(id, userId);

        // 执行删除
        repositoryService.deleteModel(id);
        // 禁用流程定义
        updateProcessDefinitionSuspended(model.getDeploymentId());
    }


    /**
     *
     *
     * @param userId
     * @param id
     * @param state
    */
    @Override
    public void updateModelState(Long userId, String id, Integer state) {
        // 1.1 校验流程模型存在
        Model model = validateModelManager(id, userId);
        // 1.2 校验流程定义存在
        ProcessDefinition definition = processDefinitionService.getProcessDefinitionByDeploymentId(model.getDeploymentId());
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }

        // 2. 更新状态
        processDefinitionService.updateProcessDefinitionState(definition.getId(), state);
    }

    @Override
    public BpmnModel getBpmnModelByDefinitionId(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    @Override
    public BpmSimpleModelNodeVO getSimpleModel(String modelId) {
        Model model = validateModelExists(modelId);
        // 通过 ACT_RE_MODEL 表 EDITOR_SOURCE_EXTRA_VALUE_ID_ ，获取仿钉钉快搭模型的 JSON 数据
        byte[] jsonBytes = getModelSimpleJson(model.getId());
        return JsonUtils.parseObject(jsonBytes, BpmSimpleModelNodeVO.class);
    }

    @Override
    public void updateSimpleModel(Long userId, BpmSimpleModelUpdateReqVO reqVO) {
        // 1. 校验流程模型存在
        Model model = validateModelManager(reqVO.getId(), userId);

        // 2.1 JSON 转换成 bpmnModel
        BpmnModel bpmnModel = SimpleModelUtils.buildBpmnModel(model.getKey(), model.getName(), reqVO.getSimpleModel());
        // 2.2 保存 Bpmn XML
        updateModelBpmnXml(model.getId(), BpmnModelUtils.getBpmnXml(bpmnModel));
        // 2.3 保存 JSON 数据
        saveModelSimpleJson(model.getId(), JsonUtils.toJsonByte(reqVO.getSimpleModel()));
    }

    /**
     * 校验流程表单已配置
     *
     * @param metaInfo 流程模型元数据
     * @return 表单配置
     */
    private BpmForm validateFormConfig(BpmModelMetaInfoVO metaInfo) {
        if (metaInfo == null || metaInfo.getFormType() == null) {
            throw exception(MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
        }
        // 校验表单存在
        if (Objects.equals(metaInfo.getFormType(), BpmModelFormTypeEnum.NORMAL.getType())) {
            if (metaInfo.getFormId() == null) {
                throw exception(MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
            }
            BpmForm form = bpmFormMapper.selectById(metaInfo.getFormId());
            if (form == null) {
                throw exception(FORM_NOT_EXISTS);
            }
            return form;
        } else {
            if (StrUtil.isEmpty(metaInfo.getFormCustomCreatePath()) || StrUtil.isEmpty(metaInfo.getFormCustomViewPath())) {
                throw exception(MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG);
            }
            return null;
        }
    }

    /**
     * 修改流程模型的 BPMN XML
     *
     * @param id      编号
     * @param bpmnXml BPMN XML
     */
    @Override
    public void updateModelBpmnXml(String id, String bpmnXml) {
        if (StrUtil.isEmpty(bpmnXml)) {
            return;
        }
        repositoryService.addModelEditorSource(id, StrUtil.utf8Bytes(bpmnXml));
    }

    private byte[] getModelSimpleJson(String id) {
        return repositoryService.getModelEditorSourceExtra(id);
    }

    private void saveModelSimpleJson(String id, byte[] jsonBytes) {
        if (ArrayUtil.isEmpty(jsonBytes)) {
            return;
        }
        repositoryService.addModelEditorSourceExtra(id, jsonBytes);
    }

    /**
     * 挂起 deploymentId 对应的流程定义
     * <p>
     * 注意：这里一个 deploymentId 只关联一个流程定义
     *
     * @param deploymentId 流程发布Id
     */
    private void updateProcessDefinitionSuspended(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return;
        }
        ProcessDefinition oldDefinition = processDefinitionService.getProcessDefinitionByDeploymentId(deploymentId);
        if (oldDefinition == null) {
            return;
        }
        processDefinitionService.updateProcessDefinitionState(oldDefinition.getId(), SuspensionState.SUSPENDED.getStateCode());
    }

    private Model getModelByKey(String key) {
        return repositoryService.createModelQuery()
                .modelTenantId(FlowableUtils.getTenantId())
                .modelKey(key).singleResult();
    }

    @Override
    public Model getModel(String id) {
        return repositoryService.getModel(id);
    }

    @Override
    public byte[] getModelBpmnXML(String id) {
        return repositoryService.getModelEditorSource(id);
    }

}
