import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface RoleVO {
  id: number
  type: number
  name: string
  code: string
  sort: number
  status: number
  // dataScope: number
  // dataScopeDeptIds: number[]
  createTime: Date
}

export interface UpdateStatusReqVO {
  id: number
  status: number
}

// 查询角色列表
export const getRolePage = async (params: PageParam) => {
  return await request.get({ url: `/${SYS_BASE_URL}/role/list`, params })
}

// 查询角色（精简)列表
export const getSimpleRoleList = async (): Promise<RoleVO[]> => {
  const data = {
    type: 0
  }
  return await request.post({ url: `/${SYS_BASE_URL}/role/getList`, data})
}

// 查询角色详情
export const getRole = async (id: number) => {
  return await request.get({ url: `/${SYS_BASE_URL}/role/get/` + id })
}

// 查询角色菜单列表
export const getRoleMenuTreeList = async (id: number) => {
  return await request.get({ url: `/${SYS_BASE_URL}/role/getRoleMenuTreeList/` + id })
}

// 新增角色
export const createRole = async (data: RoleVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/role/saveOrUpdate`, data })
}

// 修改角色
export const updateRole = async (data: RoleVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/role/saveOrUpdate`, data })
}

// 修改角色菜单权限
export const updateMenuPermission = async (data: RoleVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/role/updateMenuPermission`, data })
}

// 修改角色状态
export const updateRoleStatus = async (data: UpdateStatusReqVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/role/update-status`, data })
}

// 删除角色
export const deleteRole = async (id: number) => {
  return await request.delete({ url: `/${SYS_BASE_URL}/role/delete/` + id })
}

// 导出角色
export const exportRole = (params) => {
  return request.download({
    url: `/${SYS_BASE_URL}/role/export-excel`,
    params
  })
}
