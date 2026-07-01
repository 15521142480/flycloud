import { useI18n } from '@/hooks/web/useI18n'
/**
 * Created by lxs
 *
 * 枚举类
 */

// ========== COMMON 模块 ==========
// 全局通用状态枚举
const { t } = useI18n()

export const CommonStatusEnum = {
  ENABLE: 0, // 开启
  DISABLE: 1 // 禁用
}

// 全局用户类型枚举
export const UserTypeEnum = {
  MEMBER: 1, // 会员
  ADMIN: 2 // 管理员
}

// ========== SYSTEM 模块 ==========

/**
 * 系统类型
 */
export const SystemTypeEnum = {
  TYPE_0: 0, // 平台管理系统
  TYPE_1: 1, // 商家管理系统
  TYPE_2: 2, // 音乐平台管理系统
  TYPE_3: 3 // 音乐歌手管理系统
}

/**
 * 角色标识
 */
export const RoleEnum = {
  SUPER_ADMIN: 'super_admin' // 平台管理系统
}

/**
 * 菜单的类型枚举
 */
export const SystemMenuTypeEnum = {
  DIR: 1, // 目录
  MENU: 2, // 菜单
  BUTTON: 3 // 按钮
}

/**
 * 角色的类型枚举
 */
export const SystemRoleTypeEnum = {
  SYSTEM: 1, // 内置角色
  CUSTOM: 2 // 自定义角色
}

/**
 * 数据权限的范围枚举
 */
export const SystemDataScopeEnum = {
  ALL: 1, // 全部数据权限
  DEPT_CUSTOM: 2, // 指定部门数据权限
  DEPT_ONLY: 3, // 部门数据权限
  DEPT_AND_CHILD: 4, // 部门及以下数据权限
  DEPT_SELF: 5 // 仅本人数据权限
}

/**
 * 用户的社交平台的类型枚举
 */
export const SystemUserSocialTypeEnum = {
  DINGTALK: {
    title: t('auto.utils.constants.k32f35a70'),
    type: 20,
    source: 'dingtalk',
    img: 'https://s1.ax1x.com/2022/05/22/OzMDRs.png'
  },
  WECHAT_ENTERPRISE: {
    title: t('auto.utils.constants.kc1983c6d'),
    type: 30,
    source: 'wechat_enterprise',
    img: 'https://s1.ax1x.com/2022/05/22/OzMrzn.png'
  }
}

/**
 * 用户性别枚举（对齐后端 system_user_sex 字典）
 */
export const SystemUserSexEnum = {
  UNKNOWN: 0, // 未知
  MALE: 1, // 男
  FEMALE: 2 // 女
}

// ========== INFRA 模块 ==========
/**
 * 代码生成模板类型
 */
export const InfraCodegenTemplateTypeEnum = {
  CRUD: 1, // 基础 CRUD
  TREE: 2, // 树形 CRUD
  SUB: 3 // 主子表 CRUD
}

/**
 * 任务状态的枚举
 */
export const InfraJobStatusEnum = {
  INIT: 0, // 初始化中
  NORMAL: 1, // 运行中
  STOP: 2 // 暂停运行
}

/**
 * API 异常数据的处理状态
 */
export const InfraApiErrorLogProcessStatusEnum = {
  INIT: 0, // 未处理
  DONE: 1, // 已处理
  IGNORE: 2 // 已忽略
}

// ========== PAY 模块 ==========
/**
 * 支付渠道枚举
 */
export const PayChannelEnum = {
  WX_PUB: {
    code: 'wx_pub',
    name: t('auto.utils.constants.ka11438a9')
  },
  WX_LITE: {
    code: 'wx_lite',
    name: t('auto.utils.constants.k787e4e5c')
  },
  WX_APP: {
    code: 'wx_app',
    name: t('auto.utils.constants.k1be0105c')
  },
  WX_NATIVE: {
    code: 'wx_native',
    name: t('auto.utils.constants.ke17527c2')
  },
  WX_WAP: {
    code: 'wx_wap',
    name: t('auto.utils.constants.k1749d062')
  },
  WX_BAR: {
    code: 'wx_bar',
    name: t('auto.utils.constants.k2eb2da22')
  },
  ALIPAY_PC: {
    code: 'alipay_pc',
    name: t('auto.utils.constants.kdff9b7f7')
  },
  ALIPAY_WAP: {
    code: 'alipay_wap',
    name: t('auto.utils.constants.ke6c0682c')
  },
  ALIPAY_APP: {
    code: 'alipay_app',
    name: t('auto.utils.constants.k7a8b6179')
  },
  ALIPAY_QR: {
    code: 'alipay_qr',
    name: t('auto.utils.constants.ka3178821')
  },
  ALIPAY_BAR: {
    code: 'alipay_bar',
    name: t('auto.utils.constants.ke6558bef')
  },
  WALLET: {
    code: 'wallet',
    name: t('auto.utils.constants.k659bba3a')
  },
  MOCK: {
    code: 'mock',
    name: t('auto.utils.constants.kbf4800c0')
  }
}

/**
 * 支付的展示模式每局
 */
export const PayDisplayModeEnum = {
  URL: {
    mode: 'url'
  },
  IFRAME: {
    mode: 'iframe'
  },
  FORM: {
    mode: 'form'
  },
  QR_CODE: {
    mode: 'qr_code'
  },
  APP: {
    mode: 'app'
  }
}

/**
 * 支付类型枚举
 */
export const PayType = {
  WECHAT: 'WECHAT',
  ALIPAY: 'ALIPAY',
  MOCK: 'MOCK'
}

/**
 * 支付订单状态枚举
 */
export const PayOrderStatusEnum = {
  WAITING: {
    status: 0,
    name: t('auto.utils.constants.k5a565427')
  },
  SUCCESS: {
    status: 10,
    name: t('auto.utils.constants.k2a3603c4')
  },
  CLOSED: {
    status: 20,
    name: t('auto.utils.constants.k5a565427')
  }
}

// ========== MALL - 商品模块 ==========
/**
 * 商品 SPU 状态
 */
export const ProductSpuStatusEnum = {
  RECYCLE: {
    status: -1,
    name: t('auto.utils.constants.k64ea8751')
  },
  DISABLE: {
    status: 0,
    name: t('auto.utils.constants.ka2698bcf')
  },
  ENABLE: {
    status: 1,
    name: t('auto.utils.constants.kddc61d57')
  }
}

// ========== MALL - 营销模块 ==========
/**
 * 优惠劵模板的有限期类型的枚举
 */
export const CouponTemplateValidityTypeEnum = {
  DATE: {
    type: 1,
    name: t('auto.utils.constants.kc016a5ce')
  },
  TERM: {
    type: 2,
    name: t('auto.utils.constants.ka3cfafb7')
  }
}

/**
 * 优惠劵模板的领取方式的枚举
 */
export const CouponTemplateTakeTypeEnum = {
  USER: {
    type: 1,
    name: t('auto.utils.constants.k72e180ef')
  },
  ADMIN: {
    type: 2,
    name: t('auto.utils.constants.kf27beb7d')
  },
  REGISTER: {
    type: 3,
    name: t('auto.utils.constants.k0242561b')
  }
}

/**
 * 营销的商品范围枚举
 */
export const PromotionProductScopeEnum = {
  ALL: {
    scope: 1,
    name: t('auto.utils.constants.kd0e90d4d')
  },
  SPU: {
    scope: 2,
    name: t('auto.utils.constants.k4946245f')
  },
  CATEGORY: {
    scope: 3,
    name: t('auto.utils.constants.k724e35c4')
  }
}

/**
 * 营销的条件类型枚举
 */
export const PromotionConditionTypeEnum = {
  PRICE: {
    type: 10,
    name: t('auto.utils.constants.ka5c1e674')
  },
  COUNT: {
    type: 20,
    name: t('auto.utils.constants.kab859662')
  }
}

/**
 * 优惠类型枚举
 */
export const PromotionDiscountTypeEnum = {
  PRICE: {
    type: 1,
    name: t('auto.utils.constants.k8b27cd6b')
  },
  PERCENT: {
    type: 2,
    name: t('auto.utils.constants.kb167c8f7')
  }
}

// ========== MALL - 交易模块 ==========
/**
 * 分销关系绑定模式枚举
 */
export const BrokerageBindModeEnum = {
  ANYTIME: {
    mode: 1,
    name: t('auto.utils.constants.ka0124a75')
  },
  REGISTER: {
    mode: 2,
    name: t('auto.utils.constants.k19dae694')
  },
  OVERRIDE: {
    mode: 3,
    name: t('auto.utils.constants.ka42341fe')
  }
}
/**
 * 分佣模式枚举
 */
export const BrokerageEnabledConditionEnum = {
  ALL: {
    condition: 1,
    name: t('auto.utils.constants.k010060e3')
  },
  ADMIN: {
    condition: 2,
    name: t('auto.utils.constants.k4088c831')
  }
}
/**
 * 佣金记录业务类型枚举
 */
export const BrokerageRecordBizTypeEnum = {
  ORDER: {
    type: 1,
    name: t('auto.utils.constants.k09ca2e77')
  },
  WITHDRAW: {
    type: 2,
    name: t('auto.utils.constants.k0b403e3e')
  }
}
/**
 * 佣金提现状态枚举
 */
export const BrokerageWithdrawStatusEnum = {
  AUDITING: {
    status: 0,
    name: t('auto.utils.constants.kfe58c849')
  },
  AUDIT_SUCCESS: {
    status: 10,
    name: t('auto.utils.constants.k637104b3')
  },
  AUDIT_FAIL: {
    status: 20,
    name: t('auto.utils.constants.k436557bf')
  },
  WITHDRAW_SUCCESS: {
    status: 11,
    name: t('auto.utils.constants.k8de86d88')
  },
  WITHDRAW_FAIL: {
    status: 21,
    name: t('auto.utils.constants.kd9348c3e')
  }
}
/**
 * 佣金提现类型枚举
 */
export const BrokerageWithdrawTypeEnum = {
  WALLET: {
    type: 1,
    name: t('auto.utils.constants.kc02d6976')
  },
  BANK: {
    type: 2,
    name: t('auto.utils.constants.k0be83ef1')
  },
  WECHAT: {
    type: 3,
    name: t('auto.utils.constants.k68406df3')
  },
  ALIPAY: {
    type: 4,
    name: t('auto.utils.constants.k66f1177d')
  }
}

/**
 * 配送方式枚举
 */
export const DeliveryTypeEnum = {
  EXPRESS: {
    type: 1,
    name: t('auto.utils.constants.k7b1ede77')
  },
  PICK_UP: {
    type: 2,
    name: t('auto.utils.constants.ke4ef9c8a')
  }
}
/**
 * 交易订单 - 状态
 */
export const TradeOrderStatusEnum = {
  UNPAID: {
    status: 0,
    name: t('auto.utils.constants.k3840a0a2')
  },
  UNDELIVERED: {
    status: 10,
    name: t('auto.utils.constants.k2dd7fc21')
  },
  DELIVERED: {
    status: 20,
    name: t('auto.utils.constants.k43409596')
  },
  COMPLETED: {
    status: 30,
    name: t('auto.utils.constants.ke99b48a2')
  },
  CANCELED: {
    status: 40,
    name: t('auto.utils.constants.ka5ffdc95')
  }
}

// ========== ERP - 企业资源计划 ==========

export const ErpBizType = {
  PURCHASE_ORDER: 10,
  PURCHASE_IN: 11,
  PURCHASE_RETURN: 12,
  SALE_ORDER: 20,
  SALE_OUT: 21,
  SALE_RETURN: 22
}

// ========== BPM 模块 ==========

export const BpmModelType = {
  BPMN: 10, // BPMN 设计器
  SIMPLE: 20 // 简易设计器
}

export const BpmModelFormType = {
  NORMAL: 10, // 流程表单
  CUSTOM: 20 // 业务表单
}

export const BpmProcessInstanceStatus = {
  NOT_START: -1, // 未开始
  RUNNING: 1, // 审批中
  APPROVE: 2, // 审批通过
  REJECT: 3, // 审批不通过
  CANCEL: 4 // 已取消
}
