import request from '@/config/axios'
import {rsaEncrypt} from "@/utils/crypto/rsa";

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface UserVO {
  id: string
  account: string
  username?: string
  password: string
  name: string
  nickname?: string
  deptId: number
  deptName?: string
  postIds?: string[]
  // postIds: string[]
  email: string
  telephone: string
  mobile?: string
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
  return request.get({ url: `/${SYS_BASE_URL}/user/list`, params })
}

// 获取用户精简信息列表
export const getSimpleUserList = (): Promise<UserVO[]> => {
  return request.get({ url: `/${SYS_BASE_URL}/user/allListSimple` })
}

const normalizeSimpleUser = (user: UserVO): UserVO => {
  if (!user) return user
  return {
    ...user,
    username: user.username ?? user.account,
    nickname: user.nickname ?? user.name,
    mobile: user.mobile ?? user.telephone
  }
}

// 按昵称/姓名模糊搜索用户（IM 加好友使用）
export const getSimpleUserListByNickname = async (nickname: string): Promise<UserVO[]> => {
  const list = await request.get({
    url: `/${SYS_BASE_URL}/user/list`,
    params: { name: nickname, nickname, pageNum: 1, pageSize: 20 }
  })
  const rows = Array.isArray(list) ? list : list?.list || list?.rows || list?.data || []
  return rows.map(normalizeSimpleUser)
}

// 查询所有用户列表
export const getAllUser = () => {
  return request.get({ url: `/${SYS_BASE_URL}/user/all` })
}

// 根据用户编号列表查询用户
export const getUserList = async (ids: string[]): Promise<UserVO[]> => {
  if (!ids?.length) return []
  const list = await request.get({
    url: `/${SYS_BASE_URL}/user/list`,
    params: { ids: ids.join(',') }
  })
  const rows = Array.isArray(list) ? list : list?.list || list?.rows || list?.data || []
  return rows.map(normalizeSimpleUser)
}

// 查询用户详情信息：用户详情、用户菜单、用户权限
export const getDetailInfo = (id: string) => {
  return request.get({ url: `/${SYS_BASE_URL}/user/getDetailInfo/` + id })
}

// 用户详情
export const getUserDetail = (id: string) => {
  return request.get({ url: `/${SYS_BASE_URL}/user/get/` + id })
}

// 当前用户详情
export const getCurUser = () => {
  return request.get({ url: `/${SYS_BASE_URL}/user/get`})
}


// 新增用户
export const createUser = async (data: UserVO) => {
  const newData = { ...data }
  newData.password = await rsaEncrypt(
    newData.password,
    import.meta.env.VITE_FLY_CLOUD_LOGIN_PASSWORD_PUBLIC_KEY
  )
  return request.post({ url: `/${SYS_BASE_URL}/user/saveOrUpdate`, data: newData })
}

// 修改用户
export const updateUser = (data: UserVO) => {
  // data.password = md5(data.password)
  return request.post({ url: `/${SYS_BASE_URL}/user/saveOrUpdate`, data })
}

// 删除用户
export const deleteUser = (id: number) => {
  const ids: number[] = []
  ids.push(id)
  return request.delete({ url: `/${SYS_BASE_URL}/user/delete/` + ids })
}

// 导出用户
export const exportUser = (params) => {
  return request.download({ url: `/${SYS_BASE_URL}/user/export`, params })
}

// 下载用户导入模板
export const importUserTemplate = () => {
  return request.download({ url: `/${SYS_BASE_URL}/user/get-import-template` })
}

// 用户密码重置
export const resetUserPwd = async (id: number, password: string) => {
  const data = {
    id: id,
    password: ''
  }
  data.password = await rsaEncrypt(
    password,
    import.meta.env.VITE_FLY_CLOUD_LOGIN_PASSWORD_PUBLIC_KEY
  )
  return request.post({
    url: `/${SYS_BASE_URL}/user/customResetPassword`, data
  })
}

// 用户状态修改
export const updateUserStatus = (id: number, status: number) => {
  // const data = {
  //   id,
  //   status
  // }
  // return request.post({ url: `/${SYS_BASE_URL}/user/enable`, data: data })
  return request.post({ url: `/${SYS_BASE_URL}/user/enable?id=` + id + `&status=` + status })
}
