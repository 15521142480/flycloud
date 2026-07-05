import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

/** 用户钱包查询参数 */
export interface PayWalletUserReqVO {
  userId: number
}

/** 钱包 VO */
export interface WalletVO {
  id: number
  userId: number
  userType: number
  balance: number
  totalExpense: number
  totalRecharge: number
  freezePrice: number
}

/** 修改会员钱包余额 Request VO */
export interface WalletBalanceUpdateReqVO {
  userId: number
  balance: number
}

/** 查询用户钱包详情 */
export const getWallet = async (params: PayWalletUserReqVO) => {
  return await request.get<WalletVO>({ url: `/${SYS_BASE_URL}/admin/pay/wallet/get`, params })
}

/** 查询会员钱包列表 */
export const getWalletPage = async (params: any) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/pay/wallet/page`, params })
}

/** 修改会员钱包余额 */
export const updateWalletBalance = async (data: WalletBalanceUpdateReqVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/pay/wallet/update-balance`, data })
}
