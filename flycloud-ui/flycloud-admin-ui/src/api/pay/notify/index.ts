import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface NotifyLogVO {
  id?: string
  status?: number
  notifyTimes?: number
  lastExecuteTime?: Date
  createTime?: Date
  response?: string
}

export interface NotifyTaskVO {
  id?: string
  appId?: string
  appName?: string
  merchantOrderId?: string
  merchantRefundId?: string
  merchantTransferId?: string
  dataId?: string
  type?: number
  status?: number
  notifyTimes?: number
  maxNotifyTimes?: number
  lastExecuteTime?: Date
  nextNotifyTime?: Date
  createTime?: Date
  updateTime?: Date
  logs?: NotifyLogVO[]
}

// 获得支付通知明细
export const getNotifyTaskDetail = (id: string) => {
  return request.get<NotifyTaskVO>({
    url: `/${SYS_BASE_URL}/admin/pay/notify/get-detail?id=` + id
  })
}

// 获得支付通知分页
export const getNotifyTaskPage = (query: any) => {
  return request.get<PageResult<NotifyTaskVO[]>>({
    url: `/${SYS_BASE_URL}/admin/pay/notify/page`,
    params: query
  })
}
