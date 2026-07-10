import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface RefundVO {
  id: string
  merchantId: string
  appId: string
  channelId: string
  channelCode: string
  orderId: string
  tradeNo: string
  merchantOrderId: string
  merchantRefundNo: string
  notifyUrl: string
  notifyStatus: number
  status: number
  type: number
  payAmount: number
  refundAmount: number
  reason: string
  userIp: string
  channelOrderNo: string
  channelRefundNo: string
  channelErrorCode: string
  channelErrorMsg: string
  channelExtras: string
  expireTime: Date
  successTime: Date
  notifyTime: Date
  createTime: Date
}

export interface RefundDetailVO {
  merchantRefundId?: string
  channelRefundNo?: string
  merchantOrderId?: string
  channelOrderNo?: string
  appId?: string
  appName?: string
  payPrice?: number
  refundPrice?: number
  status?: number
  successTime?: Date
  createTime?: Date
  updateTime?: Date
  channelCode?: string
  reason?: string
  userIp?: string
  notifyUrl?: string
  channelErrorCode?: string
  channelErrorMsg?: string
  channelNotifyData?: string
}

// 查询列表退款订单
export const getRefundPage = (params: any) => {
  return request.get<PageResult<RefundDetailVO[]>>({
    url: `/${SYS_BASE_URL}/admin/pay/refund/page`,
    params
  })
}

// 查询详情退款订单
export const getRefund = (id: string) => {
  return request.get<RefundDetailVO>({ url: `/${SYS_BASE_URL}/admin/pay/refund/get?id=` + id })
}

// 新增退款订单
export const createRefund = (data: RefundVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/admin/pay/refund/create`, data })
}

// 修改退款订单
export const updateRefund = (data: RefundVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/admin/pay/refund/update`, data })
}

// 删除退款订单
export const deleteRefund = (id: string) => {
  return request.delete({ url: `/${SYS_BASE_URL}/admin/pay/refund/delete?id=` + id })
}

// 导出退款订单
export const exportRefund = (params: any) => {
  return request.download({ url: `/${SYS_BASE_URL}/admin/pay/refund/export-excel`, params })
}
