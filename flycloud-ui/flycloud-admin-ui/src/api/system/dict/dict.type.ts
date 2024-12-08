import request from '@/config/axios'
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export type DictTypeVO = {
  id: number | undefined
  name: string
  type: string
  status: number
  remark: string
  createTime: Date
}

// 查询字典（精简)列表
export const getSimpleDictTypeList = () => {
  return request.get({ url: `/${SYS_BASE_URL}/dict-type/list-all-simple` })
}

// 查询字典列表
export const getDictTypePage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/dict-type/page`, params })
}

// 查询字典详情
export const getDictType = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/dict-type/get/` + id })
}

// 新增字典
export const createDictType = (data: DictTypeVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/dict-type/create`, data })
}

// 修改字典
export const updateDictType = (data: DictTypeVO) => {
  return request.put({ url: `/${SYS_BASE_URL}/dict-type/update`, data })
}

// 删除字典
export const deleteDictType = (id: number) => {
  return request.delete({ url: `/${SYS_BASE_URL}/dict-type/delete/` + id })
}
// 导出字典类型
export const exportDictType = (params) => {
  return request.download({ url: `/${SYS_BASE_URL}/dict-type/export`, params })
}
