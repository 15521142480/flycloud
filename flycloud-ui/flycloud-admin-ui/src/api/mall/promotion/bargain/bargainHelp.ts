import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface BargainHelpVO {
  id: number
  record: number
  userId: number
  reducePrice: number
  endTime: Date
}

// 查询砍价记录列表
export const getBargainHelpPage = async (params) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/promotion/bargain-help/page`, params })
}
