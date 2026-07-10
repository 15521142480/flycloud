import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

// 用户端能看到的频道素材详情
export interface ImChannelMaterialRespVO {
  id: string
  channelId: string
  type: number
  title: string
  coverUrl?: string
  summary?: string
  content?: string
  url?: string
}

// 获取频道素材详情；用于客户端点击图文卡片渲染详情页
export const getChannelMaterial = (id: string) => {
  return request.get<ImChannelMaterialRespVO>({
    url: `/${SYS_BASE_URL}/im/channel/material/get`,
    params: { id }
  })
}
