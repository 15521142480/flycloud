// @ts-ignore
import { DictDataVO } from '@/api/system/dict/types'

/**
 * 节点类型
 */
export enum NodeType {
  /**
   * 结束节点
   */
  END_EVENT_NODE = 1,
  /**
   * 发起人节点
   */
  START_USER_NODE = 10,
  /**
   * 审批人节点
   */
  USER_TASK_NODE = 11,

  /**
   * 抄送人节点
   */
  COPY_TASK_NODE = 12,

  /**
   * 条件节点
   */
  CONDITION_NODE = 50,
  /**
   * 条件分支节点 (对应排他网关)
   */
  CONDITION_BRANCH_NODE = 51,
  /**
   * 并行分支节点 (对应并行网关)
   */
  PARALLEL_BRANCH_NODE = 52,

  /**
   * 包容分支节点 (对应包容网关)
   */
  INCLUSIVE_BRANCH_NODE = 53
}

export enum NodeId {
  /**
   * 发起人节点 Id
   */
  START_USER_NODE_ID = 'StartUserNode',

  /**
   * 发起人节点 Id
   */
  END_EVENT_NODE_ID = 'EndEvent'
}

/**
 *  节点结构定义
 */
export interface SimpleFlowNode {
  id: string
  type: NodeType
  name: string
  showText?: string
  // 孩子节点
  childNode?: SimpleFlowNode
  // 条件节点
  conditionNodes?: SimpleFlowNode[]
  // 审批类型
  approveType?: ApproveType
  // 候选人策略
  candidateStrategy?: number
  // 候选人参数
  candidateParam?: string
  // 多人审批方式
  approveMethod?: ApproveMethodType
  //通过比例
  approveRatio?: number
  // 审批按钮设置
  buttonsSetting?: any[]
  // 表单权限
  fieldsPermission?: Array<Record<string, string>>
  // 审批任务超时处理
  timeoutHandler?: TimeoutHandler
  // 审批任务拒绝处理
  rejectHandler?: RejectHandler
  // 审批人为空的处理
  assignEmptyHandler?: AssignEmptyHandler
  // 审批节点的审批人与发起人相同时，对应的处理类型
  assignStartUserHandlerType?: number
  // 条件类型
  conditionType?: ConditionType
  // 条件表达式
  conditionExpression?: string
  // 条件组
  conditionGroups?: ConditionGroup
  // 是否默认的条件
  defaultFlow?: boolean

}
// 候选人策略枚举 （ 用于审批节点。抄送节点 )
export enum CandidateStrategy {
  /**
   * 指定角色
   */
  ROLE = 10,
  /**
   * 部门成员
   */
  DEPT_MEMBER = 20,
  /**
   * 部门的负责人
   */
  DEPT_LEADER = 21,
  /**
   * 连续多级部门的负责人
   */
  MULTI_LEVEL_DEPT_LEADER = 23,
  /**
   * 指定岗位
   */
  POST = 22,
  /**
   * 指定用户
   */
  USER = 30,
  /**
   * 发起人自选
   */
  START_USER_SELECT = 35,
  /**
   * 发起人自己
   */
  START_USER = 36,
  /**
   * 发起人部门负责人
   */
  START_USER_DEPT_LEADER = 37,
  /**
   * 发起人连续多级部门的负责人
   */
  START_USER_MULTI_LEVEL_DEPT_LEADER = 38,
  /**
   * 指定用户组
   */
  USER_GROUP = 40,
  /**
   * 流程表达式
   */
  EXPRESSION = 60
}

// 多人审批方式类型枚举 （ 用于审批节点 ）
export enum ApproveMethodType {
  /**
   * 随机挑选一人审批
   */
  RANDOM_SELECT_ONE_APPROVE = 1,

  /**
   * 多人会签(按通过比例)
   */
  APPROVE_BY_RATIO = 2,

  /**
   * 多人或签(通过只需一人，拒绝只需一人)
   */
  ANY_APPROVE = 3,
  /**
   * 多人依次审批
   */
  SEQUENTIAL_APPROVE = 4
}

/**
 * 审批拒绝结构定义
 */
export type RejectHandler = {
  // 审批拒绝类型
  type: RejectHandlerType
  // 回退节点 Id
  returnNodeId?: string
}

/**
 * 审批超时结构定义
 */
export type TimeoutHandler = {
  // 是否开启超时处理
  enable: boolean
  // 超时执行的动作
  type?: number
  // 超时时间设置
  timeDuration?: string
  // 执行动作是自动提醒, 最大提醒次数
  maxRemindCount?: number
}

/**
 * 审批人为空的结构定义
 */
export type AssignEmptyHandler = {
  // 审批人为空的处理类型
  type: AssignEmptyHandlerType
  // 指定用户的编号数组
  userIds?: number[]
}

// 审批拒绝类型枚举
export enum RejectHandlerType {
  /**
   * 结束流程
   */
  FINISH_PROCESS = 1,
  /**
   * 驳回到指定节点
   */
  RETURN_USER_TASK = 2
}
// 用户任务超时处理类型枚举
export enum TimeoutHandlerType {
  /**
   * 自动提醒
   */
  REMINDER = 1,
  /**
   * 自动同意
   */
  APPROVE = 2,
  /**
   * 自动拒绝
   */
  REJECT = 3
}
// 用户任务的审批人为空时，处理类型枚举
export enum AssignEmptyHandlerType {
  /**
   * 自动通过
   */
  APPROVE = 1,
  /**
   * 自动拒绝
   */
  REJECT = 2,
  /**
   * 指定人员审批
   */
  ASSIGN_USER,
  /**
   * 转交给流程管理员
   */
  ASSIGN_ADMIN = 4
}
// 用户任务的审批人与发起人相同时，处理类型枚举
export enum AssignStartUserHandlerType {
  /**
   * 由发起人对自己审批
   */
  START_USER_AUDIT = 1,
  /**
   * 自动跳过【参考飞书】：1）如果当前节点还有其他审批人，则交由其他审批人进行审批；2）如果当前节点没有其他审批人，则该节点自动通过
   */
  SKIP = 2,
  /**
   * 转交给部门负责人审批
   */
  ASSIGN_DEPT_LEADER = 3
}

// 用户任务的审批类型。 【参考飞书】
export enum ApproveType {
  /**
   * 人工审批
   */
  USER = 1,
  /**
   * 自动通过
   */
  AUTO_APPROVE = 2,
  /**
   * 自动拒绝
   */
  AUTO_REJECT = 3
}

// 时间单位枚举
export enum TimeUnitType {
  /**
   * 分钟
   */
  MINUTE = 1,
  /**
   * 小时
   */
  HOUR = 2,
  /**
   * 天
   */
  DAY = 3
}

// 条件配置类型 （ 用于条件节点配置 ）
export enum ConditionType {
  /**
   * 条件表达式
   */
  EXPRESSION = 1,

  /**
   * 条件规则
   */
  RULE = 2
}
/**
 * 表单权限的枚举
 */
export enum FieldPermissionType {
  /**
   * 只读
   */
  READ = '1',
  /**
   * 编辑
   */
  WRITE = '2',
  /**
   * 隐藏
   */
  NONE = '3'
}
/**
 * 操作按钮权限结构定义
 */
export type ButtonSetting = {
  id: OperationButtonType
  displayName: string
  enable: boolean
}

// 操作按钮类型枚举 (用于审批节点)
export enum OperationButtonType {
  /**
   * 通过
   */
  APPROVE = 1,
  /**
   * 拒绝
   */
  REJECT = 2,
  /**
   * 转办
   */
  TRANSFER = 3,
  /**
   * 委派
   */
  DELEGATE = 4,
  /**
   * 加签
   */
  ADD_SIGN = 5,
  /**
   * 回退
   */
  RETURN = 6
}

/**
 * 条件规则结构定义
 */
export type ConditionRule = {
  type: number
  opName: string
  opCode: string
  leftSide: string
  rightSide: string
}

/**
 * 条件组结构定义
 */
export type ConditionGroup = {
  // 条件组的逻辑关系是否为且
  and: boolean
  // 条件数组
  conditions: Condition[]
}

/**
 * 条件结构定义
 */
export type Condition = {
  // 条件规则的逻辑关系是否为且
  and: boolean
  rules: ConditionRule[]
}

export const NODE_DEFAULT_TEXT = new Map<number, string>()
NODE_DEFAULT_TEXT.set(NodeType.USER_TASK_NODE, '请配置审批人')
NODE_DEFAULT_TEXT.set(NodeType.COPY_TASK_NODE, '请配置抄送人')
NODE_DEFAULT_TEXT.set(NodeType.CONDITION_NODE, '请设置条件')
NODE_DEFAULT_TEXT.set(NodeType.START_USER_NODE, '请设置发起人')

export const NODE_DEFAULT_NAME = new Map<number, string>()
NODE_DEFAULT_NAME.set(NodeType.USER_TASK_NODE, '审批人')
NODE_DEFAULT_NAME.set(NodeType.COPY_TASK_NODE, '抄送人')
NODE_DEFAULT_NAME.set(NodeType.CONDITION_NODE, '条件')
NODE_DEFAULT_NAME.set(NodeType.START_USER_NODE, '发起人')

// 候选人策略。暂时不从字典中取。 后续可能调整。控制显示顺序
export const CANDIDATE_STRATEGY: DictDataVO[] = [
  { label: '指定成员', value: CandidateStrategy.USER },
  { label: '指定角色', value: CandidateStrategy.ROLE },
  { label: '部门成员', value: CandidateStrategy.DEPT_MEMBER },
  { label: '部门负责人', value: CandidateStrategy.DEPT_LEADER },
  { label: '连续多级部门负责人', value: CandidateStrategy.MULTI_LEVEL_DEPT_LEADER },
  { label: '发起人自选', value: CandidateStrategy.START_USER_SELECT },
  { label: '发起人本人', value: CandidateStrategy.START_USER },
  { label: '发起人部门负责人', value: CandidateStrategy.START_USER_DEPT_LEADER },
  { label: '发起人连续部门负责人', value: CandidateStrategy.START_USER_MULTI_LEVEL_DEPT_LEADER },
  { label: '用户组', value: CandidateStrategy.USER_GROUP },
  { label: '流程表达式', value: CandidateStrategy.EXPRESSION }
]
// 审批节点 的审批类型
export const APPROVE_TYPE: DictDataVO[] = [
  { label: '人工审批', value: ApproveType.USER },
  { label: '自动通过', value: ApproveType.AUTO_APPROVE },
  { label: '自动拒绝', value: ApproveType.AUTO_REJECT }
]

export const APPROVE_METHODS: DictDataVO[] = [
  { label: '按顺序依次审批', value: ApproveMethodType.SEQUENTIAL_APPROVE },
  { label: '会签（可同时审批，至少 % 人必须审批通过）', value: ApproveMethodType.APPROVE_BY_RATIO },
  { label: '或签(可同时审批，有一人通过即可)', value: ApproveMethodType.ANY_APPROVE },
  { label: '随机挑选一人审批', value: ApproveMethodType.RANDOM_SELECT_ONE_APPROVE }
]

export const CONDITION_CONFIG_TYPES: DictDataVO[] = [
  { label: '条件表达式', value: ConditionType.EXPRESSION },
  { label: '条件规则', value: ConditionType.RULE }
]

// 时间单位类型
export const TIME_UNIT_TYPES: DictDataVO[] = [
  { label: '分钟', value: TimeUnitType.MINUTE },
  { label: '小时', value: TimeUnitType.HOUR },
  { label: '天', value: TimeUnitType.DAY }
]
// 超时处理执行动作类型
export const TIMEOUT_HANDLER_TYPES: DictDataVO[] = [
  { label: '自动提醒', value: 1 },
  { label: '自动同意', value: 2 },
  { label: '自动拒绝', value: 3 }
]
export const REJECT_HANDLER_TYPES: DictDataVO[] = [
  { label: '终止流程', value: RejectHandlerType.FINISH_PROCESS },
  { label: '驳回到指定节点', value: RejectHandlerType.RETURN_USER_TASK }
  // { label: '结束任务', value: RejectHandlerType.FINISH_TASK }
]
export const ASSIGN_EMPTY_HANDLER_TYPES: DictDataVO[] = [
  { label: '自动通过', value: 1 },
  { label: '自动拒绝', value: 2 },
  { label: '指定成员审批', value: 3 },
  { label: '转交给流程管理员', value: 4 }
]
export const ASSIGN_START_USER_HANDLER_TYPES: DictDataVO[] = [
  { label: '由发起人对自己审批', value: 1 },
  { label: '自动跳过', value: 2 },
  { label: '转交给部门负责人审批', value: 3 }
]

// 比较运算符
export const COMPARISON_OPERATORS: DictDataVO = [
  {
    value: '==',
    label: '等于'
  },
  {
    value: '!=',
    label: '不等于'
  },
  {
    value: '>',
    label: '大于'
  },
  {
    value: '>=',
    label: '大于等于'
  },
  {
    value: '<',
    label: '小于'
  },
  {
    value: '<=',
    label: '小于等于'
  }
]
// 审批操作按钮名称
export const OPERATION_BUTTON_NAME = new Map<number, string>()
OPERATION_BUTTON_NAME.set(OperationButtonType.APPROVE, '通过')
OPERATION_BUTTON_NAME.set(OperationButtonType.REJECT, '拒绝')
OPERATION_BUTTON_NAME.set(OperationButtonType.TRANSFER, '转办')
OPERATION_BUTTON_NAME.set(OperationButtonType.DELEGATE, '委派')
OPERATION_BUTTON_NAME.set(OperationButtonType.ADD_SIGN, '加签')
OPERATION_BUTTON_NAME.set(OperationButtonType.RETURN, '回退')

// 默认的按钮权限设置
export const DEFAULT_BUTTON_SETTING: ButtonSetting[] = [
  { id: OperationButtonType.APPROVE, displayName: '通过', enable: true },
  { id: OperationButtonType.REJECT, displayName: '拒绝', enable: true },
  { id: OperationButtonType.TRANSFER, displayName: '转办', enable: false },
  { id: OperationButtonType.DELEGATE, displayName: '委派', enable: false },
  { id: OperationButtonType.ADD_SIGN, displayName: '加签', enable: false },
  { id: OperationButtonType.RETURN, displayName: '回退', enable: false }
]

// 发起人的按钮权限。暂时定死，不可以编辑
export const START_USER_BUTTON_SETTING: ButtonSetting[] = [
  { id: OperationButtonType.APPROVE, displayName: '提交', enable: true },
  { id: OperationButtonType.REJECT, displayName: '拒绝', enable: false },
  { id: OperationButtonType.TRANSFER, displayName: '转办', enable: false },
  { id: OperationButtonType.DELEGATE, displayName: '委派', enable: false },
  { id: OperationButtonType.ADD_SIGN, displayName: '加签', enable: false },
  { id: OperationButtonType.RETURN, displayName: '回退', enable: false }
]

export const MULTI_LEVEL_DEPT: DictDataVO = [
  { label: '第 1 级部门', value: 1 },
  { label: '第 2 级部门', value: 2 },
  { label: '第 3 级部门', value: 3 },
  { label: '第 4 级部门', value: 4 },
  { label: '第 5 级部门', value: 5 },
  { label: '第 6 级部门', value: 6 },
  { label: '第 7 级部门', value: 7 },
  { label: '第 8 级部门', value: 8 },
  { label: '第 9 级部门', value: 9 },
  { label: '第 10 级部门', value: 10 },
  { label: '第 11 级部门', value: 11 },
  { label: '第 12 级部门', value: 12 },
  { label: '第 13 级部门', value: 13 },
  { label: '第 14 级部门', value: 14 },
  { label: '第 15 级部门', value: 15 }
]
