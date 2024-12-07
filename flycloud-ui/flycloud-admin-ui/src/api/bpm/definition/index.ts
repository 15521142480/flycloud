import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER


export const getProcessDefinition = async (id?: string, key?: string) => {
  return await request.get({
    url: `/${BPM_BASE_URL}/processDefinition/get`,
    params: { id, key }
  })
}

export const getProcessDefinitionPage = async (params) => {
  return await request.get({
    url: `/${BPM_BASE_URL}/processDefinition/page`,
    params
  })
}

export const getProcessDefinitionList = async (params) => {
  return await request.get({
    url: `/${BPM_BASE_URL}/processDefinition/list`,
    params
  })
}
