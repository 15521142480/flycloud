import request from '@/config/axios'
import { DiyPageVO } from '@/api/mall/promotion/diy/page'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface DiyTemplateVO {
  id?: string
  name: string
  used: boolean
  usedTime?: Date
  remark: string
  previewPicUrls: string[]
  property: string
}

export interface DiyTemplatePropertyVO extends DiyTemplateVO {
  pages: DiyPageVO[]
}

// 查询装修模板列表
export const getDiyTemplatePage = async (params: any) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/promotion/diy-template/page`, params })
}

// 查询装修模板详情
export const getDiyTemplate = async (id: string) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/promotion/diy-template/get?id=` + id })
}

// 新增装修模板
export const createDiyTemplate = async (data: DiyTemplateVO) => {
  return await request.post({ url: `/${MALL_BASE_URL}/admin/promotion/diy-template/create`, data })
}

// 修改装修模板
export const updateDiyTemplate = async (data: DiyTemplateVO) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/promotion/diy-template/update`, data })
}

// 删除装修模板
export const deleteDiyTemplate = async (id: string) => {
  return await request.delete({ url: `/${MALL_BASE_URL}/admin/promotion/diy-template/delete?id=` + id })
}

// 使用装修模板
export const useDiyTemplate = async (id: string) => {
  return await request.put({ url: `/${MALL_BASE_URL}/admin/promotion/diy-template/use?id=` + id })
}

// 获得装修模板属性
export const getDiyTemplateProperty = async (id: string) => {
  return await request.get<DiyTemplatePropertyVO>({
    url: `/${MALL_BASE_URL}/admin/promotion/diy-template/get-property?id=` + id
  })
}

// 更新装修模板属性
export const updateDiyTemplateProperty = async (data: DiyTemplateVO) => {
  return await request.put({
    url: `/${MALL_BASE_URL}/admin/promotion/diy-template/update-property`,
    data
  })
}
