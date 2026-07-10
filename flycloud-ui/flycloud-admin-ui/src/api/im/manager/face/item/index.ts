import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerFacePackItemVO {
  id: string
  packId: string
  url: string
  name?: string
  width: number
  height: number
  sort: number
  status: number
  createTime?: Date
}

// 获得表情分页
export const getManagerFacePackItemPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/face-pack-item/page`, params })
}

// 获得表情详情
export const getManagerFacePackItem = (id: string) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/face-pack-item/get`, params: { id } })
}

// 新增表情
export const createManagerFacePackItem = (data: ImManagerFacePackItemVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/im/manager/face-pack-item/create`, data })
}

// 修改表情
export const updateManagerFacePackItem = (data: ImManagerFacePackItemVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/im/manager/face-pack-item/update`, data })
}

// 删除表情
export const deleteManagerFacePackItem = (id: string) => {
  return request.delete({
    url: `/${SYS_BASE_URL}/im/manager/face-pack-item/delete`,
    params: { id }
  })
}

// 批量删除表情
export const deleteManagerFacePackItemList = (ids: string[]) => {
  return request.delete({
    url: `/${SYS_BASE_URL}/im/manager/face-pack-item/delete-list`,
    params: { ids: ids.join(',') }
  })
}
