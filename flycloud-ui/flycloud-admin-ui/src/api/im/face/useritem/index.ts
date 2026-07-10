import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

// 个人表情
export interface ImFaceUserItemVO {
  id: string
  url: string
  name?: string
  width: number
  height: number
}

// 添加个人表情请求
export interface ImFaceUserItemSaveReqVO {
  url: string
  name?: string
  width: number
  height: number
}

// 获取我的个人表情列表
export const getFaceUserItemList = () => {
  return request.get<ImFaceUserItemVO[]>({ url: `/${SYS_BASE_URL}/im/face-user-item/list` })
}

// 添加个人表情
export const createFaceUserItem = (data: ImFaceUserItemSaveReqVO) => {
  return request.post<string>({ url: `/${SYS_BASE_URL}/im/face-user-item/create`, data })
}

// 删除个人表情
export const deleteFaceUserItem = (id: string) => {
  return request.delete({ url: `/${SYS_BASE_URL}/im/face-user-item/delete`, params: { id } })
}
