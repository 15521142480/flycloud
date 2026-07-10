import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface DeliveryExpressTemplateVO {
  id?: string
  name: string
  chargeMode: number
  sort: number
  charges: ExpressTemplateChargeVO[]
  frees: ExpressTemplateFreeVO[]
}

export declare type ExpressTemplateChargeVO = {
  areaIds: string[]
  startCount: number
  startPrice: number
  extraCount: number
  extraPrice: number
}

export declare type ExpressTemplateFreeVO = {
  areaIds: string[]
  freeCount: number
  freePrice: number
}

// 查询快递运费模板列表
export const getDeliveryExpressTemplatePage = async (params: PageParam) => {
  return await request.get<PageResult<DeliveryExpressTemplateVO[]>>({
    url: `/${MALL_BASE_URL}/admin/trade/delivery/express-template/page`,
    params
  })
}

// 查询快递运费模板详情
export const getDeliveryExpressTemplate = async (id: string) => {
  return await request.get<DeliveryExpressTemplateVO>({
    url: `/${MALL_BASE_URL}/admin/trade/delivery/express-template/get?id=` + id
  })
}

// 查询快递运费模板详情
export const getSimpleTemplateList = async () => {
  return await request.get<DeliveryExpressTemplateVO[]>({
    url: `/${MALL_BASE_URL}/admin/trade/delivery/express-template/list-all-simple`
  })
}

// 新增快递运费模板
export const createDeliveryExpressTemplate = async (data: DeliveryExpressTemplateVO) => {
  return await request.post({
    url: `/${MALL_BASE_URL}/admin/trade/delivery/express-template/create`,
    data
  })
}

// 修改快递运费模板
export const updateDeliveryExpressTemplate = async (data: DeliveryExpressTemplateVO) => {
  return await request.put({
    url: `/${MALL_BASE_URL}/admin/trade/delivery/express-template/update`,
    data
  })
}

// 删除快递运费模板
export const deleteDeliveryExpressTemplate = async (id: string) => {
  return await request.delete({
    url: `/${MALL_BASE_URL}/admin/trade/delivery/express-template/delete?id=` + id
  })
}
