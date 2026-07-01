import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

// 查询佣金记录列表
export const getBrokerageRecordPage = async (params: any) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/trade/brokerage-record/page`, params })
}

// 查询佣金记录详情
export const getBrokerageRecord = async (id: number) => {
  return await request.get({ url: `/${MALL_BASE_URL}/admin/trade/brokerage-record/get?id=` + id })
}
