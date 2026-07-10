import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface OrderVO {
  id: string
  merchantId: string
  appId: string
  channelId: string
  channelCode: string
  merchantOrderId: string
  subject: string
  body: string
  notifyUrl: string
  notifyStatus: number
  amount: number
  price: number
  channelFeeRate: number
  channelFeeAmount: number
  status: number
  userIp: string
  expireTime: Date
  successTime: Date
  notifyTime: Date
  successExtensionId: string
  refundStatus: number
  refundTimes: number
  refundAmount: number
  channelUserId: string
  channelOrderNo: string
  createTime: Date
}

export interface OrderDetailVO {
  merchantOrderId?: string
  no?: string
  appId?: string
  appName?: string
  status?: number
  price?: number
  refundPrice?: number
  channelFeePrice?: number
  channelFeeRate?: number
  successTime?: Date
  expireTime?: Date
  createTime?: Date
  updateTime?: Date
  subject?: string
  body?: string
  channelCode?: string
  userIp?: string
  channelOrderNo?: string
  channelUserId?: string
  notifyUrl?: string
  extension?: {
    channelNotifyData?: string
  }
}

// 查询列表支付订单
export const getOrderPage = async (params: any) => {
  return await request.get<PageResult<OrderDetailVO[]>>({
    url: `/${SYS_BASE_URL}/admin/pay/order/page`,
    params
  })
}

// 查询详情支付订单
export const getOrder = async (id: string, sync?: boolean) => {
  return await request.get<OrderVO>({
    url: `/${SYS_BASE_URL}/admin/pay/order/get`,
    params: {
      id,
      sync
    }
  })
}

// 获得支付订单的明细
export const getOrderDetail = async (id: string) => {
  return await request.get<OrderDetailVO>({ url: `/${SYS_BASE_URL}/admin/pay/order/get-detail?id=` + id })
}

// 提交支付订单
export const submitOrder = async (data: any) => {
  return await request.post({ url: `/${SYS_BASE_URL}/admin/pay/order/submit`, data })
}

// 导出支付订单
export const exportOrder = async (params: any) => {
  return await request.download({ url: `/${SYS_BASE_URL}/admin/pay/order/export-excel`, params })
}
