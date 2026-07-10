import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface DemoOrderVO {
  spuId: string
  createTime: Date
}

// 创建示例订单
export function createDemoOrder(data: DemoOrderVO) {
  return request.post({
    url: `/${SYS_BASE_URL}/admin/pay/demo-order/create`,
    data: data
  })
}

// 获得示例订单分页
export function getDemoOrderPage(query: PageParam) {
  return request.get({
    url: `/${SYS_BASE_URL}/admin/pay/demo-order/page`,
    params: query
  })
}

// 退款示例订单
export function refundDemoOrder(id: string) {
  return request.put({
    url: `/${SYS_BASE_URL}/admin/pay/demo-order/refund?id=` + id
  })
}
