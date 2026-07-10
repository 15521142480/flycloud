import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ChannelVO {
  id: string
  code: string
  config: string
  status: number
  remark: string
  feeRate: number
  appId: string
  createTime: Date
}

// 查询列表支付渠道
export const getChannelPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/admin/pay/channel/page`, params })
}

// 查询详情支付渠道
export const getChannel = (appId: string, code: string) => {
  const params = {
    appId: appId,
    code: code
  }
  return request.get({ url: `/${SYS_BASE_URL}/admin/pay/channel/get`, params: params })
}

// 新增支付渠道
export const createChannel = (data: ChannelVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/admin/pay/channel/create`, data })
}

// 修改支付渠道
export const updateChannel = (data: ChannelVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/admin/pay/channel/update`, data })
}

// 删除支付渠道
export const deleteChannel = (id: string) => {
  return request.delete({ url: `/${SYS_BASE_URL}/admin/pay/channel/delete?id=` + id })
}

// 导出支付渠道
export const exportChannel = (params) => {
  return request.download({ url: `/${SYS_BASE_URL}/admin/pay/channel/export-excel`, params })
}
