import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

// TODO @dhb52：vo 缺少

// 删除优惠劵
export const deleteCoupon = async (id: number) => {
  return request.delete({
    url: `/${MALL_BASE_URL}/admin/promotion/coupon/delete?id=${id}`
  })
}

// 获得优惠劵分页
export const getCouponPage = async (params: PageParam) => {
  return request.get({
    url: `/${MALL_BASE_URL}/admin/promotion/coupon/page`,
    params: params
  })
}

// 发送优惠券
export const sendCoupon = async (data: any) => {
  return request.post({
    url: `/${MALL_BASE_URL}/admin/promotion/coupon/send`,
    data: data
  })
}
