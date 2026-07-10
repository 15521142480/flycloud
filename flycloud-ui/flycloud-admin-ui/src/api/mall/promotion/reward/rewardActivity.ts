import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface RewardActivityVO {
  id?: string
  name?: string
  startTime?: Date
  endTime?: Date
  startAndEndTime?: Date[] // 只前端使用
  remark?: string
  conditionType?: number
  productScope?: number
  rules: RewardRule[]
  productScopeValues: string[] // 商品范围：值为品类编号列表、商品编号列表
  // 如下仅用于表单，不提交
  productCategoryIds: string[]
  productSpuIds: string[]
}

// 优惠规则
export interface RewardRule {
  limit?: number
  discountPrice?: number
  freeDelivery?: boolean
  point: number
  giveCouponTemplateCounts?: {
    [key: string]: number
  }
}

// 新增满减送活动
export const createRewardActivity = async (data: RewardActivityVO) => {
  return await request.post({ url: `/${MALL_BASE_URL}/admin/promotion/reward-activity/create`, data })
}

// 更新满减送活动
export const updateRewardActivity = async (data: RewardActivityVO) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/promotion/reward-activity/update`, data })
}

// 查询满减送活动列表
export const getRewardActivityPage = async (params) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/promotion/reward-activity/page`, params })
}

// 查询满减送活动详情
export const getReward = async (id: string) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/promotion/reward-activity/get?id=` + id })
}

// 删除满减送活动
export const deleteRewardActivity = async (id: string) => {
  return await request.delete({
    url: `/${MALL_BASE_URL}/admin/promotion/reward-activity/delete?id=` + id
  })
}

// 关闭满减送活动
export const closeRewardActivity = async (id: string) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/promotion/reward-activity/close?id=` + id })
}
