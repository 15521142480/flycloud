import request from '@/config/axios'
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER


// seata测试
export const seataTestApi = (isRollback: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/test/seataTest/` + isRollback })
}
