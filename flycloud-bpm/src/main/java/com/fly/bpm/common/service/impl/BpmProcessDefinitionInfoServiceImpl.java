package com.fly.bpm.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.vo.model.BpmModelMetaInfoVO;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.constant.bpm.BpmnModelConstants;
import com.fly.common.constant.bpm.ErrorCodeConstants;
import com.fly.common.database.util.PageUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.exception.utils.ServiceExceptionUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.collection.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import com.fly.bpm.api.domain.bo.BpmProcessDefinitionInfoBo;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.common.mapper.BpmProcessDefinitionInfoMapper;
import com.fly.bpm.common.service.IBpmProcessDefinitionService;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Collections.emptyList;

/**
 * BPM 流程定义的信息Service业务层处理
 *
 * @author fly
 * @date 2024-11-24
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BpmProcessDefinitionInfoServiceImpl extends BaseServiceImpl<BpmProcessDefinitionInfoMapper, BpmProcessDefinitionInfo> implements IBpmProcessDefinitionService {

    private final RepositoryService repositoryService;

    private final BpmProcessDefinitionInfoMapper baseMapper;


    /**
     * 获得流程定义分页
     */
    @Override
    public PageVo<ProcessDefinition> queryPageList(BpmProcessDefinitionInfoBo bo, PageBo pageBo) {

        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        query.processDefinitionTenantId(FlowableUtils.getTenantId());
        if (StrUtil.isNotBlank(bo.getKey())) {
            query.processDefinitionKey(bo.getKey());
        }

        // 执行查询
        long count = query.count();
        if (count == 0) {
            return PageVo.empty(count);
        }

        List<ProcessDefinition> list = query.orderByProcessDefinitionVersion()
                .desc()
                .listPage(PageUtils.getStart(pageBo), pageBo.getPageSize());

        return new PageVo<>(list, count);
    }


    @Override
    public List<ProcessDefinition> getProcessDefinitionListBySuspensionState(Integer suspensionState) {
        // 拼接查询条件
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), suspensionState)) {
            query.suspended();
        } else if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), suspensionState)) {
            query.active();
        }
        // 执行查询
        query.processDefinitionTenantId(FlowableUtils.getTenantId());
        return query.list();
    }



    @Override
    public ProcessDefinition getProcessDefinition(String id) {
        return repositoryService.getProcessDefinition(id);
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionList(Set<String> ids) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionIds(ids).list();
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) {
        if (StrUtil.isEmpty(deploymentId)) {
            return null;
        }
        return repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionListByDeploymentIds(Set<String> deploymentIds) {
        if (CollUtil.isEmpty(deploymentIds)) {
            return emptyList();
        }
        return repositoryService.createProcessDefinitionQuery().deploymentIds(deploymentIds).list();
    }

    @Override
    public ProcessDefinition getActiveProcessDefinition(String key) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(FlowableUtils.getTenantId())
                .processDefinitionKey(key).active().singleResult();
    }

    @Override
    public boolean canUserStartProcessDefinition(BpmProcessDefinitionInfo processDefinition, Long userId) {
        if (processDefinition == null) {
            return false;
        }
        // 为空，则所有人都可以发起
        if (CollUtil.isEmpty(processDefinition.getStartUserIds())) {
            return true;
        }
        // 不为空，则需要存在里面
        return processDefinition.getStartUserIds().contains(userId);
    }


    @Override
    public List<Deployment> getDeploymentList(Set<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return emptyList();
        }
        List<Deployment> list = new ArrayList<>(ids.size());
        for (String id : ids) {
            CollectionUtils.addIfNotNull(list, getDeployment(id));
        }
        return list;
    }

    @Override
    public Deployment getDeployment(String id) {
        if (StrUtil.isEmpty(id)) {
            return null;
        }
        return repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
    }

    @Override
    public String createProcessDefinition(Model model, BpmModelMetaInfoVO modelMetaInfo,
                                          byte[] bpmnBytes, byte[] simpleBytes, BpmForm form) {
        // 创建 Deployment 部署
        Deployment deploy = repositoryService.createDeployment()
                .key(model.getKey()).name(model.getName()).category(model.getCategory())
                .addBytes(model.getKey() + BpmnModelConstants.BPMN_FILE_SUFFIX, bpmnBytes)
                .tenantId(FlowableUtils.getTenantId())
                .disableSchemaValidation() // 禁用 XML Schema 验证，因为有自定义的属性
                .deploy();

        // 设置 ProcessDefinition 的 category 分类
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), model.getCategory());
        // 注意 1，ProcessDefinition 的 key 和 name 是通过 BPMN 中的 <bpmn2:process /> 的 id 和 name 决定
        // 注意 2，目前该项目的设计上，需要保证 Model、Deployment、ProcessDefinition 使用相同的 key，保证关联性。
        //          否则，会导致 ProcessDefinition 的分页无法查询到。
        if (!Objects.equals(definition.getKey(), model.getKey())) {
            throw ServiceExceptionUtils.exception(ErrorCodeConstants.PROCESS_DEFINITION_KEY_NOT_MATCH, model.getKey(), definition.getKey());
        }
        if (!Objects.equals(definition.getName(), model.getName())) {
            throw ServiceExceptionUtils.exception(ErrorCodeConstants.PROCESS_DEFINITION_NAME_NOT_MATCH, model.getName(), definition.getName());
        }

        // 插入拓展表
        BpmProcessDefinitionInfo definitionDO = BeanUtils.toBean(modelMetaInfo, BpmProcessDefinitionInfo.class)
                .setModelId(model.getId()).setProcessDefinitionId(definition.getId()).setModelType(modelMetaInfo.getType())
                .setSimpleModel(StrUtil.str(simpleBytes, StandardCharsets.UTF_8));

        if (form != null) {
            definitionDO.setFormFields(form.getFields()).setFormConf(form.getConf());
        }
        baseMapper.insert(definitionDO);

        return definition.getId();
    }

    @Override
    public void updateProcessDefinitionState(String id, Integer state) {
        // 激活
        if (Objects.equals(SuspensionState.ACTIVE.getStateCode(), state)) {
            repositoryService.activateProcessDefinitionById(id, false, null);
            return;
        }
        // 挂起
        if (Objects.equals(SuspensionState.SUSPENDED.getStateCode(), state)) {
            // suspendProcessInstances = false，进行中的任务，不进行挂起。
            // 原因：只要新的流程不允许发起即可，老流程继续可以执行。
            repositoryService.suspendProcessDefinitionById(id, false, null);
            return;
        }

        log.error("[updateProcessDefinitionState][流程定义({}) 修改未知状态({})]", id, state);
    }


    @Override
    public BpmnModel getProcessDefinitionBpmnModel(String id) {
        return repositoryService.getBpmnModel(id);
    }


    /**
     * 获得流程定义的信息
     *
     * @param id 流程定义编号
     * @return 流程定义信息
     */
    @Override
    public BpmProcessDefinitionInfo getProcessDefinitionInfo(String id) {

        LambdaQueryWrapper<BpmProcessDefinitionInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(BpmProcessDefinitionInfo::getProcessDefinitionId, id);
        return baseMapper.selectOne(queryWrapper);
    }


    /**
     * 获得 ids 对应的 ProcessDefinition 数组
     *
     * @param ids 编号的数组
     * @return 流程定义的数组
     */
    @Override
    public List<BpmProcessDefinitionInfo> getProcessDefinitionInfoList(Collection<String> ids) {

        LambdaQueryWrapper<BpmProcessDefinitionInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(BpmProcessDefinitionInfo::getProcessDefinitionId, ids);
        return baseMapper.selectList(queryWrapper);
    }


}