import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

// BPM 流程监听器 VO
export interface ProcessListenerVO {
  id: number // 编号
  name: string // 监听器名字
  type: string // 监听器类型
  status: number // 监听器状态
  event: string // 监听事件
  valueType: string // 监听器值类型
  value: string // 监听器值
}

// BPM 流程监听器 API
export const ProcessListenerApi = {
  // 查询流程监听器分页
  getProcessListenerPage: async (params: any) => {
    return await request.get({ url: `/${BPM_BASE_URL}/processListener/page`, params })
  },

  // 查询流程监听器详情
  getProcessListener: async (id: number) => {
    return await request.get({ url: `/${BPM_BASE_URL}/processListener/get/` + id })
  },

  // 新增流程监听器
  createProcessListener: async (data: ProcessListenerVO) => {
    return await request.post({ url: `/${BPM_BASE_URL}/processListener/create`, data })
  },

  // 修改流程监听器
  updateProcessListener: async (data: ProcessListenerVO) => {
    return await request.put({ url: `/${BPM_BASE_URL}/processListener/update`, data })
  },

  // 删除流程监听器
  deleteProcessListener: async (id: number) => {
    return await request.delete({ url: `/${BPM_BASE_URL}/processListener/delete/` + id })
  }
}
