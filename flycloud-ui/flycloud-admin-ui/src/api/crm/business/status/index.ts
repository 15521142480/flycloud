import request from '@/config/axios'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export interface BusinessStatusTypeVO {
  id: number
  name: string
  deptIds: number[]
  statuses?: {
    id: number
    name: string
    percent: number
  }
}

export const DEFAULT_STATUSES = [
  {
    endStatus: 1,
    key: t('auto.api.crm.business.status.index.k76b98808'),
    name: t('auto.api.crm.business.status.index.ke65d23dd'),
    percent: 100
  },
  {
    endStatus: 2,
    key: t('auto.api.crm.business.status.index.k76b98808'),
    name: t('auto.api.crm.business.status.index.k5375e3d6'),
    percent: 0
  },
  {
    endStatus: 3,
    key: t('auto.api.crm.business.status.index.k76b98808'),
    name: t('auto.api.crm.business.status.index.keb645ab4'),
    percent: 0
  }
]

// 查询商机状态组列表
export const getBusinessStatusPage = async (params: any) => {
  return await request.get({ url: `/crm/business-status/page`, params })
}

// 新增商机状态组
export const createBusinessStatus = async (data: BusinessStatusTypeVO) => {
  return await request.post({ url: `/crm/business-status/create`, data })
}

// 修改商机状态组
export const updateBusinessStatus = async (data: BusinessStatusTypeVO) => {
  return await request.put({ url: `/crm/business-status/update`, data })
}

// 查询商机状态类型详情
export const getBusinessStatus = async (id: number) => {
  return await request.get({ url: `/crm/business-status/get/` + id })
}

// 删除商机状态
export const deleteBusinessStatus = async (id: number) => {
  return await request.delete({ url: `/crm/business-status/delete/` + id })
}

// 获得商机状态组列表
export const getBusinessStatusTypeSimpleList = async () => {
  return await request.get({ url: `/crm/business-status/type-simple-list` })
}

// 获得商机阶段列表
export const getBusinessStatusSimpleList = async (typeId: number) => {
  return await request.get({ url: `/crm/business-status/status-simple-list`, params: { typeId } })
}
