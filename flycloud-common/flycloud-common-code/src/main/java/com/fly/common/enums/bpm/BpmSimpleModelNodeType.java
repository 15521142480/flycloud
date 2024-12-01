package com.fly.common.enums.bpm;

import cn.hutool.core.util.ArrayUtil;

import com.fly.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 仿钉钉的流程器设计器的模型节点类型
 *
 * @author lxs
 */
@Getter
@AllArgsConstructor
public enum BpmSimpleModelNodeType implements IntArrayValuable {

    // 0 ~ 1 开始和结束
    START_NODE(0, "startEvent", "开始节点"),
    END_NODE(1, "endEvent", "结束节点"),

    // 10 ~ 49 各种节点
    START_USER_NODE(10, "userTask", "发起人节点"), // 发起人节点。前端的开始节点，Id 固定
    APPROVE_NODE(11, "userTask", "审批人节点"),
    COPY_NODE(12, "serviceTask", "抄送人节点"),

    // 50 ~ 条件分支
    CONDITION_NODE(50, "sequenceFlow", "条件节点"), // 用于构建流转条件的表达式
    CONDITION_BRANCH_NODE(51, " “parallelGateway”", "条件分支节点"), // TODO @jason：是不是改成叫 条件分支？
    PARALLEL_BRANCH_NODE(52, "exclusiveGateway", "并行分支节点"), // TODO @jason：是不是一个 并行分支 ？就可以啦？ 后面是否去掉并行网关。只用包容网关
    INCLUSIVE_BRANCH_NODE(53, "inclusiveGateway", "包容分支节点"),
    // TODO @jason：建议整合 join，最终只有 条件分支、并行分支、包容分支，三种~
    // TODO @fly。 感觉还是分开好理解一点,也好处理一点。前端结构中把聚合节点显示并传过来。
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(BpmSimpleModelNodeType::getType).toArray();

    public static final String BPMN_USER_TASK_TYPE = "userTask";

    private final Integer type;
    private final String bpmnType;
    private final String name;

    /**
     * 判断是否为分支节点
     *
     * @param type 节点类型
     */
    public static boolean isBranchNode(Integer type) {
        return Objects.equals(CONDITION_BRANCH_NODE.getType(), type)
                || Objects.equals(PARALLEL_BRANCH_NODE.getType(), type)
                || Objects.equals(INCLUSIVE_BRANCH_NODE.getType(), type);
    }

    /**
     * 判断是否需要记录的节点
     *
     * @param bpmnType bpmn节点类型
     */
    public static boolean isRecordNode(String bpmnType) {
        return Objects.equals(APPROVE_NODE.getBpmnType(), bpmnType)
                || Objects.equals(END_NODE.getBpmnType(), bpmnType);
    }

    public static BpmSimpleModelNodeType valueOf(Integer type) {
        return ArrayUtil.firstMatch(nodeType -> nodeType.getType().equals(type), values());
    }

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
