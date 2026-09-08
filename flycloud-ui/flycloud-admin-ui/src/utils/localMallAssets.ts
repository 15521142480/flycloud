const LOCAL_MALL_ASSET_PREFIXES = ['/static/img/icon/', '/static/img/decoration/']

const LEGACY_MALL_ASSET_PATH_MAP: Record<string, string> = {
  'mall.yudao.iocoder.cn/static/images/1-001.png': '/static/img/icon/tabbar/home.png',
  'mall.yudao.iocoder.cn/static/images/1-002.png': '/static/img/icon/tabbar/home-active.png',
  'mall.yudao.iocoder.cn/static/images/2-001.png': '/static/img/icon/tabbar/category.png',
  'mall.yudao.iocoder.cn/static/images/2-002.png': '/static/img/icon/tabbar/category-active.png',
  'mall.yudao.iocoder.cn/static/images/3-001.png': '/static/img/icon/tabbar/cart.png',
  'mall.yudao.iocoder.cn/static/images/3-002.png': '/static/img/icon/tabbar/cart-active.png',
  'mall.yudao.iocoder.cn/static/images/4-001.png': '/static/img/icon/tabbar/user.png',
  'mall.yudao.iocoder.cn/static/images/4-002.png': '/static/img/icon/tabbar/user-active.png',
  'mall.yudao.iocoder.cn/static/images/xinjian.png': '/static/img/icon/notice/new.png',

  'test.yudao.iocoder.cn/static/img/diy/groupon.png': '/static/img/icon/activity/groupon.png',
  'test.yudao.iocoder.cn/static/img/diy/seckill.png': '/static/img/icon/activity/seckill.png',
  'test.yudao.iocoder.cn/static/img/diy/coupon.png': '/static/img/icon/activity/coupon.png',
  'test.yudao.iocoder.cn/static/img/diy/sign.png': '/static/img/icon/activity/sign.png',
  'test.yudao.iocoder.cn/static/img/diy/point.png': '/static/img/icon/activity/point.png',
  'test.yudao.iocoder.cn/static/img/diy/recharge.png': '/static/img/icon/payment/recharge.png',
  'test.yudao.iocoder.cn/static/img/diy/withdraw.png': '/static/img/icon/payment/withdraw.png',
  'test.yudao.iocoder.cn/static/img/diy/goods.png': '/static/img/icon/goods/goods.png',
  'test.yudao.iocoder.cn/static/img/diy/goods-collect.png': '/static/img/icon/goods/collect.png',
  'test.yudao.iocoder.cn/static/img/diy/goods-log.png': '/static/img/icon/goods/history.png',
  'test.yudao.iocoder.cn/static/img/diy/setting.png': '/static/img/icon/user/settings.png',
  'test.yudao.iocoder.cn/static/img/diy/feedback.png': '/static/img/icon/user/feedback.png',
  'test.yudao.iocoder.cn/static/img/diy/commission.png': '/static/img/icon/user/commission.png',
  'test.yudao.iocoder.cn/static/img/diy/faq.png': '/static/img/icon/user/faq.png',
  'test.yudao.iocoder.cn/static/img/diy/about-us.png': '/static/img/icon/user/about.png',
  'test.yudao.iocoder.cn/static/img/diy/privacy.png': '/static/img/icon/user/privacy.png',
  'test.yudao.iocoder.cn/static/img/diy/address.png': '/static/img/icon/user/address.png',
  'test.yudao.iocoder.cn/static/img/diy/invoice.png': '/static/img/icon/user/invoice.png',
  'test.yudao.iocoder.cn/static/img/diy/chat-index.png':
    '/static/img/icon/user/customer-service.png',
  'test.yudao.iocoder.cn/static/img/diy/commission-goods.png':
    '/static/img/icon/user/commission-goods.png',
  'test.yudao.iocoder.cn/static/img/diy/chat.png': '/static/img/icon/navigation/chat.png',
  'test.yudao.iocoder.cn/static/img/diy/home.png': '/static/img/icon/navigation/home.png',

  'test.yudao.iocoder.cn/static/img/diy/decorate_my.png':
    '/static/img/decoration/user/decorate-background.png',
  'test.yudao.iocoder.cn/static/img/diy/geren-bg.png':
    '/static/img/decoration/user/navigation-background.png',
  'test.yudao.iocoder.cn/20251111/1755531278_1762864213249.jpeg':
    '/static/img/decoration/home/floating-action.jpeg',
  'test.yudao.iocoder.cn/20251111/blob_1752042302026_1762825876900.jpg':
    '/static/img/decoration/home/point-banner.jpg'
}

/**
 * 将商城装修中的本地资源或历史 yudao 地址转换成商城 H5 的项目资源地址。
 */
export const getLocalMallAssetUrl = (pathOrUrl: string): string => {
  const value = pathOrUrl.trim()
  if (!value) return ''

  const cleanValue = value.replace(/[?#].*$/, '')
  let localPath = LOCAL_MALL_ASSET_PREFIXES.some((prefix) => cleanValue.startsWith(prefix))
    ? cleanValue
    : ''

  if (!localPath) {
    const legacyKey = cleanValue.replace(/^https?:\/\//i, '').replace(/^\/\//, '')
    localPath = LEGACY_MALL_ASSET_PATH_MAP[legacyKey] || ''
  }
  if (!localPath) return ''

  const mallH5Domain = String(import.meta.env.VITE_MALL_H5_DOMAIN || '').replace(/\/+$/, '')
  return mallH5Domain ? `${mallH5Domain}${localPath}` : localPath
}
