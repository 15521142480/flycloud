import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerChannelVO {
  id: number
  code: string
  name: string
  avatar?: string
  sort: number
  status: number
  createTime?: Date
}

// 获得频道分页
export const getManagerChannelPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/channel/page`, params })
}

// 获得频道详情
export const getManagerChannel = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/channel/get`, params: { id } })
}

// 新增频道
export const createManagerChannel = (data: ImManagerChannelVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/im/manager/channel/create`, data })
}

// 修改频道
export const updateManagerChannel = (data: ImManagerChannelVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/im/manager/channel/update`, data })
}

// 删除频道
export const deleteManagerChannel = (id: number) => {
  return request.delete({ url: `/${SYS_BASE_URL}/im/manager/channel/delete`, params: { id } })
}

// 获得启用的频道精简列表（表单选择用）
export const getSimpleChannelList = () => {
  return request.get<ImManagerChannelVO[]>({
    url: `/${SYS_BASE_URL}/im/manager/channel/simple-list`
  })
}
