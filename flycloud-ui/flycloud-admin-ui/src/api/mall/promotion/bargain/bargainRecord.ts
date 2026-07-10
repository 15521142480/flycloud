import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface BargainRecordVO {
  id: string
  activityId: string
  userId: string
  spuId: string
  skuId: string
  bargainFirstPrice: number
  bargainPrice: number
  status: number
  orderId: string
  endTime: Date
}

// 查询砍价记录列表
export const getBargainRecordPage = async (params) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/promotion/bargain-record/page`, params })
}
