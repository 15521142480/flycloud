import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface WalletTransactionVO {
  id: string
  walletId: string
  title: string
  price: number
  balance: number
}

// 查询会员钱包流水列表
export const getWalletTransactionPage = async (params) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/pay/wallet-transaction/page`, params })
}
