import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface UserVO {
  id: number
  username: string
  name: string
  deptId: number
  postIds: string[]
  email: string
  mobile: string
  sex: number
  avatar: string
  loginIp: string
  status: number
  remark: string
  loginDate: Date
  createTime: Date
}

// 查询用户管理列表
export const getUserPage = (params: PageParam) => {
  return request.get({ url: `${SYS_BASE_URL}/user/page`, params })
}

// 获取用户精简信息列表
export const getSimpleUserList = (): Promise<UserVO[]> => {
  return request.get({ url: `${SYS_BASE_URL}/user/allListSimple` })
}

// 查询所有用户列表
export const getAllUser = () => {
  return request.get({ url: `${SYS_BASE_URL}/user/all` })
}

// 查询用户详情
export const getUser = (id: number) => {
  return request.get({ url: `${SYS_BASE_URL}/user/get?id=` + id })
}

// 新增用户
export const createUser = (data: UserVO) => {
  return request.post({ url: `${SYS_BASE_URL}/user/create`, data })
}

// 修改用户
export const updateUser = (data: UserVO) => {
  return request.put({ url: `${SYS_BASE_URL}/user/update`, data })
}

// 删除用户
export const deleteUser = (id: number) => {
  return request.delete({ url: `${SYS_BASE_URL}/user/delete?id=` + id })
}

// 导出用户
export const exportUser = (params) => {
  return request.download({ url: `${SYS_BASE_URL}/user/export`, params })
}

// 下载用户导入模板
export const importUserTemplate = () => {
  return request.download({ url: `${SYS_BASE_URL}/user/get-import-template` })
}

// 用户密码重置
export const resetUserPwd = (id: number, password: string) => {
  const data = {
    id,
    password
  }
  return request.put({ url: `${SYS_BASE_URL}/user/update-password`, data: data })
}

// 用户状态修改
export const updateUserStatus = (id: number, status: number) => {
  const data = {
    id,
    status
  }
  return request.put({ url: `${SYS_BASE_URL}/user/update-status`, data: data })
}
