import request from '@/config/axios'

// ERP 产品库存明细 VO
export interface StockRecordVO {
  id: string // 编号
  productId: string // 产品编号
  warehouseId: string // 仓库编号
  count: number // 出入库数量
  totalCount: number // 总库存量
  bizType: number // 业务类型
  bizId: string // 业务编号
  bizItemId: string // 业务项编号
  bizNo: string // 业务单号
}

// ERP 产品库存明细 API
export const StockRecordApi = {
  // 查询产品库存明细分页
  getStockRecordPage: async (params: any) => {
    return await request.get({ url: `/erp/stock-record/page`, params })
  },

  // 查询产品库存明细详情
  getStockRecord: async (id: string) => {
    return await request.get({ url: `/erp/stock-record/get/` + id })
  },

  // 导出产品库存明细 Excel
  exportStockRecord: async (params) => {
    return await request.download({ url: `/erp/stock-record/export-excel`, params })
  }
}
