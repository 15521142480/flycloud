import request from '@/config/axios'
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

// 获得地区树
export const getAreaTree = async () => {
  return await request.get({ url: `/${SYS_BASE_URL}/system/area/tree` })
}

// 获得 IP 对应的地区名
export const getAreaByIp = async (ip: string) => {
  return await request.get({ url: `/${SYS_BASE_URL}/system/area/get-by-ip?ip=` + ip })
}
