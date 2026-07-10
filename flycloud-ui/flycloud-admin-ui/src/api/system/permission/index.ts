import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface PermissionAssignUserRoleReqVO {
  userId: string
  roleIds: string[]
}

export interface PermissionAssignRoleMenuReqVO {
  roleId: string
  menuIds: string[]
}

export interface PermissionAssignRoleDataScopeReqVO {
  roleId: string
  dataScope: number
  dataScopeDeptIds: string[]
}

// 查询角色拥有的菜单权限
export const getRoleMenuList = async (roleId: string) => {
  return await request.get({ url: `/${SYS_BASE_URL}/permission/list-role-menus?roleId=` + roleId })
}

// 赋予角色菜单权限
export const assignRoleMenu = async (data: PermissionAssignRoleMenuReqVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/permission/assign-role-menu`, data })
}

// 赋予角色数据权限
export const assignRoleDataScope = async (data: PermissionAssignRoleDataScopeReqVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/permission/assign-role-data-scope`, data })
}

// 查询用户拥有的角色数组
export const getUserRoleList = async (userId: string) => {
  return await request.get({ url: `/${SYS_BASE_URL}/userRole/getRoleIdsByUserId/` + userId })
}

// 赋予用户角色
export const assignUserRole = async (data: PermissionAssignUserRoleReqVO) => {
  return await request.post({ url: `/${SYS_BASE_URL}/userRole/update`, data })
}
