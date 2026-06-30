import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ConfigVO {
  id?: number
  pointTradeDeductEnable: boolean
  pointTradeDeductUnitPrice: number
  pointTradeDeductMaxPrice: number
  pointTradeGivePoint: number
}

// 查询积分设置详情
export const getConfig = async () => {
  return await request.get<ConfigVO>({ url: `/${SYS_BASE_URL}/admin/member/config/get` })
}

// 新增修改积分设置
export const saveConfig = async (data: ConfigVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/member/config/save`, data })
}
