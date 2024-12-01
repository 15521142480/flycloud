package com.fly.bpm.flowable.convert;

import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import com.fly.bpm.api.domain.vo.instance.BpmProcessInstanceRespVO;
import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.flowable.event.BpmProcessInstanceStatusEvent;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.collection.MapUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.bo.SysDeptBo;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 流程实例 Convert
 *
 */
@Mapper
public interface BpmProcessInstanceConvert {

    BpmProcessInstanceConvert INSTANCE = Mappers.getMapper(BpmProcessInstanceConvert.class);


    /**
     *
     */
    default PageVo<BpmProcessInstanceRespVO> buildProcessInstancePage(PageVo<HistoricProcessInstance> pageResult,
                                                                      Map<String, ProcessDefinition> processDefinitionMap,
                                                                      Map<String, BpmCategory> categoryMap,
                                                                      Map<String, List<Task>> taskMap,
                                                                      Map<Long, SysUser> userMap,
                                                                      Map<Long, SysDeptBo> deptMap) {

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
                SysUser startUser = userMap.get(NumberUtils.parseLong(pageResult.getList().get(i).getStartUserId()));
                if (startUser != null) {
                    respVO.setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
                    MapUtils.findAndThen(deptMap, startUser.getDeptId(), dept -> respVO.getStartUser().setDeptName(dept.getName()));
                }
            }
        }

        return vpPageResult;
    }


    /**
     *
     */
    default BpmProcessInstanceRespVO buildProcessInstance(HistoricProcessInstance processInstance,
                                                          ProcessDefinition processDefinition,
                                                          BpmProcessDefinitionInfo processDefinitionExt,
                                                          String bpmnXml,
                                                          SysUser startUser,
                                                          SysDeptBo dept) {
        BpmProcessInstanceRespVO respVO = BeanUtils.toBean(processInstance, BpmProcessInstanceRespVO.class);
        respVO.setStatus(FlowableUtils.getProcessInstanceStatus(processInstance));
        respVO.setFormVariables(FlowableUtils.getProcessInstanceFormVariable(processInstance));
        // definition
        respVO.setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class));
        copyTo(processDefinitionExt, respVO.getProcessDefinition());
        respVO.getProcessDefinition().setBpmnXml(bpmnXml);
        // user
        if (startUser != null) {
            respVO.setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
            if (dept != null) {
                respVO.getStartUser().setDeptName(dept.getName());
            }
        }
        return respVO;
    }

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionInfo from, @MappingTarget BpmProcessDefinitionRespVO to);


    default BpmProcessInstanceStatusEvent buildProcessInstanceStatusEvent(Object source, ProcessInstance instance, Integer status) {;
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

}
