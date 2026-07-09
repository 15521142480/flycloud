import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface Favorite {
  id?: number
  userId?: string // 用户编号
  spuId?: number | null // 商品 SPU 编号
  name?: string
  picUrl?: string
  price?: number
  salesCount?: number
  createTime?: Date
  status?: number
}

// 获得 ProductFavorite 列表
export const getFavoritePage = (params: PageParam) => {
  return request.get<PageResult<Favorite[]>>({
    url: `/${MALL_BASE_URL}/admin/product/favorite/page`,
    params
  })
}
