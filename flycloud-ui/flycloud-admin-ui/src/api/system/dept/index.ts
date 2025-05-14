import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface DeptVO {
  id?: number
  name: string
  parentId: number
  status: number
  sort: number
  leaderUserId: number
  phone: string
  email: string
  createTime: Date
}

// 查询部门（精简)列表
export const getSimpleDeptList = async (): Promise<DeptVO[]> => {
  return await request.get({ url: `/${SYS_BASE_URL}/dept/getList` })
}

// 查询部门列表
export const getDeptPage = async (params: PageParam) => {
  return await request.get({ url: `/${SYS_BASE_URL}/dept/list`, params })
}

// 查询部门详情
export const getDept = async (id: number) => {
  return await request.get({ url: `/${SYS_BASE_URL}/dept/get/` + id })
}

// 新增/修改部门
export const saveOrUpdate = async (data: DeptVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/dept/saveOrUpdate`, data: data })
}

// 删除部门
export const deleteDept = async (id: number) => {
  return await request.delete({ url: `/${SYS_BASE_URL}/dept/delete/` + id })
}
