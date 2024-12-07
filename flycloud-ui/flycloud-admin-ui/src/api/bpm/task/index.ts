import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

/**
 * 任务状态枚举
 */
export enum TaskStatusEnum {
  /**
   * 未开始
   */
  NOT_START = -1,

   /**
   * 待审批
   */
   WAIT = 0,
  /**
   * 审批中
   */
  RUNNING = 1,
  /**
   * 审批通过
   */
  APPROVE = 2,

  /**
   * 审批不通过
   */
  REJECT = 3,
  
  /**
   * 已取消
   */
  CANCEL = 4,
  /**
   * 已退回
   */
  RETURN = 5,
  /**
   * 委派中
   */
  DELEGATE = 6,
  /**
   * 审批通过中
   */
  APPROVING = 7,

}

export type TaskVO = {
  id: number
}

export const getTaskTodoPage = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/task/todoPage`, params })
}

export const getTaskDonePage = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/task/donePage`, params })
}

export const getTaskManagerPage = async (params: any) => {
  return await request.get({ url: `/${BPM_BASE_URL}/task/managerPage`, params })
}

export const approveTask = async (data: any) => {
  return await request.put({ url: `/${BPM_BASE_URL}/task/approve`, data })
}

export const rejectTask = async (data: any) => {
  return await request.put({ url: `/${BPM_BASE_URL}/task/reject`, data })
}

export const getTaskListByProcessInstanceId = async (processInstanceId: string) => {
  return await request.get({
    url: `/${BPM_BASE_URL}/taskList?processInstanceId=` + processInstanceId
  })
}

// 获取所有可回退的节点
export const getTaskListByReturn = async (id: string) => {
  return await request.get({ url: `/${BPM_BASE_URL}/task/listByReturn?id=${id}`})
}

// 回退
export const returnTask = async (data: any) => {
  return await request.put({ url: `/${BPM_BASE_URL}/task/return`, data })
}

// 委派
export const delegateTask = async (data: any) => {
  return await request.put({ url: `/${BPM_BASE_URL}/task/delegate`, data })
}

// 转派
export const transferTask = async (data: any) => {
  return await request.put({ url: `/${BPM_BASE_URL}/task/transfer`, data })
}

// 加签
export const signCreateTask = async (data: any) => {
  return await request.put({ url: `/${BPM_BASE_URL}/task/createSign`, data })
}

// 减签
export const signDeleteTask = async (data: any) => {
  return await request.delete({ url: `/${BPM_BASE_URL}/task/deleteSign`, data })
}

// 获取减签任务列表
export const getChildrenTaskList = async (id: string) => {
  return await request.get({ url: `/${BPM_BASE_URL}/task/listByParentTaskId?parentTaskId=` + id })
}
