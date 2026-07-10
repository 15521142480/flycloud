import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface GroupVO {
  id: string
  name: string
  remark: string
  status: number
}

// 查询用户分组列表
export const getGroupPage = async (params: any) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/member/group/page`, params })
}

// 查询用户分组详情
export const getGroup = async (id: string) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/member/group/get?id=` + id })
}

// 新增用户分组
export const createGroup = async (data: GroupVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/admin/member/group/create`, data })
}

// 查询用户分组 - 精简信息列表
export const getSimpleGroupList = async () => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/member/group/list-all-simple` })
}

// 修改用户分组
export const updateGroup = async (data: GroupVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/member/group/update`, data })
}

// 删除用户分组
export const deleteGroup = async (id: string) => {
  return await request.delete({ url: `/${SYS_BASE_URL}/admin/member/group/delete?id=` + id })
}
