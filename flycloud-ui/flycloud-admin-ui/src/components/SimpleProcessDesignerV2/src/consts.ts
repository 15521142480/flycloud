// @ts-ignore
import { DictDataVO } from '@/api/system/dict/types'
import { TaskStatusEnum } from '@/api/bpm/task'
import { useI18n } from '@/hooks/web/useI18n'
/**
 * 节点类型
 */
const { t } = useI18n()

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
  fieldsPermission?: Array<Record<string, any>>
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
  // 活动的状态，用于前端节点状态展示
  activityStatus?: TaskStatusEnum
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
   * 表单内用户字段
   */
  FORM_USER = 50,
  /**
   * 表单内部门负责人
   */
  FORM_DEPT_LEADER = 51,
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
  // 退回节点 Id
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
  userIds?: string[]
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
   * 退回
   */
  RETURN = 6,
  /**
   * 抄送
   */
  COPY = 7
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
NODE_DEFAULT_TEXT.set(
  NodeType.USER_TASK_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k4ddc88ee')
)
NODE_DEFAULT_TEXT.set(
  NodeType.COPY_TASK_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k6996ccb1')
)
NODE_DEFAULT_TEXT.set(
  NodeType.CONDITION_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.kc55d6cdb')
)
NODE_DEFAULT_TEXT.set(
  NodeType.START_USER_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k14798384')
)

export const NODE_DEFAULT_NAME = new Map<number, string>()
NODE_DEFAULT_NAME.set(
  NodeType.USER_TASK_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k9b446de3')
)
NODE_DEFAULT_NAME.set(
  NodeType.COPY_TASK_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.kdb9f866c')
)
NODE_DEFAULT_NAME.set(
  NodeType.CONDITION_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k72f1cf75')
)
NODE_DEFAULT_NAME.set(
  NodeType.START_USER_NODE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k89d282d2')
)

// 候选人策略。暂时不从字典中取。 后续可能调整。控制显示顺序
export const CANDIDATE_STRATEGY: DictDataVO[] = [
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k59ee319d'),
    value: CandidateStrategy.USER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kf0b09386'),
    value: CandidateStrategy.ROLE
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kcf5b1ca4'),
    value: CandidateStrategy.DEPT_MEMBER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k3798d110'),
    value: CandidateStrategy.DEPT_LEADER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k3c28e3ce'),
    value: CandidateStrategy.MULTI_LEVEL_DEPT_LEADER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k63f1f84d'),
    value: CandidateStrategy.START_USER_SELECT
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k9a016504'),
    value: CandidateStrategy.START_USER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k4f428552'),
    value: CandidateStrategy.START_USER_DEPT_LEADER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k3ee19e36'),
    value: CandidateStrategy.START_USER_MULTI_LEVEL_DEPT_LEADER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k653600ba'),
    value: CandidateStrategy.USER_GROUP
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k805bd925'),
    value: CandidateStrategy.FORM_USER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k69389a79'),
    value: CandidateStrategy.FORM_DEPT_LEADER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kb86130ce'),
    value: CandidateStrategy.EXPRESSION
  }
]
// 审批节点 的审批类型
export const APPROVE_TYPE: DictDataVO[] = [
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k83c3f720'),
    value: ApproveType.USER
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k76074d68'),
    value: ApproveType.AUTO_APPROVE
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k47e02145'),
    value: ApproveType.AUTO_REJECT
  }
]

export const APPROVE_METHODS: DictDataVO[] = [
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k973864aa'),
    value: ApproveMethodType.SEQUENTIAL_APPROVE
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k8f91241f'),
    value: ApproveMethodType.APPROVE_BY_RATIO
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k3bfd4629'),
    value: ApproveMethodType.ANY_APPROVE
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k6707f8ca'),
    value: ApproveMethodType.RANDOM_SELECT_ONE_APPROVE
  }
]

export const CONDITION_CONFIG_TYPES: DictDataVO[] = [
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k25290fa6'),
    value: ConditionType.EXPRESSION
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kd92b978a'),
    value: ConditionType.RULE
  }
]

// 时间单位类型
export const TIME_UNIT_TYPES: DictDataVO[] = [
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k28bf227b'),
    value: TimeUnitType.MINUTE
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k99f6904f'),
    value: TimeUnitType.HOUR
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kc3304d1e'),
    value: TimeUnitType.DAY
  }
]
// 超时处理执行动作类型
export const TIMEOUT_HANDLER_TYPES: DictDataVO[] = [
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kd85d48d8'), value: 1 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kd3cafb10'), value: 2 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k47e02145'), value: 3 }
]
export const REJECT_HANDLER_TYPES: DictDataVO[] = [
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.ked69f4d4'),
    value: RejectHandlerType.FINISH_PROCESS
  },
  {
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k6843506d'),
    value: RejectHandlerType.RETURN_USER_TASK
  }
  // { label: '结束任务', value: RejectHandlerType.FINISH_TASK }
]
export const ASSIGN_EMPTY_HANDLER_TYPES: DictDataVO[] = [
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k76074d68'), value: 1 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k47e02145'), value: 2 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k32290673'), value: 3 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kc2c2c0b4'), value: 4 }
]
export const ASSIGN_START_USER_HANDLER_TYPES: DictDataVO[] = [
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k3d30ebed'), value: 1 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kda019002'), value: 2 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kfc6f013f'), value: 3 }
]

// 比较运算符
export const COMPARISON_OPERATORS: DictDataVO = [
  {
    value: '==',
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kbd0f3091')
  },
  {
    value: '!=',
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k3b8d5ab5')
  },
  {
    value: '>',
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k9e2e25fc')
  },
  {
    value: '>=',
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k2a2be930')
  },
  {
    value: '<',
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.kb445321c')
  },
  {
    value: '<=',
    label: t('auto.components.SimpleProcessDesignerV2.src.consts.k7701dee4')
  }
]
// 审批操作按钮名称
export const OPERATION_BUTTON_NAME = new Map<number, string>()
OPERATION_BUTTON_NAME.set(
  OperationButtonType.APPROVE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.kdcc42332')
)
OPERATION_BUTTON_NAME.set(
  OperationButtonType.REJECT,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k03e210a6')
)
OPERATION_BUTTON_NAME.set(
  OperationButtonType.TRANSFER,
  t('auto.components.SimpleProcessDesignerV2.src.consts.ka52c1691')
)
OPERATION_BUTTON_NAME.set(
  OperationButtonType.DELEGATE,
  t('auto.components.SimpleProcessDesignerV2.src.consts.kb78f3880')
)
OPERATION_BUTTON_NAME.set(
  OperationButtonType.ADD_SIGN,
  t('auto.components.SimpleProcessDesignerV2.src.consts.kf2218c36')
)
OPERATION_BUTTON_NAME.set(
  OperationButtonType.RETURN,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k607cd976')
)
OPERATION_BUTTON_NAME.set(
  OperationButtonType.COPY,
  t('auto.components.SimpleProcessDesignerV2.src.consts.k48a091c0')
)

// 默认的按钮权限设置
export const DEFAULT_BUTTON_SETTING: ButtonSetting[] = [
  {
    id: OperationButtonType.APPROVE,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.kdcc42332'),
    enable: true
  },
  {
    id: OperationButtonType.REJECT,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.k03e210a6'),
    enable: true
  },
  {
    id: OperationButtonType.TRANSFER,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.ka52c1691'),
    enable: true
  },
  {
    id: OperationButtonType.DELEGATE,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.kb78f3880'),
    enable: true
  },
  {
    id: OperationButtonType.ADD_SIGN,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.kf2218c36'),
    enable: true
  },
  {
    id: OperationButtonType.RETURN,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.k607cd976'),
    enable: true
  }
]

// 发起人的按钮权限。暂时定死，不可以编辑
export const START_USER_BUTTON_SETTING: ButtonSetting[] = [
  {
    id: OperationButtonType.APPROVE,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.k09cbc97a'),
    enable: true
  },
  {
    id: OperationButtonType.REJECT,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.k03e210a6'),
    enable: false
  },
  {
    id: OperationButtonType.TRANSFER,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.ka52c1691'),
    enable: false
  },
  {
    id: OperationButtonType.DELEGATE,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.kb78f3880'),
    enable: false
  },
  {
    id: OperationButtonType.ADD_SIGN,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.kf2218c36'),
    enable: false
  },
  {
    id: OperationButtonType.RETURN,
    displayName: t('auto.components.SimpleProcessDesignerV2.src.consts.k607cd976'),
    enable: false
  }
]

export const MULTI_LEVEL_DEPT: DictDataVO = [
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kc5d143da'), value: 1 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.ke35d04cb'), value: 2 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k2e879e03'), value: 3 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kb760abf3'), value: 4 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k06bc50a1'), value: 5 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k87b31390'), value: 6 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k1640f4a3'), value: 7 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k87b13525'), value: 8 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kecf15194'), value: 9 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k12dc1f74'), value: 10 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k75df474e'), value: 11 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k2543e5c8'), value: 12 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k1c707818'), value: 13 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.k64b33abf'), value: 14 },
  { label: t('auto.components.SimpleProcessDesignerV2.src.consts.kccf22517'), value: 15 }
]

/**
 * 流程实例的变量枚举
 */
export enum ProcessVariableEnum {
  /**
   * 发起用户 ID
   */
  START_USER_ID = 'PROCESS_START_USER_ID'
}
