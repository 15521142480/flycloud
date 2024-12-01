package com.fly.bpm.api.domain.bo.task;

import com.fly.bpm.api.domain.vo.instance.BpmApprovalDetailRespVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 已经进行中的审批节点 Response BO
 *
 * @author: lxs
 * @date: 2024/11/29
 */
@Data
@Accessors(chain = true)
public class AlreadyRunApproveNodeRespBO {

    /**
     * 审批节点信息数组
     */
    private List<BpmApprovalDetailRespVO.ApprovalNodeInfo> approveNodes;

    /**
     * 已运行的节点 ID 数组 (对应 Bpmn XML 节点 id)
     */
    private Set<String> runNodeIds;

    /**
     * 正在运行的节点的审批信息（key: activityId, value: 审批信息）
     * <p>
     * 用于依次审批，需要加上候选人信息
     */
    private Map<String, BpmApprovalDetailRespVO.ApprovalNodeInfo> runningApprovalNodes;
}
