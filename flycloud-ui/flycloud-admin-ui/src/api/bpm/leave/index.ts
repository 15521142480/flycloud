import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

export type LeaveVO = {
  id: number
  status: number
  type: number
  reason: string
  processInstanceId: string
  startTime: string
  endTime: string
  createTime: string
}

// 创建请假申请
export const createLeave = async (data: LeaveVO) => {
  return await request.post({ url: `/${BPM_BASE_URL}/oa/leave/create`, data: data })
}

// 获得请假申请
export const getLeave = async (id: number) => {
  return await request.get({ url: `/${BPM_BASE_URL}/oa/leave/get/` + id })
}

// 获得请假申请分页
export const getLeavePage = async (params: PageParam) => {
  return await request.get({ url: `/${BPM_BASE_URL}/oa/leave/page`, params })
}
