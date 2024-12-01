package com.fly.bpm.flowable.convert;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.collection.CollectionUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * Bpm 流程定义的 Convert
 *
 */
@Mapper
public interface BpmProcessDefinitionConvert {


    BpmProcessDefinitionConvert INSTANCE = Mappers.getMapper(BpmProcessDefinitionConvert.class);


    /**
     *
     *
     * @param page
     * @param deploymentMap
     * @param processDefinitionInfoMap
     * @param formMap
     * @param categoryMap
    */
    default PageVo<BpmProcessDefinitionRespVO> buildProcessDefinitionPage(PageVo<ProcessDefinition> page,
                                                                          Map<String, Deployment> deploymentMap,
                                                                          Map<String, BpmProcessDefinitionInfo> processDefinitionInfoMap,
                                                                          Map<Long, BpmForm> formMap,
                                                                          Map<String, BpmCategory> categoryMap) {
        List<BpmProcessDefinitionRespVO> list = buildProcessDefinitionList(page.getList(), deploymentMap, processDefinitionInfoMap, formMap, categoryMap);
        return new PageVo<>(list, page.getTotal());
    }


    /**
     *
     *
     * @param list
     * @param deploymentMap
     * @param processDefinitionInfoMap
     * @param formMap
     * @param categoryMap
    */
    default List<BpmProcessDefinitionRespVO> buildProcessDefinitionList(List<ProcessDefinition> list,
                                                                        Map<String, Deployment> deploymentMap,
                                                                        Map<String, BpmProcessDefinitionInfo> processDefinitionInfoMap,
                                                                        Map<Long, BpmForm> formMap,
                                                                        Map<String, BpmCategory> categoryMap) {
        return CollectionUtils.convertList(list, definition -> {
            Deployment deployment = MapUtil.get(deploymentMap, definition.getDeploymentId(), Deployment.class);
            BpmProcessDefinitionInfo processDefinitionInfo = MapUtil.get(processDefinitionInfoMap, definition.getId(), BpmProcessDefinitionInfo.class);
            BpmForm form = null;
            if (processDefinitionInfo != null) {
                form = MapUtil.get(formMap, processDefinitionInfo.getFormId(), BpmForm.class);
            }
            BpmCategory category = MapUtil.get(categoryMap, definition.getCategory(), BpmCategory.class);
            return buildProcessDefinition(definition, deployment, processDefinitionInfo, form, category, null, null);
        });
    }

    /**
     *
     *
     * @param definition
     * @param deployment
     * @param processDefinitionInfo
     * @param form
     * @param category
     * @param bpmnModel
     * @param startUserSelectUserTaskList
    */
    default BpmProcessDefinitionRespVO buildProcessDefinition(ProcessDefinition definition,
                                                              Deployment deployment,
                                                              BpmProcessDefinitionInfo processDefinitionInfo,
                                                              BpmForm form,
                                                              BpmCategory category,
                                                              BpmnModel bpmnModel,
                                                              List<UserTask> startUserSelectUserTaskList) {
        BpmProcessDefinitionRespVO respVO = BeanUtils.toBean(definition, BpmProcessDefinitionRespVO.class);
        respVO.setSuspensionState(definition.isSuspended() ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
        // Deployment
        if (deployment != null) {
            respVO.setDeploymentTime(LocalDateTimeUtil.of(deployment.getDeploymentTime()));
        }
        // BpmProcessDefinitionInfoDO
        if (processDefinitionInfo != null) {
            copyTo(processDefinitionInfo, respVO);
            // Form
            if (form != null) {
                respVO.setFormName(form.getName());
            }
        }
        // Category
        if (category != null) {
            respVO.setCategoryName(category.getName());
        }
        // BpmnModel
        if (bpmnModel != null) {
            respVO.setBpmnXml(BpmnModelUtils.getBpmnXml(bpmnModel));
            respVO.setStartUserSelectTasks(BeanUtils.toBean(startUserSelectUserTaskList, BpmProcessDefinitionRespVO.UserTask.class));
        }
        return respVO;
    }

    /**
     *
     *
     * @param from
     * @param to
    */
    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionInfo from, @MappingTarget BpmProcessDefinitionRespVO to);

}
