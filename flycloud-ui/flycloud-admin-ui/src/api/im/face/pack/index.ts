import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

// 用户端表情包项（精简版）
export interface ImFacePackUserItemVO {
  id: string
  url: string
  name?: string
  width: number
  height: number
}

// 用户端表情包 + 嵌套 items
export interface ImFacePackUserVO {
  id: string
  name: string
  icon?: string
  items: ImFacePackUserItemVO[]
}

// 拉取所有启用的系统表情包（含表情列表）
export const getFacePackList = () => {
  return request.get<ImFacePackUserVO[]>({ url: `/${SYS_BASE_URL}/im/face-pack/list` })
}
