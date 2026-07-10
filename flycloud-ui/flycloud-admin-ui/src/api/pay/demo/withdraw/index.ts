import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface PayDemoWithdrawVO {
  id?: string
  subject: string
  price: number
  userName: string
  userAccount: string
  type: number
  status?: number
  payTransferId?: string
  transferChannelCode?: string
  transferTime?: Date
  transferErrorMsg?: string
}

// 查询示例提现单列表
export const getDemoWithdrawPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/admin/pay/demo-withdraw/page`, params })
}

// 创建示例提现单
export const createDemoWithdraw = (data: PayDemoWithdrawVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/admin/pay/demo-withdraw/create`, data })
}

// 发起提现单转账
export const transferDemoWithdraw = (id: string) => {
  return request.post({ url: `/${SYS_BASE_URL}/admin/pay/demo-withdraw/transfer`, params: { id } })
}
