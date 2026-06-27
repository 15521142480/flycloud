import { useI18n } from '@/hooks/web/useI18n'
/** 跟进状态 */
const { t } = useI18n()

export const FOLLOWUP_STATUS = [
  { label: t('auto.views.crm.backlog.components.common.k4b453811'), value: false },
  { label: t('auto.views.crm.backlog.components.common.k366e6f26'), value: true }
]

/** 归属范围 */
export const SCENE_TYPES = [
  { label: t('auto.views.crm.backlog.components.common.k0bbf4db1'), value: 1 },
  { label: t('auto.views.crm.backlog.components.common.k1640a0ff'), value: 2 },
  { label: t('auto.views.crm.backlog.components.common.k72715f2e'), value: 3 }
]

/** 联系状态 */
export const CONTACT_STATUS = [
  { label: t('auto.views.crm.backlog.components.common.k4a758e3e'), value: 1 },
  { label: t('auto.views.crm.backlog.components.common.kbf6fc7bf'), value: 2 },
  { label: t('auto.views.crm.backlog.components.common.k6f0de14f'), value: 3 }
]

/** 审批状态 */
export const AUDIT_STATUS = [
  { label: t('auto.views.crm.backlog.components.common.k57fce0c4'), value: 10 },
  { label: t('auto.views.crm.backlog.components.common.k637104b3'), value: 20 },
  { label: t('auto.views.crm.backlog.components.common.k436557bf'), value: 30 }
]

/** 回款提醒类型 */
export const RECEIVABLE_REMIND_TYPE = [
  { label: t('auto.views.crm.backlog.components.common.k93579e83'), value: 1 },
  { label: t('auto.views.crm.backlog.components.common.kbf6fc7bf'), value: 2 },
  { label: t('auto.views.crm.backlog.components.common.k2723c655'), value: 3 }
]

/** 合同过期状态 */
export const CONTRACT_EXPIRY_TYPE = [
  { label: t('auto.views.crm.backlog.components.common.k40545311'), value: 1 },
  { label: t('auto.views.crm.backlog.components.common.k1354374f'), value: 2 }
]
