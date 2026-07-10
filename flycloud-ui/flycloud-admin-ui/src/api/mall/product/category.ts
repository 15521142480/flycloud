import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

/**
 * 产品分类
 */
export interface CategoryVO {
  /**
   * 分类编号
   */
  id?: string
  /**
   * 父分类编号
   */
  parentId?: string
  /**
   * 分类名称
   */
  name: string
  /**
   * 移动端分类图
   */
  picUrl: string
  /**
   * 分类排序
   */
  sort: number
  /**
   * 开启状态
   */
  status: number
}

// 创建商品分类
export const createCategory = (data: CategoryVO) => {
  return request.post({ url: `/${MALL_BASE_URL}/admin/product/category/create`, data })
}

// 更新商品分类
export const updateCategory = (data: CategoryVO) => {
  return request.put({ url: `/${MALL_BASE_URL}/admin/product/category/update`, data })
}

// 删除商品分类
export const deleteCategory = (id: string) => {
  return request.delete({ url: `/${MALL_BASE_URL}/admin/product/category/delete?id=${id}` })
}

// 获得商品分类
export const getCategory = (id: string) => {
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/category/get?id=${id}` })
}

// 获得商品分类列表
export const getCategoryList = (params: any) => {
  // return request.get({ url: `/${MALL_BASE_URL}/admin/product/category/list`, params })
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/category/getList`, params })
}

// 获得商品全部分类列表
// export const getCategoryAllList = (params: any) => {
//   return request.get({ url: `/${MALL_BASE_URL}/admin/product/category/getList`, params })
// }
