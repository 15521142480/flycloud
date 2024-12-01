package com.fly.bpm.common.service.impl;

import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenTaskCreatedReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenTaskTimeoutReqDTO;
import com.fly.bpm.common.service.BpmMessageService;
import com.fly.bpm.flowable.convert.BpmMessageConvert;
import com.fly.common.enums.bpm.BpmMessageEnum;
import com.fly.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * BPM 消息 Service 实现类
 *
 */
@Service
@Validated
@Slf4j
public class BpmMessageServiceImpl implements BpmMessageService {

//    @Resource
//    private SmsSendApi smsSendApi;
//
//    @Resource
//    private WebProperties webProperties;



    /**
     * 同意
     *
     */
    @Override
    public void sendMessageWhenProcessInstanceApprove(BpmMessageSendWhenProcessInstanceApproveReqDTO reqDTO) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));

        log.info("通过:{}", JsonUtils.toJsonString(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(), BpmMessageEnum.PROCESS_INSTANCE_APPROVE.getSmsTemplateCode(), templateParams)));

//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(),
//                BpmMessageEnum.PROCESS_INSTANCE_APPROVE.getSmsTemplateCode(), templateParams)).checkError();
    }


    /**
     * 不同意
     *
     */
    @Override
    public void sendMessageWhenProcessInstanceReject(BpmMessageSendWhenProcessInstanceRejectReqDTO reqDTO) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("reason", reqDTO.getReason());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));

        log.info("不通过:{}", JsonUtils.toJsonString(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(), BpmMessageEnum.PROCESS_INSTANCE_REJECT.getSmsTemplateCode(), templateParams)));

//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(),
//                BpmMessageEnum.PROCESS_INSTANCE_REJECT.getSmsTemplateCode(), templateParams)).checkError();
    }


    /**
     * 分配
     *
     */
    @Override
    public void sendMessageWhenTaskAssigned(BpmMessageSendWhenTaskCreatedReqDTO reqDTO) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("taskName", reqDTO.getTaskName());
        templateParams.put("startUserNickname", reqDTO.getStartUserNickname());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));

        log.info("分配:{}", JsonUtils.toJsonString(BpmMessageConvert.INSTANCE.convert(reqDTO.getAssigneeUserId(),
                BpmMessageEnum.TASK_ASSIGNED.getSmsTemplateCode(), templateParams)));

//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getAssigneeUserId(),
//                BpmMessageEnum.TASK_ASSIGNED.getSmsTemplateCode(), templateParams)).checkError();
    }



    /**
     * 超时
     *
     */
    @Override
    public void sendMessageWhenTaskTimeout(BpmMessageSendWhenTaskTimeoutReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("taskName", reqDTO.getTaskName());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));

        log.info("超时:{}", JsonUtils.toJsonString(BpmMessageConvert.INSTANCE.convert(reqDTO.getAssigneeUserId(), BpmMessageEnum.TASK_TIMEOUT.getSmsTemplateCode(), templateParams)));

        //        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getAssigneeUserId(),
//                BpmMessageEnum.TASK_TIMEOUT.getSmsTemplateCode(), templateParams)).checkError();
    }


    /**
     * 获取流程审批页面的接口地址
     *
     * @param taskId
    */
    private String getProcessInstanceDetailUrl(String taskId) {

        return "/bpm/process-instance/detail?id=" + taskId;
        //        return webProperties.getAdminUi().getUrl() + "/bpm/process-instance/detail?id=" + taskId;
    }

}
