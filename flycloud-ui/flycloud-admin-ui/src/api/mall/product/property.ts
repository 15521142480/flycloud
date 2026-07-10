import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

/**
 * 商品属性
 */
export interface PropertyVO {
  id?: string
  /** 名称 */
  name: string
  /** 备注 */
  remark?: string
}

/**
 * 属性值
 */
export interface PropertyValueVO {
  id?: string
  /** 属性项的编号 */
  propertyId?: string
  /** 名称 */
  name: string
  /** 备注 */
  remark?: string
}

// ------------------------ 属性项 -------------------

// 创建属性项
export const createProperty = (data: PropertyVO) => {
  return request.post({ url: `/${MALL_BASE_URL}/admin/product/property/create`, data })
}

// 更新属性项
export const updateProperty = (data: PropertyVO) => {
  return request.put({ url: `/${MALL_BASE_URL}/admin/product/property/update`, data })
}

// 删除属性项
export const deleteProperty = (id: string) => {
  return request.delete({ url: `/${MALL_BASE_URL}/admin/product/property/delete?id=${id}` })
}

// 获得属性项
export const getProperty = (id: string): Promise<PropertyVO> => {
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/property/get?id=${id}` })
}

// 获得属性项分页
export const getPropertyPage = (params: PageParam) => {
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/property/page`, params })
}

// 获得属性项精简列表
export const getPropertySimpleList = (): Promise<PropertyVO[]> => {
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/property/simple-list` })
}

// ------------------------ 属性值 -------------------

// 获得属性值分页
export const getPropertyValuePage = (params: PageParam & any) => {
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/property/value/page`, params })
}

// 获得属性值
export const getPropertyValue = (id: string): Promise<PropertyValueVO> => {
  return request.get({ url: `/${MALL_BASE_URL}/admin/product/property/value/get?id=${id}` })
}

// 创建属性值
export const createPropertyValue = (data: PropertyValueVO) => {
  return request.post({ url: `/${MALL_BASE_URL}/admin/product/property/value/create`, data })
}

// 更新属性值
export const updatePropertyValue = (data: PropertyValueVO) => {
  return request.put({ url: `/${MALL_BASE_URL}/admin/product/property/value/update`, data })
}

// 删除属性值
export const deletePropertyValue = (id: string) => {
  return request.delete({ url: `/${MALL_BASE_URL}/admin/product/property/value/delete?id=${id}` })
}

// 获得属性值精简列表
export const getPropertyValueSimpleList = (propertyId: string): Promise<PropertyValueVO[]> => {
  return request.get({
    url: `/${MALL_BASE_URL}/admin/product/property/value/simple-list`,
    params: { propertyId }
  })
}
