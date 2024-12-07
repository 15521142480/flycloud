import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export type DictDataVO = {
  id: number | undefined
  sort: number | undefined
  label: string
  value: string
  dictType: string
  status: number
  colorType: string
  cssClass: string
  remark: string
  createTime: Date
}

// 查询字典数据（精简)列表
export const getSimpleDictDataList = () => {
  return request.get({ url: `${SYS_BASE_URL}/dict/getList` })
}

// 查询字典数据列表
export const getDictDataPage = (params: PageParam) => {
  return request.get({ url: `${SYS_BASE_URL}/dict/list`, params })
}

// 查询字典数据详情
export const getDictData = (id: number) => {
  return request.get({ url: `${SYS_BASE_URL}/dict/get?id=` + id })
}

// 新增字典数据
export const createDictData = (data: DictDataVO) => {
  return request.post({ url: `${SYS_BASE_URL}/dict/create`, data })
}

// 修改字典数据
export const updateDictData = (data: DictDataVO) => {
  return request.put({ url: `${SYS_BASE_URL}/dict/update`, data })
}

// 删除字典数据
export const deleteDictData = (id: number) => {
  return request.delete({ url: `${SYS_BASE_URL}/dict/delete?id=` + id })
}

// 导出字典类型数据
export const exportDictData = (params) => {
  return request.download({ url: `${SYS_BASE_URL}/dict/export`, params })
}