import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface AddressVO {
  id: number
  name: string
  mobile: string
  areaId: number
  detailAddress: string
  defaultStatus: boolean
}

// 查询用户收件地址列表
export const getAddressList = async (params) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/member/address/list`, params })
}
