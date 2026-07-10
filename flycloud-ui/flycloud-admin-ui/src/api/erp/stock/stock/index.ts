import request from '@/config/axios'

// ERP 产品库存 VO
export interface StockVO {
  // 编号
  id: string
  // 产品编号
  productId: string
  // 仓库编号
  warehouseId: string
  // 库存数量
  count: number
}

// ERP 产品库存 API
export const StockApi = {
  // 查询产品库存分页
  getStockPage: async (params: any) => {
    return await request.get({ url: `/erp/stock/page`, params })
  },

  // 查询产品库存详情
  getStock: async (id: string) => {
    return await request.get({ url: `/erp/stock/get/` + id })
  },

  // 查询产品库存详情
  getStock2: async (productId: string, warehouseId: string) => {
    return await request.get({ url: `/erp/stock/get`, params: { productId, warehouseId } })
  },

  // 获得产品库存数量
  getStockCount: async (productId: string) => {
    return await request.get({ url: `/erp/stock/get-count`, params: { productId } })
  },

  // 导出产品库存 Excel
  exportStock: async (params) => {
    return await request.download({ url: `/erp/stock/export-excel`, params })
  }
}
