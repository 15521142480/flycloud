import request from '@/config/axios'

const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

export type ProcessDefinitionVO = {
  id: string
  version: number
  deploymentTIme: string
  suspensionState: number
  formType?: number
}

export type ModelVO = {
  id: string
  formName: string
  key: string
  name: string
  description: string
  category: string
  formType: number
  formId: string
  formCustomCreatePath: string
  formCustomViewPath: string
  processDefinition: ProcessDefinitionVO
  status: number
  remark: string
  createTime: string
  bpmnXml: string
}

export const getModelList = async (name: string | undefined) => {
  return await request.get({ url: `/${BPM_BASE_URL}/model/list`, params: { name } })
}

export const getModelPage = async (params) => {
  return await request.get({ url: `/${BPM_BASE_URL}/model/page`, params })
}

export const getModel = async (id: string) => {
  return await request.get({ url: `/${BPM_BASE_URL}/model/get/` + id })
}

export const updateModel = async (data: ModelVO) => {
  return await request.put({ url: `/${BPM_BASE_URL}/model/update`, data: data })
}

export const updateModelBpmn = async (data: ModelVO) => {
  return await request.put({ url: `/${BPM_BASE_URL}/model/update-bpmn`, data: data })
}

// 任务状态修改
export const updateModelState = async (id: string, state: number) => {
  const data = {
    id: id,
    state: state
  }
  return await request.put({ url: `/${BPM_BASE_URL}/model/update-state`, data: data })
}

export const updateModelSortBatch = async (ids: string[]) => {
  return await request.put({ url: `/${BPM_BASE_URL}/model/update-sort-batch`,
    params: {
      ids: ids.join(',')
    }
  })
}

export const createModel = async (data: ModelVO) => {
  return await request.post({ url: `/${BPM_BASE_URL}/model/create`, data: data })
}

export const deleteModel = async (id: string) => {
  return await request.delete({ url: `/${BPM_BASE_URL}/model/delete/` + id })
}

export const deployModel = async (id: string) => {
  return await request.post({ url: `/${BPM_BASE_URL}/model/deploy/` + id })
}
