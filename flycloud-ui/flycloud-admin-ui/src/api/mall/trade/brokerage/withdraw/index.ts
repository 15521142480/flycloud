import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface BrokerageWithdrawVO {
  id: string
  userId: string
  price: number
  feePrice: number
  totalPrice: number
  type: number
  userName: string
  userAccount: string
  bankName: string
  bankAddress: string
  qrCodeUrl: string
  status: number
  auditReason: string
  auditTime: Date
  remark: string
  payTransferId?: string
  transferChannelCode?: string
  transferTime?: Date
  transferErrorMsg?: string
}

// 查询佣金提现列表
export const getBrokerageWithdrawPage = async (params: any) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/trade/brokerage-withdraw/page`, params })
}

// 查询佣金提现详情
export const getBrokerageWithdraw = async (id: string) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/trade/brokerage-withdraw/get?id=` + id })
}

// 佣金提现 - 通过申请
export const approveBrokerageWithdraw = async (id: string) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/trade/brokerage-withdraw/approve?id=` + id })
}

// 审核佣金提现 - 驳回申请
export const rejectBrokerageWithdraw = async (data: BrokerageWithdrawVO) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/trade/brokerage-withdraw/reject`, data })
}
