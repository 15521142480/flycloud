import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

/** 支付统计 */
export interface PaySummaryRespVO {
  /** 充值金额，单位分 */
  rechargePrice: number
}

/** 获取钱包充值金额 */
export const getWalletRechargePrice = async () => {
  return await request.get<PaySummaryRespVO>({ url: `/${MALL_BASE_URL}/admin/statistics/pay/summary` })
}
