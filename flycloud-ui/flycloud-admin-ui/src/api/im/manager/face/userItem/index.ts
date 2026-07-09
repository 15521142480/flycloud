import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerFaceUserItemVO {
  id: number
  userId: string
  userNickname?: string
  url: string
  name?: string
  width?: number
  height?: number
  createTime?: Date
}

// 获得用户表情分页
export const getManagerFaceUserItemPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/face-user-item/page`, params })
}

// 删除用户表情
export const deleteManagerFaceUserItem = (id: number) => {
  return request.delete({
    url: `/${SYS_BASE_URL}/im/manager/face-user-item/delete`,
    params: { id }
  })
}
