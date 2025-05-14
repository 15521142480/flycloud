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
  return request.get({ url: `${SYS_BASE_URL}/dictData/getList` })
}

// 查询字典数据列表
export const getDictDataPage = (params: PageParam) => {
  return request.get({ url: `${SYS_BASE_URL}/dictData/list`, params })
}

// 查询字典数据详情
export const getDictData = (id: number) => {
  return request.get({ url: `${SYS_BASE_URL}/dictData/get/` + id })
}

// 新增/修改字典数据
export const saveOrUpdate = (data: DictDataVO) => {
  return request.post({ url: `${SYS_BASE_URL}/dictData/saveOrUpdate`, data })
}

// 删除字典数据
export const deleteDictData = (id: number) => {
  return request.delete({ url: `${SYS_BASE_URL}/dictData/delete/` + id })
}

// 导出字典类型数据
export const exportDictData = (params) => {
  return request.download({ url: `${SYS_BASE_URL}/dictData/export`, params })
}
