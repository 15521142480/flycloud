import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

export type FormVO = {
  id: number
  name: string
  conf: string
  fields: string[]
  status: number
  remark: string
  createTime: string
}

// 获得工作流的表单定义分页
export const getFormPage = async (params) => {
  return await request.get({
    url: `${BPM_BASE_URL}/form/page`,
    params
  })
}

// 获得动态表单的精简列表
export const getFormSimpleList = async () => {
  return await request.get({
    url: `${BPM_BASE_URL}/form/list`
  })
}

// 创建工作流的表单定义
export const createForm = async (data: FormVO) => {
  return await request.post({
    url: `${BPM_BASE_URL}/form/create`,
    data: data
  })
}

// 更新工作流的表单定义
export const updateForm = async (data: FormVO) => {
  return await request.put({
    url: `${BPM_BASE_URL}/form/update`,
    data: data
  })
}

// 删除工作流的表单定义
export const deleteForm = async (id: number) => {
  return await request.delete({
    url: `${BPM_BASE_URL}/form/delete?id=` + id
  })
}

// 获得工作流的表单定义
export const getForm = async (id: number) => {
  return await request.get({
    url: `${BPM_BASE_URL}/form/get?id=` + id
  })
}
