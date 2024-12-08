package com.fly.bpm.flowable.convert;

import cn.hutool.core.util.ArrayUtil;
import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.vo.model.BpmModelMetaInfoVO;
import com.fly.bpm.api.domain.vo.model.BpmModelRespVO;
import com.fly.bpm.api.domain.vo.model.BpmModelSaveReqVO;
import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.api.domain.vo.user.UserSimpleBaseVO;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.DateUtils;
import com.fly.common.utils.json.JsonUtils;
import com.fly.system.api.domain.SysUser;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.fly.common.utils.collection.CollectionUtils.convertList;

/**
 * 流程模型 Convert
 *
 */
@Mapper
public interface BpmModelConvert {


    BpmModelConvert INSTANCE = Mappers.getMapper(BpmModelConvert.class);

    /**
     * 构建模型列表
     */
    default List<BpmModelRespVO> buildModelList(List<Model> list,
                                                Map<Long, BpmForm> formMap,
                                                Map<String, BpmCategory> categoryMap,
                                                Map<String, Deployment> deploymentMap,
                                                Map<String, ProcessDefinition> processDefinitionMap,
                                                Map<Long, SysUser> userMap) {
        List<BpmModelRespVO> result = convertList(list, model -> {
            BpmModelMetaInfoVO metaInfo = parseMetaInfo(model);
            BpmForm form = metaInfo != null ? formMap.get(metaInfo.getFormId()) : null;
            BpmCategory category = categoryMap.get(model.getCategory());
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinition processDefinition = model.getDeploymentId() != null ?
                    processDefinitionMap.get(model.getDeploymentId()) : null;
            List<SysUser> startUsers = metaInfo != null ? convertList(metaInfo.getStartUserIds(), userMap::get) : null;
            return buildModel0(model, metaInfo, form, category, deployment, processDefinition, startUsers);
        });
        // 排序
        result.sort(Comparator.comparing(BpmModelMetaInfoVO::getSort));
        return result;
    }


    /**
     * 构建模型分页数据vo
     */
    default PageVo<BpmModelRespVO> buildModelPage(PageVo<Model> PageVo,
                                                  Map<Long, BpmForm> formMap,
                                                  Map<String, BpmCategory> categoryMap, Map<String, Deployment> deploymentMap,
                                                  Map<String, ProcessDefinition> processDefinitionMap,
                                                  Map<Long, SysUser> userMap)
    {
        List<BpmModelRespVO> list = convertList(PageVo.getList(), model -> {
            BpmModelMetaInfoVO metaInfo = parseMetaInfo(model);
            BpmForm form = metaInfo != null ? formMap.get(metaInfo.getFormId()) : null;
            BpmCategory category = categoryMap.get(model.getCategory());
            Deployment deployment = model.getDeploymentId() != null ? deploymentMap.get(model.getDeploymentId()) : null;
            ProcessDefinition processDefinition = model.getDeploymentId() != null ? processDefinitionMap.get(model.getDeploymentId()) : null;
            List<SysUser> startUsers = metaInfo != null ? convertList(metaInfo.getStartUserIds(), userMap::get) : null;
            return buildModel0(model, metaInfo, form, category, deployment, processDefinition, startUsers);
        });

        return new PageVo<>(list, PageVo.getTotal());
    }

    /**
     * 构建模型
     */
    default BpmModelRespVO buildModel(Model model,
                                     byte[] bpmnBytes) {
        BpmModelMetaInfoVO metaInfo = parseMetaInfo(model);
        BpmModelRespVO modelVO = buildModel0(model, metaInfo, null, null, null, null, null);
        if (ArrayUtil.isNotEmpty(bpmnBytes)) {
            modelVO.setBpmnXml(BpmnModelUtils.getBpmnXml(bpmnBytes));
        }
        return modelVO;
    }


    /**
     * 构建模型
     */
    default BpmModelRespVO buildModel0(Model model,
                                       BpmModelMetaInfoVO metaInfo, BpmForm form, BpmCategory category,
                                       Deployment deployment, ProcessDefinition processDefinition,
                                       List<SysUser> startUsers) {
        BpmModelRespVO modelRespVO = new BpmModelRespVO().setId(model.getId()).setName(model.getName())
                .setKey(model.getKey()).setCategory(model.getCategory())
                .setCreateTime(DateUtils.of(model.getCreateTime()));
        // Form
        BeanUtils.copyProperties(metaInfo, modelRespVO);
        if (form != null) {
            modelRespVO.setFormName(form.getName());
        }
        // Category
        if (category != null) {
            modelRespVO.setCategoryName(category.getName());
        }
        // ProcessDefinition
        if (processDefinition != null) {
            modelRespVO.setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class));
            modelRespVO.getProcessDefinition().setSuspensionState(processDefinition.isSuspended() ?
                    SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
            if (deployment != null) {
                modelRespVO.getProcessDefinition().setDeploymentTime(DateUtils.of(deployment.getDeploymentTime()));
            }
        }
        modelRespVO.setStartUsers(BeanUtils.toBean(startUsers, UserSimpleBaseVO.class));
        return modelRespVO;
    }


    /**
     * 赋值模型数据
     */
    default void copyToModel(Model model, BpmModelSaveReqVO reqVO) {
        model.setName(reqVO.getName());
        model.setKey(reqVO.getKey());
        model.setCategory(reqVO.getCategory());
        model.setMetaInfo(JsonUtils.toJsonString(BeanUtils.toBean(reqVO, BpmModelMetaInfoVO.class)));
    }


    /**
     * 模型数据转模型MetaInfo-vo
     */
    default BpmModelMetaInfoVO parseMetaInfo(Model model) {

        BpmModelMetaInfoVO vo = JsonUtils.parseObject(model.getMetaInfo(), BpmModelMetaInfoVO.class);
        if (vo == null) {
            return null;
        }
        if (vo.getManagerUserIds() == null) {
            vo.setManagerUserIds(Collections.emptyList());
        }
        if (vo.getStartUserIds() == null) {
            vo.setStartUserIds(Collections.emptyList());
        }

        return vo;
    }

}
