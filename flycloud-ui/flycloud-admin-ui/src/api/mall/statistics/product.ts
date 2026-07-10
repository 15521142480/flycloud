import request from '@/config/axios'
import { DataComparisonRespVO } from '@/api/mall/statistics/common'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface ProductStatisticsVO {
  id: string
  day: string
  spuId: string
  spuName: string
  spuPicUrl: string
  browseCount: number
  browseUserCount: number
  favoriteCount: number
  cartCount: number
  orderCount: number
  orderPayCount: number
  orderPayPrice: number
  afterSaleCount: number
  afterSaleRefundPrice: number
  browseConvertPercent: number
}

// 商品统计 API
export const ProductStatisticsApi = {
  // 获得商品统计分析
  getProductStatisticsAnalyse: (params: any) => {
    return request.get<DataComparisonRespVO<ProductStatisticsVO>>({
      url: `/${MALL_BASE_URL}/admin/statistics/product/analyse`,
      params
    })
  },
  // 获得商品状况明细
  getProductStatisticsList: (params: any) => {
    return request.get<ProductStatisticsVO[]>({
      url: `/${MALL_BASE_URL}/admin/statistics/product/list`,
      params
    })
  },
  // 导出获得商品状况明细 Excel
  exportProductStatisticsExcel: (params: any) => {
    return request.download({
      url: `/${MALL_BASE_URL}/admin/statistics/product/export-excel`,
      params
    })
  },
  // 获得商品排行榜分页
  getProductStatisticsRankPage: async (params: any) => {
    return await request.get({
      url: `/${MALL_BASE_URL}/admin/statistics/product/rank-page`,
      params
    })
  }
}
