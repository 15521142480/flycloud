import request from '@/config/axios'
import { ProcessDefinitionVO } from '@/api/bpm/model'
import { NodeType } from '@/components/SimpleProcessDesignerV2/src/consts'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER


export type Task = {
  id: string
  name: string
}

export type ProcessInstanceVO = {
  id: number
  name: string
  processDefinitionId: string
  category: string
  result: number
  tasks: Task[]
  fields: string[]
  status: number
  remark: string
  businessKey: string
  createTime: string
  endTime: string
  processDefinition?: ProcessDefinitionVO
}

// 用户信息
export type User = {
  id: number,
  name: string,
  avatar: string
}

// 审批任务信息
export type ApprovalTaskInfo = {
  id: number,
  ownerUser: User,
  assigneeUser: User,
  status: number,
  reason: string

}

// 审批节点信息
export type ApprovalNodeInfo = {
  id : number
  name: string
  nodeType: NodeType
  status: number
  startTime?: Date
  endTime?: Date
  candidateUserList?: User[]
  tasks: ApprovalTaskInfo[]
}

export const getProcessInstanceMyPage = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/instance/myPage`, params })
}

export const getProcessInstanceManagerPage = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/instance/managerPage`, params })
}

export const createProcessInstance = async (data) => {
  return await request.post({ url: `/${BPM_BASE_URL}/instance/create`, data: data })
}

export const cancelProcessInstanceByStartUser = async (id: number, reason: string) => {
  const data = {
    id: id,
    reason: reason
  }
  return await request.delete({ url: `/${BPM_BASE_URL}/instance/cancelByStartUser`, data: data })
}

export const cancelProcessInstanceByAdmin = async (id: number, reason: string) => {
  const data = {
    id: id,
    reason: reason
  }
  return await request.delete({ url: `/${BPM_BASE_URL}/instance/cancelByAdmin`, data: data })
}

export const getProcessInstance = async (id: string) => {
  return await request.get({ url: `/${BPM_BASE_URL}/instance/get?id=` + id })
}


// 获得抄送流程分页列表
export const getProcessInstanceCopyPage = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/instanceCopy/page`, params })
}

// 获取审批详情
export const getApprovalDetail = async (processInstanceId?:string, processDefinitionId?:string) => {
  const param = processInstanceId ? '?processInstanceId='+ processInstanceId : '?processDefinitionId='+ processDefinitionId
  return await request.get({ url: `${BPM_BASE_URL}/instance/getApprovalDetail`+ param })
}

// 获取表单字段权限
export const getFormFieldsPermission = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/instance/getFormFieldsPermission`, params })
}
