import request from '@/config/axios'
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface MenuVO {
  id: number
  name: string
  permission: string
  buttonPermission: string
  type: number
  sort: number
  parentId: number
  path: string
  icon: string
  component: string
  componentName?: string
  status: number
  visible: boolean
  keepAlive: boolean
  alwaysShow?: boolean
  createTime: Date
}

// 查询菜单树型列表
export const getMenuTreeList = (params) => {
  return request.get({ url: `/${SYS_BASE_URL}/menu/getTreeList`, params })
}

// 查询菜单分页列表
export const getMenuPageList = (params) => {
  return request.get({ url: `/${SYS_BASE_URL}/menu/page`, params })
}

// 查询菜单（精简）列表
export const getMenusList = () => {
  return request.get({ url: `/${SYS_BASE_URL}/menu/list` })
}


// 获取菜单详情
export const getMenu = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/menu/get/` + id })
}

// 新增菜单
export const createMenu = (data: MenuVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/menu/create`, data })
}

// 修改菜单
export const updateMenu = (data: MenuVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/menu/update`, data })
}

// 修改菜单状态
export const updateStatus = (id: number, status: number) => {
  return request.post({ url: `${SYS_BASE_URL}/menu/enable?id=` + id + `&status=` + status})
}

// 删除菜单
export const deleteMenu = (id: number) => {
  return request.delete({ url: `/${SYS_BASE_URL}/menu/delete/` + id })
}
