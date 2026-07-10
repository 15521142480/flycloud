import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface CommentVO {
  id: string
  userId: string
  userNickname: string
  userAvatar: string
  anonymous: boolean
  orderId: string
  orderItemId: string
  spuId: string
  spuName: string
  skuId: string
  visible: boolean
  scores: number
  descriptionScores: number
  benefitScores: number
  content: string
  picUrls: string
  replyStatus: boolean
  replyUserId: string
  replyContent: string
  replyTime: Date
}

// 查询商品评论列表
export const getCommentPage = async (params) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/product/comment/page`, params })
}

// 查询商品评论详情
export const getComment = async (id: string) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/product/comment/get?id=` + id })
}

// 添加自评
export const createComment = async (data: CommentVO) => {
  return await request.post({ url: `/${MALL_BASE_URL}/admin/product/comment/create`, data })
}

// 显示 / 隐藏评论
export const updateCommentVisible = async (data: any) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/product/comment/update-visible`, data })
}

// 商家回复
export const replyComment = async (data: any) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/product/comment/reply`, data })
}
