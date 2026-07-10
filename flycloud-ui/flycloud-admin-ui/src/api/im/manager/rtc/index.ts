import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerRtcCallVO {
  id: string
  room: string
  conversationType: number
  mediaType: number
  inviterUserId: string
  inviterNickname?: string
  groupId?: string
  groupName?: string
  status: number
  endReason?: number
  startTime: Date
  acceptTime?: Date
  endTime?: Date
  createTime: Date
}

export interface ImManagerRtcParticipantVO {
  id: string
  callId: string
  userId: string
  userNickname?: string
  role: number
  status: number
  inviteTime: Date
  acceptTime?: Date
  leaveTime?: Date
}

// 获得通话记录分页
export const getManagerRtcCallPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/rtc/page`, params })
}

// 获得通话参与者列表
export const getManagerRtcCallParticipantList = (id: string) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/rtc/participant-list`, params: { id } })
}
