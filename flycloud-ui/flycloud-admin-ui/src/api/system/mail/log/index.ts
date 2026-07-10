import request from '@/config/axios'

export interface MailLogVO {
  id: string
  userId: string
  userType: number
  toMail: string
  accountId: string
  fromMail: string
  templateId: string
  templateCode: string
  templateNickname: string
  templateTitle: string
  templateContent: string
  templateParams: string
  sendStatus: number
  sendTime: Date
  sendMessageId: string
  sendException: string
}

// 查询邮件日志列表
export const getMailLogPage = async (params: PageParam) => {
  return await request.get({ url: '/system/mail-log/page', params })
}

// 查询邮件日志详情
export const getMailLog = async (id: string) => {
  return await request.get({ url: '/system/mail-log/get/' + id })
}
