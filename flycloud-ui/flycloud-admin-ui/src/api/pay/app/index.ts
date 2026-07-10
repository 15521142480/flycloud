import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface AppVO {
  id: string
  appKey: string
  name: string
  status: number
  remark: string
  payNotifyUrl: string
  refundNotifyUrl: string
  transferNotifyUrl: string
  merchantId: string
  merchantName: string
  createTime: Date
}

export interface AppPageReqVO extends PageParam {
  name?: string
  status?: number
  remark?: string
  payNotifyUrl?: string
  refundNotifyUrl?: string
  transferNotifyUrl?: string
  merchantName?: string
  createTime?: Date[]
}

export interface AppUpdateStatusReqVO {
  id: string
  status: number
}

// 查询列表支付应用
export const getAppPage = (params: AppPageReqVO) => {
  return request.get({ url: `/${SYS_BASE_URL}/admin/pay/app/page`, params })
}

// 查询详情支付应用
export const getApp = (id: string) => {
  return request.get({ url: `/${SYS_BASE_URL}/admin/pay/app/get?id=` + id })
}

// 新增支付应用
export const createApp = (data: AppVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/admin/pay/app/create`, data })
}

// 修改支付应用
export const updateApp = (data: AppVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/admin/pay/app/update`, data })
}

// 支付应用信息状态修改
export const changeAppStatus = (data: AppUpdateStatusReqVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/admin/pay/app/update-status`, data: data })
}

// 删除支付应用
export const deleteApp = (id: string) => {
  return request.delete({ url: `/${SYS_BASE_URL}/admin/pay/app/delete?id=` + id })
}

// 获得支付应用列表
export const getAppList = () => {
  return request.get<AppVO[]>({
    url: `/${SYS_BASE_URL}/admin/pay/app/list`
  })
}
