import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerFacePackVO {
  id: number
  name: string
  icon?: string
  sort: number
  status: number
  createTime?: Date
}

// 获得表情包分页
export const getManagerFacePackPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/face-pack/page`, params })
}

// 获得表情包详情
export const getManagerFacePack = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/face-pack/get`, params: { id } })
}

// 新增表情包
export const createManagerFacePack = (data: ImManagerFacePackVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/im/manager/face-pack/create`, data })
}

// 修改表情包
export const updateManagerFacePack = (data: ImManagerFacePackVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/im/manager/face-pack/update`, data })
}

// 删除表情包
export const deleteManagerFacePack = (id: number) => {
  return request.delete({ url: `/${SYS_BASE_URL}/im/manager/face-pack/delete`, params: { id } })
}

// 批量删除表情包
export const deleteManagerFacePackList = (ids: number[]) => {
  return request.delete({
    url: `/${SYS_BASE_URL}/im/manager/face-pack/delete-list`,
    params: { ids: ids.join(',') }
  })
}
