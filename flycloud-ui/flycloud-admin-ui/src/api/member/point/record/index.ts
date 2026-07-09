import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface RecordVO {
  id: number
  bizId: string
  bizType: string
  title: string
  description: string
  point: number
  totalPoint: number
  userId: string
  createDate: Date
}

// 查询用户积分记录列表
export const getRecordPage = async (params) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/member/point/record/page`, params })
}
