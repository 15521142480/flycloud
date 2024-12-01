package com.fly.bpm.flowable.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelNodeVO;
import com.fly.bpm.flowable.listener.BpmCopyTaskDelegate;
import com.fly.common.constant.bpm.BpmnModelConstants;
import com.fly.common.enums.bpm.BpmBoundaryEventType;
import com.fly.common.enums.bpm.BpmSimpleModeConditionType;
import com.fly.common.enums.bpm.BpmSimpleModelNodeType;
import com.fly.common.enums.bpm.BpmUserTaskApproveMethodEnum;
import com.fly.common.utils.collection.CollectionUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelNodeVO.OperationButtonSetting;
import static com.fly.bpm.api.domain.vo.model.simple.BpmSimpleModelNodeVO.TimeoutHandler;
import static com.fly.common.enums.bpm.BpmSimpleModelNodeType.*;
import static com.fly.common.enums.bpm.BpmUserTaskApproveMethodEnum.*;
import static com.fly.common.enums.bpm.BpmUserTaskApproveTypeEnum.USER;
import static com.fly.common.enums.bpm.BpmUserTaskAssignStartUserHandlerTypeEnum.SKIP;
import static com.fly.common.enums.bpm.BpmUserTaskTimeoutHandlerTypeEnum.REMINDER;
import static com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum.START_USER;
import static com.fly.common.constant.bpm.BpmnModelConstants.*;
import static org.flowable.bpmn.constants.BpmnXMLConstants.*;

/**
 * 仿钉钉快搭模型相关的工具方法
 *
 * @author lxs
 */
public class SimpleModelUtils {

    /**
     * 聚合网关节点 Id 后缀
     */
    public static final String JOIN_GATE_WAY_NODE_ID_SUFFIX = "_join";

    /**
     * 所有审批人同意的表达式
     */
    public static final String ALL_APPROVE_COMPLETE_EXPRESSION = "${ nrOfCompletedInstances >= nrOfInstances }";

    /**
     * 任一一名审批人同意的表达式
     */
    public static final String ANY_OF_APPROVE_COMPLETE_EXPRESSION = "${ nrOfCompletedInstances > 0 }";

    /**
     * 按通过比例完成表达式
     */
    public static final String APPROVE_BY_RATIO_COMPLETE_EXPRESSION = "${ nrOfCompletedInstances/nrOfInstances >= %s}";

    // TODO @yunai：注释需要完善下；

    /**
     * 仿钉钉流程设计模型数据结构(json) 转换成 Bpmn Model (待完善）
     *
     * @param processId       流程标识
     * @param processName     流程名称
     * @param simpleModelNode 仿钉钉流程设计模型数据结构
     * @return Bpmn Model
     */
    public static BpmnModel buildBpmnModel(String processId, String processName, BpmSimpleModelNodeVO simpleModelNode) {
        BpmnModel bpmnModel = new BpmnModel();
        // 不加这个 解析 Message 会报 NPE 异常 .
        bpmnModel.setTargetNamespace(BPMN2_NAMESPACE); // TODO @jason：待定：是不是搞个自定义的 namespace；
        // TODO fly：后续在 review

        Process process = new Process();
        process.setId(processId);
        process.setName(processName);
        process.setExecutable(Boolean.TRUE); // TODO @jason：这个是必须设置的么？
        bpmnModel.addProcess(process);

        // TODO fly：这里可能纠结下，到底前端传递，还是后端创建出来。
        // 目前前端的第一个节点是“发起人节点”。这里构建一个 StartNode，用于创建 Bpmn 的 StartEvent 节点
        BpmSimpleModelNodeVO startNode = buildStartSimpleModelNode();
        startNode.setChildNode(simpleModelNode);
        // 从 前端模型数据结构 SimpleModel 构建 FlowNode 并添加到 Main Process
        traverseNodeToBuildFlowNode(startNode, process);
        // 找到 end event
        EndEvent endEvent = (EndEvent) CollUtil.findOne(process.getFlowElements(), item -> item instanceof EndEvent);

        // 构建并添加节点之间的连线 Sequence Flow
        traverseNodeToBuildSequenceFlow(process, startNode, endEvent.getId());
        // 自动布局
        new BpmnAutoLayout(bpmnModel).execute();
        return bpmnModel;
    }

    private static BpmSimpleModelNodeVO buildStartSimpleModelNode() {
        BpmSimpleModelNodeVO startNode = new BpmSimpleModelNodeVO();
        startNode.setId(START_EVENT_NODE_ID);
        startNode.setName(START_EVENT_NODE_NAME);
        startNode.setType(START_NODE.getType());
        return startNode;
    }

    // TODO @fly：在优化下这个注释
    private static void traverseNodeToBuildSequenceFlow(Process process, BpmSimpleModelNodeVO node, String targetNodeId) {
        // 1.1 无效节点返回
        if (!isValidNode(node)) {
            return;
        }
        // 1.2 END_NODE 直接返回
        BpmSimpleModelNodeType nodeType = BpmSimpleModelNodeType.valueOf(node.getType());
        Assert.notNull(nodeType, "模型节点类型不支持");
        if (nodeType == END_NODE) {
            return;
        }
        // 2.1 情况一：普通节点
        BpmSimpleModelNodeVO childNode = node.getChildNode();
        if (!BpmSimpleModelNodeType.isBranchNode(node.getType())) {
            if (!isValidNode(childNode)) {
                // 2.1.1 普通节点且无孩子节点。分两种情况
                // a.结束节点  b. 条件分支的最后一个节点.与分支节点的孩子节点或聚合节点建立连线。
                if (StrUtil.isNotEmpty(node.getAttachNodeId())) {
                    // 2.1.1.1 如果有附加节点. 需要先建立和附加节点的连线。再建立附加节点和目标节点的连线
                    List<SequenceFlow> sequenceFlows = buildAttachNodeSequenceFlow(node.getId(), node.getAttachNodeId(), targetNodeId);
                    sequenceFlows.forEach(process::addFlowElement);
                } else {
                    SequenceFlow sequenceFlow = buildBpmnSequenceFlow(node.getId(), targetNodeId, null, null, null);
                    process.addFlowElement(sequenceFlow);
                }
            } else {
                // 2.1.2 普通节点且有孩子节点。建立连线
                if (StrUtil.isNotEmpty(node.getAttachNodeId())) {
                    // 2.1.1.2 如果有附加节点. 需要先建立和附加节点的连线。再建立附加节点和目标节点的连线
                    List<SequenceFlow> sequenceFlows = buildAttachNodeSequenceFlow(node.getId(), node.getAttachNodeId(), childNode.getId());
                    sequenceFlows.forEach(process::addFlowElement);
                } else {
                    SequenceFlow sequenceFlow = buildBpmnSequenceFlow(node.getId(), childNode.getId(), null, null, null);
                    process.addFlowElement(sequenceFlow);
                }
                // 递归调用后续节点
                traverseNodeToBuildSequenceFlow(process, childNode, targetNodeId);
            }
        } else {
            // 2.2 情况二：分支节点
            List<BpmSimpleModelNodeVO> conditionNodes = node.getConditionNodes();
            Assert.notEmpty(conditionNodes, "分支节点的条件节点不能为空");
            // 分支终点节点 Id
            String branchEndNodeId = null;
            if (nodeType == CONDITION_BRANCH_NODE) { // 条件分支
                // 分两种情况 1. 分支节点有孩子节点为孩子节点 Id 2. 分支节点孩子为无效节点时 (分支嵌套且为分支最后一个节点) 为分支终点节点Id
                branchEndNodeId = isValidNode(childNode) ? childNode.getId() : targetNodeId;
            } else if (nodeType == PARALLEL_BRANCH_NODE) {  // 并行分支
                // 分支节点：分支终点节点 Id 为程序创建的网关集合节点。目前不会从前端传入。
                branchEndNodeId = node.getId() + JOIN_GATE_WAY_NODE_ID_SUFFIX;
            }
            // TODO 包容网关待实现
            Assert.notEmpty(branchEndNodeId, "分支终点节点 Id 不能为空");
            // 3.1 遍历分支节点. 如下情况:
            // 分支1、A->B->C->D->E 和 分支2、A->D->E。 A为分支节点, D为A孩子节点
            for (BpmSimpleModelNodeVO item : conditionNodes) {
                // TODO @jason：条件分支的情况下，需要分 item 搞的条件，和 conditionNodes 搞的条件
                // @fly 这个是啥意思。 这里的 item 的节点类型为 BpmSimpleModelNodeType.CONDITION_NODE 类型，没有对应的 bpmn 的节点。 仅仅用于构建条件表达式。
                Assert.isTrue(Objects.equals(item.getType(), CONDITION_NODE.getType()), "条件节点类型不符合");
                // 构建表达式,可以为空. 并行分支为空
                String conditionExpression = buildConditionExpression(item);
                BpmSimpleModelNodeVO nextNodeOnCondition = item.getChildNode();
                // 3.2 分支有后续节点, 分支1: A->B->C->D
                if (isValidNode(nextNodeOnCondition)) {
                    // 3.2.1 建立 A->B
                    SequenceFlow sequenceFlow = buildBpmnSequenceFlow(node.getId(), nextNodeOnCondition.getId(),
                            item.getId(), item.getName(), conditionExpression);
                    process.addFlowElement(sequenceFlow);
                    // 3.2.2 递归调用后续节点连线。 建立 B->C->D 的连线
                    traverseNodeToBuildSequenceFlow(process, nextNodeOnCondition, branchEndNodeId);
                } else {
                    // 3.3 分支无后续节点 建立 A->D
                    SequenceFlow sequenceFlow = buildBpmnSequenceFlow(node.getId(), branchEndNodeId,
                            item.getId(), item.getName(), conditionExpression);
                    process.addFlowElement(sequenceFlow);
                }
            }
            // 如果是并行分支。由于是程序创建的聚合网关。需要手工创建聚合网关和下一个节点的连线
            if (nodeType == PARALLEL_BRANCH_NODE) {
                String nextNodeId = isValidNode(childNode) ? childNode.getId() : targetNodeId;
                SequenceFlow sequenceFlow = buildBpmnSequenceFlow(branchEndNodeId, nextNodeId, null, null, null);
                process.addFlowElement(sequenceFlow);
            }
            // 4.递归调用后续节点 继续递归建立 D->E 的连线
            traverseNodeToBuildSequenceFlow(process, childNode, targetNodeId);
        }
    }

    /**
     * 构建有附加节点的连线
     *
     * @param nodeId       当前节点 Id
     * @param attachNodeId 附属节点 Id
     * @param targetNodeId 目标节点 Id
     */
    private static List<SequenceFlow> buildAttachNodeSequenceFlow(String nodeId, String attachNodeId, String targetNodeId) {
        SequenceFlow sequenceFlow = buildBpmnSequenceFlow(nodeId, attachNodeId, null, null, null);
        SequenceFlow attachSequenceFlow = buildBpmnSequenceFlow(attachNodeId, targetNodeId, null, null, null);
        return CollUtil.newArrayList(sequenceFlow, attachSequenceFlow);
    }

    /**
     * 构造条件表达式
     *
     * @param conditionNode 条件节点
     */
    public static String buildConditionExpression(BpmSimpleModelNodeVO conditionNode) {
        BpmSimpleModeConditionType conditionTypeEnum = BpmSimpleModeConditionType.valueOf(conditionNode.getConditionType());
        String conditionExpression = null;
        if (conditionTypeEnum == BpmSimpleModeConditionType.EXPRESSION) {
            conditionExpression = conditionNode.getConditionExpression();
        } else if (conditionTypeEnum == BpmSimpleModeConditionType.RULE) {
            BpmSimpleModelNodeVO.ConditionGroups conditionGroups = conditionNode.getConditionGroups();
            if (conditionGroups != null && CollUtil.isNotEmpty(conditionGroups.getConditions())) {
                List<String> strConditionGroups = conditionGroups.getConditions().stream().map(item -> {
                    if (CollUtil.isNotEmpty(item.getRules())) {
                        Boolean and = item.getAnd();
                        List<String> list = CollectionUtils.convertList(item.getRules(), (rule) -> {
                            // 如果非数值类型加引号
                            String rightSide = NumberUtil.isNumber(rule.getRightSide()) ? rule.getRightSide() : "\"" + rule.getRightSide() + "\"";
                            return String.format(" %s %s var:convertByType(%s,%s)", rule.getLeftSide(), rule.getOpCode(), rule.getLeftSide(), rightSide);
                        });
                        return "(" + CollUtil.join(list, and ? " && " : " || ") + ")";
                    } else {
                        return "";
                    }
                }).collect(Collectors.toList());
                conditionExpression = String.format("${%s}", CollUtil.join(strConditionGroups, conditionGroups.getAnd() ? " && " : " || "));
            }
        }
        // TODO 待增加其它类型
        return conditionExpression;
    }

    private static SequenceFlow buildBpmnSequenceFlow(String sourceId, String targetId, String seqFlowId, String seqName, String conditionExpression) {
        Assert.notEmpty(sourceId, "sourceId 不能为空");
        Assert.notEmpty(targetId, "targetId 不能为空");
        // TODO @jason：如果 seqFlowId 不存在的时候，是不是要生成一个默认的 seqFlowId？ @fly： 貌似不需要,Flowable 会默认生成
        // TODO @jason：如果 name 不存在的时候，是不是要生成一个默认的 name？ @fly： 不需要生成默认的吧？ 这个会在流程图展示的， 一般用户填写的。不好生成默认的吧
        SequenceFlow sequenceFlow = new SequenceFlow(sourceId, targetId);
        if (StrUtil.isNotEmpty(conditionExpression)) {
            sequenceFlow.setConditionExpression(conditionExpression);
        }
        if (StrUtil.isNotEmpty(seqFlowId)) {
            sequenceFlow.setId(seqFlowId);
        }
        if (StrUtil.isNotEmpty(seqName)) {
            sequenceFlow.setName(seqName);
        }
        return sequenceFlow;
    }

    // TODO @fly 改成了 traverseNodeToBuildFlowNode， 连线的叫 traverseNodeToBuildSequenceFlow
    private static void traverseNodeToBuildFlowNode(BpmSimpleModelNodeVO node, Process process) {
        // 判断是否有效节点
        if (!isValidNode(node)) {
            return;
        }
        BpmSimpleModelNodeType nodeType = BpmSimpleModelNodeType.valueOf(node.getType());
        Assert.notNull(nodeType, "模型节点类型不支持");

        List<FlowElement> flowElements = buildFlowNode(node, nodeType);
        flowElements.forEach(process::addFlowElement);

        // 如果不是网关类型的接口， 并且chileNode为空退出
        // 如果是“分支”节点，则递归处理条件
        if (BpmSimpleModelNodeType.isBranchNode(node.getType())
                && ArrayUtil.isNotEmpty(node.getConditionNodes())) {
            node.getConditionNodes().forEach(item -> traverseNodeToBuildFlowNode(item.getChildNode(), process));
        }

        // 如果有“子”节点，则递归处理子节点
        traverseNodeToBuildFlowNode(node.getChildNode(), process);
    }

    public static boolean isValidNode(BpmSimpleModelNodeVO node) {
        return node != null && node.getId() != null;
    }

    public static boolean isSequentialApproveNode(BpmSimpleModelNodeVO node) {
        return APPROVE_NODE.getType().equals(node.getType()) && SEQUENTIAL.getMethod().equals(node.getApproveMethod());
    }

    private static List<FlowElement> buildFlowNode(BpmSimpleModelNodeVO node, BpmSimpleModelNodeType nodeType) {
        List<FlowElement> list = new ArrayList<>();
        switch (nodeType) {
            case START_NODE: { // 开始节点
                StartEvent startEvent = convertStartNode(node);
                list.add(startEvent);
                break;
            }
            case END_NODE: { // 结束节点
                EndEvent endEvent = convertEndNode(node);
                list.add(endEvent);
                break;
            }
            case START_USER_NODE: { // 发起人节点
                UserTask userTask = convertStartUserNode(node);
                list.add(userTask);
                break;
            }
            case APPROVE_NODE: { // 审批节点
                List<FlowElement> flowElements = convertApproveNode(node);
                list.addAll(flowElements);
                break;
            }
            case COPY_NODE: { // 抄送节点
                ServiceTask serviceTask = convertCopyNode(node);
                list.add(serviceTask);
                break;
            }
            case CONDITION_BRANCH_NODE: {
                ExclusiveGateway exclusiveGateway = convertConditionBranchNode(node);
                list.add(exclusiveGateway);
                break;
            }
            case PARALLEL_BRANCH_NODE: {
                List<ParallelGateway> parallelGateways = convertParallelBranchNode(node);
                list.addAll(parallelGateways);
                break;
            }

            case INCLUSIVE_BRANCH_NODE: {
                // TODO jason 待实现
                break;
            }
            default: {
                // TODO 其它节点类型的实现
            }
        }
        return list;
    }

    private static UserTask convertStartUserNode(BpmSimpleModelNodeVO node) {
        return buildBpmnStartUserTask(node);
    }

    private static List<FlowElement> convertApproveNode(BpmSimpleModelNodeVO node) {
        List<FlowElement> flowElements = new ArrayList<>();
        UserTask userTask = buildBpmnUserTask(node);
        flowElements.add(userTask);

        // 添加用户任务的 Timer Boundary Event, 用于任务的审批超时处理
        if (node.getTimeoutHandler() != null && node.getTimeoutHandler().getEnable()) {
            BoundaryEvent boundaryEvent = buildUserTaskTimeoutBoundaryEvent(userTask, node.getTimeoutHandler());
            flowElements.add(boundaryEvent);
        }
        return flowElements;
    }

    /**
     * 添加 UserTask 用户的审批超时 BoundaryEvent 事件
     *
     * @param userTask       审批任务
     * @param timeoutHandler 超时处理器
     * @return BoundaryEvent 超时事件
     */
    private static BoundaryEvent buildUserTaskTimeoutBoundaryEvent(UserTask userTask, TimeoutHandler timeoutHandler) {
        // 1.1 定时器边界事件
        BoundaryEvent boundaryEvent = new BoundaryEvent();
        boundaryEvent.setId("Event-" + IdUtil.fastUUID());
        boundaryEvent.setCancelActivity(false); // 设置关联的任务为不会被中断
        boundaryEvent.setAttachedToRef(userTask);
        // 1.2 定义超时时间、最大提醒次数
        TimerEventDefinition eventDefinition = new TimerEventDefinition();
        eventDefinition.setTimeDuration(timeoutHandler.getTimeDuration());
        if (Objects.equals(REMINDER.getType(), timeoutHandler.getType()) &&
                timeoutHandler.getMaxRemindCount() != null && timeoutHandler.getMaxRemindCount() > 1) {
            eventDefinition.setTimeCycle(String.format("R%d/%s",
                    timeoutHandler.getMaxRemindCount(), timeoutHandler.getTimeDuration()));
        }
        boundaryEvent.addEventDefinition(eventDefinition);

        // 2.1 添加定时器边界事件类型
        addExtensionElement(boundaryEvent, BOUNDARY_EVENT_TYPE, BpmBoundaryEventType.USER_TASK_TIMEOUT.getType().toString());
        // 2.2 添加超时执行动作元素
        addExtensionElement(boundaryEvent, USER_TASK_TIMEOUT_HANDLER_TYPE, StrUtil.toStringOrNull(timeoutHandler.getType()));
        return boundaryEvent;
    }

    private static List<ParallelGateway> convertParallelBranchNode(BpmSimpleModelNodeVO node) {
        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(node.getId());
        // TODO @jason：setName

        // TODO @fly + jason：合并网关；是不是要有条件啥的。微信讨论
        // 并行聚合网关有程序创建。前端不需要传入
        ParallelGateway joinParallelGateway = new ParallelGateway();
        joinParallelGateway.setId(node.getId() + JOIN_GATE_WAY_NODE_ID_SUFFIX);
        return CollUtil.newArrayList(parallelGateway, joinParallelGateway);
    }

    private static ServiceTask convertCopyNode(BpmSimpleModelNodeVO node) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setName(node.getName());
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        serviceTask.setImplementation("${" + BpmCopyTaskDelegate.BEAN_NAME + "}");

        // 添加抄送候选人元素
        addCandidateElements(node.getCandidateStrategy(), node.getCandidateParam(), serviceTask);
        // 添加表单字段权限属性元素
        addFormFieldsPermission(node.getFieldsPermission(), serviceTask);
        return serviceTask;
    }

    /**
     * 给节点添加候选人元素
     */
    private static void addCandidateElements(Integer candidateStrategy, String candidateParam, FlowElement flowElement) {
        addExtensionElement(flowElement, BpmnModelConstants.USER_TASK_CANDIDATE_STRATEGY,
                candidateStrategy == null ? null : candidateStrategy.toString());
        addExtensionElement(flowElement, BpmnModelConstants.USER_TASK_CANDIDATE_PARAM, candidateParam);
    }

    private static ExclusiveGateway convertConditionBranchNode(BpmSimpleModelNodeVO node) {
        Assert.notEmpty(node.getConditionNodes(), "条件分支节点不能为空");
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(node.getId());
        // 寻找默认的序列流
        BpmSimpleModelNodeVO defaultSeqFlow = CollUtil.findOne(node.getConditionNodes(),
                item -> BooleanUtil.isTrue(item.getDefaultFlow()));
        if (defaultSeqFlow != null) {
            exclusiveGateway.setDefaultFlow(defaultSeqFlow.getId());
        }
        return exclusiveGateway;
    }

    private static InclusiveGateway convertInclusiveBranchNode(BpmSimpleModelNodeVO node, Boolean isFork) {
        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(node.getId());
        // TODO @jason：这里是不是 setName 哈；

        // TODO @fly + jason：是不是搞个合并网关；这里微信讨论下，有点奇怪；
        // @fly isFork 为 false 就是合并网关。由前端传入。这个前端暂时还未实现
        if (isFork) {
            Assert.notEmpty(node.getConditionNodes(), "条件节点不能为空");
            // 寻找默认的序列流
            BpmSimpleModelNodeVO defaultSeqFlow = CollUtil.findOne(
                    node.getConditionNodes(), item -> BooleanUtil.isTrue(item.getDefaultFlow()));
            if (defaultSeqFlow != null) {
                inclusiveGateway.setDefaultFlow(defaultSeqFlow.getId());
            }
        }
        return inclusiveGateway;
    }

    private static UserTask buildBpmnUserTask(BpmSimpleModelNodeVO node) {
        UserTask userTask = new UserTask();
        userTask.setId(node.getId());
        userTask.setName(node.getName());

        // 如果不是审批人节点，则直接返回
        addExtensionElement(userTask, USER_TASK_APPROVE_TYPE, StrUtil.toStringOrNull(node.getApproveType()));
        if (ObjectUtil.notEqual(node.getApproveType(), USER.getType())) {
            return userTask;
        }

        // 添加候选人元素
        addCandidateElements(node.getCandidateStrategy(), node.getCandidateParam(), userTask);
        // 添加表单字段权限属性元素
        addFormFieldsPermission(node.getFieldsPermission(), userTask);
        // 添加操作按钮配置属性元素
        addButtonsSetting(node.getButtonsSetting(), userTask);
        // 处理多实例（审批方式）
        processMultiInstanceLoopCharacteristics(node.getApproveMethod(), node.getApproveRatio(), userTask);
        // 添加任务被拒绝的处理元素
        addTaskRejectElements(node.getRejectHandler(), userTask);
        // 添加用户任务的审批人与发起人相同时的处理元素
        addAssignStartUserHandlerType(node.getAssignStartUserHandlerType(), userTask);
        // 添加用户任务的空处理元素
        addAssignEmptyHandlerType(node.getAssignEmptyHandler(), userTask);
        //  设置审批任务的截止时间
        if (node.getTimeoutHandler() != null && node.getTimeoutHandler().getEnable()) {
            userTask.setDueDate(node.getTimeoutHandler().getTimeDuration());
        }
        return userTask;
    }

    private static void addTaskRejectElements(BpmSimpleModelNodeVO.RejectHandler rejectHandler, UserTask userTask) {
        if (rejectHandler == null) {
            return;
        }
        addExtensionElement(userTask, USER_TASK_REJECT_HANDLER_TYPE, StrUtil.toStringOrNull(rejectHandler.getType()));
        addExtensionElement(userTask, USER_TASK_REJECT_RETURN_TASK_ID, rejectHandler.getReturnNodeId());
    }

    private static void addAssignStartUserHandlerType(Integer assignStartUserHandlerType, UserTask userTask) {
        if (assignStartUserHandlerType == null) {
            return;
        }
        addExtensionElement(userTask, USER_TASK_ASSIGN_START_USER_HANDLER_TYPE, assignStartUserHandlerType.toString());
    }

    private static void addAssignEmptyHandlerType(BpmSimpleModelNodeVO.AssignEmptyHandler emptyHandler, UserTask userTask) {
        if (emptyHandler == null) {
            return;
        }
        addExtensionElement(userTask, USER_TASK_ASSIGN_EMPTY_HANDLER_TYPE, StrUtil.toStringOrNull(emptyHandler.getType()));
        addExtensionElement(userTask, USER_TASK_ASSIGN_USER_IDS, StrUtil.join(",", emptyHandler.getUserIds()));
    }

    private static void processMultiInstanceLoopCharacteristics(Integer approveMethod, Integer approveRatio, UserTask userTask) {
        BpmUserTaskApproveMethodEnum approveMethodEnum = BpmUserTaskApproveMethodEnum.valueOf(approveMethod);
        // TODO @jason：这种枚举，最终不要去掉哈 BpmUserTaskApproveMethodEnum。因为容易不经意重叠
        if (approveMethodEnum == null || approveMethodEnum == RANDOM) {
            return;
        }
        // 添加审批方式的扩展属性
        addExtensionElement(userTask, BpmnModelConstants.USER_TASK_APPROVE_METHOD,
                approveMethod == null ? null : approveMethod.toString());
        MultiInstanceLoopCharacteristics multiInstanceCharacteristics = new MultiInstanceLoopCharacteristics();
        // 设置 collectionVariable。本系统用不到。仅仅为了 Flowable 校验不报错。
        multiInstanceCharacteristics.setInputDataItem("${coll_userList}");
        if (approveMethodEnum == BpmUserTaskApproveMethodEnum.ANY) {
            multiInstanceCharacteristics.setCompletionCondition(ANY_OF_APPROVE_COMPLETE_EXPRESSION);
            multiInstanceCharacteristics.setSequential(false);
            userTask.setLoopCharacteristics(multiInstanceCharacteristics);
        } else if (approveMethodEnum == SEQUENTIAL) {
            multiInstanceCharacteristics.setCompletionCondition(ALL_APPROVE_COMPLETE_EXPRESSION);
            multiInstanceCharacteristics.setSequential(true);
            multiInstanceCharacteristics.setLoopCardinality("1");
            userTask.setLoopCharacteristics(multiInstanceCharacteristics);
        } else if (approveMethodEnum == RATIO) {
            Assert.notNull(approveRatio, "通过比例不能为空");
            multiInstanceCharacteristics.setCompletionCondition(
                    String.format(APPROVE_BY_RATIO_COMPLETE_EXPRESSION, String.format("%.2f", approveRatio / (double) 100)));
            multiInstanceCharacteristics.setSequential(false);
        }
        userTask.setLoopCharacteristics(multiInstanceCharacteristics);
    }

    /**
     * 给节点添加操作按钮设置元素
     */
    private static void addButtonsSetting(List<OperationButtonSetting> buttonsSetting, UserTask userTask) {
        if (CollUtil.isNotEmpty(buttonsSetting)) {
            List<Map<String, String>> list = CollectionUtils.convertList(buttonsSetting, item -> {
                Map<String, String> settingMap = MapUtil.newHashMap(16);
                settingMap.put(BUTTON_SETTING_ELEMENT_ID_ATTRIBUTE, String.valueOf(item.getId()));
                settingMap.put(BUTTON_SETTING_ELEMENT_DISPLAY_NAME_ATTRIBUTE, item.getDisplayName());
                settingMap.put(BUTTON_SETTING_ELEMENT_ENABLE_ATTRIBUTE, String.valueOf(item.getEnable()));
                return settingMap;
            });
            list.forEach(item -> addExtensionElement(userTask, BUTTON_SETTING_ELEMENT, item));
        }
    }

    /**
     * 给节点添加表单字段权限元素
     */
    private static void addFormFieldsPermission(List<Map<String, String>> fieldsPermissions, FlowElement flowElement) {
        if (CollUtil.isNotEmpty(fieldsPermissions)) {
            fieldsPermissions.forEach(item -> addExtensionElement(flowElement, FORM_FIELD_PERMISSION_ELEMENT, item));
        }
    }

    private static void addExtensionElement(FlowElement element, String name, Map<String, String> attributes) {
        if (attributes == null) {
            return;
        }
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setNamespace(FLOWABLE_EXTENSIONS_NAMESPACE);
        extensionElement.setNamespacePrefix(FLOWABLE_EXTENSIONS_PREFIX);
        extensionElement.setName(name);
        attributes.forEach((key, value) -> {
            ExtensionAttribute extensionAttribute = new ExtensionAttribute(key, value);
            extensionAttribute.setNamespace(FLOWABLE_EXTENSIONS_NAMESPACE);
            extensionElement.addAttribute(extensionAttribute);
        });
        element.addExtensionElement(extensionElement);
    }

    private static void addExtensionElement(FlowElement element, String name, String value) {
        if (value == null) {
            return;
        }
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setNamespace(FLOWABLE_EXTENSIONS_NAMESPACE);
        extensionElement.setNamespacePrefix(FLOWABLE_EXTENSIONS_PREFIX);
        extensionElement.setElementText(value);
        extensionElement.setName(name);
        element.addExtensionElement(extensionElement);
    }

    // ========== 各种 build 节点的方法 ==========

    private static StartEvent convertStartNode(BpmSimpleModelNodeVO node) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(node.getId());
        startEvent.setName(node.getName());
        return startEvent;
    }

    private static UserTask buildBpmnStartUserTask(BpmSimpleModelNodeVO node) {
        UserTask userTask = new UserTask();
        userTask.setId(node.getId());
        userTask.setName(node.getName());
        // 人工审批
        addExtensionElement(userTask, USER_TASK_APPROVE_TYPE, USER.getType().toString());
        // 候选人策略为发起人自己
        addCandidateElements(START_USER.getStrategy(), null, userTask);
        // 添加表单字段权限属性元素
        addFormFieldsPermission(node.getFieldsPermission(), userTask);
        // 添加操作按钮配置属性元素
        addButtonsSetting(node.getButtonsSetting(), userTask);
        // 使用自动通过策略 TODO @fly 复用了SKIP， 是否需要新加一个策略；TODO @fly：【回复】是不是应该类似飞书，搞个草稿状态。待定；还有一种策略，不标记自动通过，而是首次发起后，第一个节点，自动通过；
        addAssignStartUserHandlerType(SKIP.getType(), userTask);
        return userTask;
    }

    private static EndEvent convertEndNode(BpmSimpleModelNodeVO node) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(node.getId());
        endEvent.setName(node.getName());

        // TODO @fly + jason：要不要加一个终止定义？
        return endEvent;
    }

}
