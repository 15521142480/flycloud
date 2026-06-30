import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

/**
 * 获得商品浏览记录分页
 *
 * @param params 请求参数
 */
export const getBrowseHistoryPage = (params: any) => {
  return request.get({ url: `/${MALL_BASE_URL}/product/browse-history/page`, params })
}
