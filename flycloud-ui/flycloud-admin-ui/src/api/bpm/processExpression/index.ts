import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

// BPM 流程表达式 VO
export interface ProcessExpressionVO {
  id: number // 编号
  name: string // 表达式名字
  status: number // 表达式状态
  expression: string // 表达式
}

// BPM 流程表达式 API
export const ProcessExpressionApi = {
  // 查询BPM 流程表达式分页
  getProcessExpressionPage: async (params: any) => {
    return await request.get({ url: `/${BPM_BASE_URL}/processExpression/page`, params })
  },

  // 查询BPM 流程表达式详情
  getProcessExpression: async (id: number) => {
    return await request.get({ url: `/${BPM_BASE_URL}/processExpression/get/` + id })
  },

  // 新增BPM 流程表达式
  createProcessExpression: async (data: ProcessExpressionVO) => {
    return await request.post({ url: `/${BPM_BASE_URL}/processExpression/create`, data })
  },

  // 修改BPM 流程表达式
  updateProcessExpression: async (data: ProcessExpressionVO) => {
    return await request.put({ url: `/${BPM_BASE_URL}/processExpression/update`, data })
  },

  // 删除BPM 流程表达式
  deleteProcessExpression: async (id: number) => {
    return await request.delete({ url: `/${BPM_BASE_URL}/processExpression/delete/` + id })
  },

  // 导出BPM 流程表达式 Excel
  exportProcessExpression: async (params) => {
    return await request.download({ url: `/${BPM_BASE_URL}/processExpression/exportExcel`, params })
  }
}
