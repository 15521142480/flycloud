import request from '@/config/axios'
const BPM_BASE_URL = import.meta.env.VITE_BPM_SERVER

export const getActivityList = async (params) => {
  return await request.get({
    url: `/${BPM_BASE_URL}/activity/list?processInstanceId=${params.processInstanceId}`,
    params
  })
}
