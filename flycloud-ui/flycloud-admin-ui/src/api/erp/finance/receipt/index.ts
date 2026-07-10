import request from '@/config/axios'

// ERP 收款单 VO
export interface FinanceReceiptVO {
  id: string // 收款单编号
  no: string // 收款单号
  customerId: string // 客户编号
  receiptTime: Date // 收款时间
  totalPrice: number // 合计金额，单位：元
  status: number // 状态
  remark: string // 备注
}

// ERP 收款单 API
export const FinanceReceiptApi = {
  // 查询收款单分页
  getFinanceReceiptPage: async (params: any) => {
    return await request.get({ url: `/erp/finance-receipt/page`, params })
  },

  // 查询收款单详情
  getFinanceReceipt: async (id: string) => {
    return await request.get({ url: `/erp/finance-receipt/get/` + id })
  },

  // 新增收款单
  createFinanceReceipt: async (data: FinanceReceiptVO) => {
    return await request.post({ url: `/erp/finance-receipt/create`, data })
  },

  // 修改收款单
  updateFinanceReceipt: async (data: FinanceReceiptVO) => {
    return await request.put({ url: `/erp/finance-receipt/update`, data })
  },

  // 更新收款单的状态
  updateFinanceReceiptStatus: async (id: string, status: number) => {
    return await request.put({
      url: `/erp/finance-receipt/update-status`,
      params: {
        id,
        status
      }
    })
  },

  // 删除收款单
  deleteFinanceReceipt: async (ids: string[]) => {
    return await request.delete({
      url: `/erp/finance-receipt/delete`,
      params: {
        ids: ids.join(',')
      }
    })
  },

  // 导出收款单 Excel
  exportFinanceReceipt: async (params: any) => {
    return await request.download({ url: `/erp/finance-receipt/export-excel`, params })
  }
}
