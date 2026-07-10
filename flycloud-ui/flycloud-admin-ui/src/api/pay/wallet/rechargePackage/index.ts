import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface WalletRechargePackageVO {
  id: string
  name: string
  payPrice: number
  bonusPrice: number
  status: number
}

// 查询套餐充值列表
export const getWalletRechargePackagePage = async (params) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/pay/wallet-recharge-package/page`, params })
}

// 查询套餐充值详情
export const getWalletRechargePackage = async (id: string) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/pay/wallet-recharge-package/get?id=` + id })
}

// 新增套餐充值
export const createWalletRechargePackage = async (data: WalletRechargePackageVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/admin/pay/wallet-recharge-package/create`, data })
}

// 修改套餐充值
export const updateWalletRechargePackage = async (data: WalletRechargePackageVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/pay/wallet-recharge-package/update`, data })
}

// 删除套餐充值
export const deleteWalletRechargePackage = async (id: string) => {
  return await request.delete({
    url: `/${SYS_BASE_URL}/admin/pay/wallet-recharge-package/delete?id=` + id
  })
}
