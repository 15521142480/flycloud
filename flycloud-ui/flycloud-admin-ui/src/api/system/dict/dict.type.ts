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
  return request.get({ url: `/${SYS_BASE_URL}/dictType/list` })
}

// 查询字典列表
export const getDictTypePage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/dictType/page`, params })
}

// 查询字典详情
export const getDictType = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/dictType/get/` + id })
}

// 新增/修改字典
export const saveOrUpdate = (data: DictTypeVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/dictType/saveOrUpdate`, data })
}


// 删除字典
export const deleteDictType = (id: number) => {
  return request.delete({ url: `/${SYS_BASE_URL}/dictType/delete/` + id })
}

// 导出字典类型
export const exportDictType = (params) => {
  return request.download({ url: `/${SYS_BASE_URL}/dictType/export`, params })
}
