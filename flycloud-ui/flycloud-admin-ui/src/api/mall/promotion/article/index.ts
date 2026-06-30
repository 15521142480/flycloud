import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface ArticleVO {
  id: number
  categoryId: number
  title: string
  author: string
  picUrl: string
  introduction: string
  browseCount: string
  sort: number
  status: number
  spuId: number
  recommendHot: boolean
  recommendBanner: boolean
  content: string
}

// 查询文章管理列表
export const getArticlePage = async (params: any) => {
  return await request.get({ url: `/${MALL_BASE_URL}/promotion/article/page`, params })
}

// 查询文章管理详情
export const getArticle = async (id: number) => {
  return await request.get({ url: `/${MALL_BASE_URL}/promotion/article/get?id=` + id })
}

// 新增文章管理
export const createArticle = async (data: ArticleVO) => {
  return await request.post({ url: `/${MALL_BASE_URL}/promotion/article/create`, data })
}

// 修改文章管理
export const updateArticle = async (data: ArticleVO) => {
  return await request.put({ url: `/${MALL_BASE_URL}/promotion/article/update`, data })
}

// 删除文章管理
export const deleteArticle = async (id: number) => {
  return await request.delete({ url: `/${MALL_BASE_URL}/promotion/article/delete?id=` + id })
}
