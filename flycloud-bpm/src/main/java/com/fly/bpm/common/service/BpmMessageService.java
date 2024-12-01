package com.fly.bpm.common.service;


import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenTaskCreatedReqDTO;
import com.fly.bpm.api.domain.dto.message.BpmMessageSendWhenTaskTimeoutReqDTO;

import javax.validation.Valid;

/**
 * BPM 消息 Service 接口
 *
 * TODO 未来支持消息的可配置；不同的流程，在什么场景下，需要发送什么消息，消息的内容是什么；
 *
 */
public interface BpmMessageService {

    /**
     * 发送流程实例被通过的消息
     *
     * @param reqDTO 发送信息
     */
    void sendMessageWhenProcessInstanceApprove(@Valid BpmMessageSendWhenProcessInstanceApproveReqDTO reqDTO);

    /**
     * 发送流程实例被不通过的消息
     *
     * @param reqDTO 发送信息
     */
    void sendMessageWhenProcessInstanceReject(@Valid BpmMessageSendWhenProcessInstanceRejectReqDTO reqDTO);

    /**
     * 发送任务被分配的消息
     *
     * @param reqDTO 发送信息
     */
    void sendMessageWhenTaskAssigned(@Valid BpmMessageSendWhenTaskCreatedReqDTO reqDTO);

    /**
     * 发送任务审批超时的消息
     *
     * @param reqDTO 发送信息
     */
    void sendMessageWhenTaskTimeout(@Valid BpmMessageSendWhenTaskTimeoutReqDTO reqDTO);

}
