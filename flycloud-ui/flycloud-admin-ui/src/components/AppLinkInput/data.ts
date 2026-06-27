import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export interface AppLinkGroup {
  // 分组名称
  name: string
  // 链接列表
  links: AppLink[]
}

// APP 链接
export interface AppLink {
  // 链接名称
  name: string
  // 链接地址
  path: string
  // 链接的类型
  type?: APP_LINK_TYPE_ENUM
}

// APP 链接类型（需要特殊处理，例如商品详情）
export const enum APP_LINK_TYPE_ENUM {
  // 拼团活动
  ACTIVITY_COMBINATION,
  // 秒杀活动
  ACTIVITY_SECKILL,
  // 积分商城活动
  ACTIVITY_POINT,
  // 文章详情
  ARTICLE_DETAIL,
  // 优惠券详情
  COUPON_DETAIL,
  // 自定义页面详情
  DIY_PAGE_DETAIL,
  // 品类列表
  PRODUCT_CATEGORY_LIST,
  // 商品列表
  PRODUCT_LIST,
  // 商品详情
  PRODUCT_DETAIL_NORMAL,
  // 拼团商品详情
  PRODUCT_DETAIL_COMBINATION,
  // 秒杀商品详情
  PRODUCT_DETAIL_SECKILL
}

// APP 链接列表（做一下持久化？）
export const APP_LINK_GROUP_LIST = [
  {
    name: t('auto.components.AppLinkInput.data.kc0745f30'),
    links: [
      {
        name: t('auto.components.AppLinkInput.data.kff93ad0e'),
        path: '/pages/index/index'
      },
      {
        name: t('auto.components.AppLinkInput.data.k09482df6'),
        path: '/pages/index/category',
        type: APP_LINK_TYPE_ENUM.PRODUCT_CATEGORY_LIST
      },
      {
        name: t('auto.components.AppLinkInput.data.k5c81c6bc'),
        path: '/pages/index/cart'
      },
      {
        name: t('auto.components.AppLinkInput.data.k1d23b8a4'),
        path: '/pages/index/user'
      },
      {
        name: t('auto.components.AppLinkInput.data.k7a933923'),
        path: '/pages/index/search'
      },
      {
        name: t('auto.components.AppLinkInput.data.k245b9602'),
        path: '/pages/index/page',
        type: APP_LINK_TYPE_ENUM.DIY_PAGE_DETAIL
      },
      {
        name: t('auto.components.AppLinkInput.data.kb8649fa6'),
        path: '/pages/chat/index'
      },
      {
        name: t('auto.components.AppLinkInput.data.ke976eab5'),
        path: '/pages/public/setting'
      },
      {
        name: t('auto.components.AppLinkInput.data.k0adb1147'),
        path: '/pages/public/faq'
      }
    ]
  },
  {
    name: t('auto.components.AppLinkInput.data.k00492206'),
    links: [
      {
        name: t('auto.components.AppLinkInput.data.k44544d21'),
        path: '/pages/goods/list',
        type: APP_LINK_TYPE_ENUM.PRODUCT_LIST
      },
      {
        name: t('auto.components.AppLinkInput.data.kad98c017'),
        path: '/pages/goods/index',
        type: APP_LINK_TYPE_ENUM.PRODUCT_DETAIL_NORMAL
      },
      {
        name: t('auto.components.AppLinkInput.data.k12501a47'),
        path: '/pages/goods/groupon',
        type: APP_LINK_TYPE_ENUM.PRODUCT_DETAIL_COMBINATION
      },
      {
        name: t('auto.components.AppLinkInput.data.kaf31b1bd'),
        path: '/pages/goods/seckill',
        type: APP_LINK_TYPE_ENUM.PRODUCT_DETAIL_SECKILL
      }
    ]
  },
  {
    name: t('auto.components.AppLinkInput.data.kb61413c3'),
    links: [
      {
        name: t('auto.components.AppLinkInput.data.kab704166'),
        path: '/pages/activity/groupon/order'
      },
      {
        name: t('auto.components.AppLinkInput.data.kaca75406'),
        path: '/pages/activity/index'
      },
      {
        name: t('auto.components.AppLinkInput.data.kf68ed13e'),
        path: '/pages/activity/groupon/list',
        type: APP_LINK_TYPE_ENUM.ACTIVITY_COMBINATION
      },
      {
        name: t('auto.components.AppLinkInput.data.ka7605a08'),
        path: '/pages/activity/seckill/list',
        type: APP_LINK_TYPE_ENUM.ACTIVITY_SECKILL
      },
      {
        name: t('auto.components.AppLinkInput.data.k929ca2dd'),
        path: '/pages/activity/point/list',
        type: APP_LINK_TYPE_ENUM.ACTIVITY_POINT
      },
      {
        name: t('auto.components.AppLinkInput.data.k2ff5eba2'),
        path: '/pages/app/sign'
      },
      {
        name: t('auto.components.AppLinkInput.data.k388dcfbc'),
        path: '/pages/coupon/list'
      },
      {
        name: t('auto.components.AppLinkInput.data.k6710a9f8'),
        path: '/pages/coupon/detail',
        type: APP_LINK_TYPE_ENUM.COUPON_DETAIL
      },
      {
        name: t('auto.components.AppLinkInput.data.k1fae1943'),
        path: '/pages/public/richtext',
        type: APP_LINK_TYPE_ENUM.ARTICLE_DETAIL
      }
    ]
  },
  {
    name: t('auto.components.AppLinkInput.data.k4b96bab5'),
    links: [
      {
        name: t('auto.components.AppLinkInput.data.k1c26146a'),
        path: '/pages/commission/index'
      },
      {
        name: t('auto.components.AppLinkInput.data.kb605cb8d'),
        path: '/pages/commission/goods'
      },
      {
        name: t('auto.components.AppLinkInput.data.k88ee5014'),
        path: '/pages/commission/order'
      },
      {
        name: t('auto.components.AppLinkInput.data.ka321e3cc'),
        path: '/pages/commission/team'
      }
    ]
  },
  {
    name: t('auto.components.AppLinkInput.data.k9a2dba3c'),
    links: [
      {
        name: t('auto.components.AppLinkInput.data.kb237b1d2'),
        path: '/pages/pay/recharge'
      },
      {
        name: t('auto.components.AppLinkInput.data.ka497a2ca'),
        path: '/pages/pay/recharge-log'
      }
    ]
  },
  {
    name: t('auto.components.AppLinkInput.data.k37a7a8fb'),
    links: [
      {
        name: t('auto.components.AppLinkInput.data.k55c26aba'),
        path: '/pages/user/info'
      },
      {
        name: t('auto.components.AppLinkInput.data.ka332d0e6'),
        path: '/pages/order/list'
      },
      {
        name: t('auto.components.AppLinkInput.data.kc7ed8519'),
        path: '/pages/order/aftersale/list'
      },
      {
        name: t('auto.components.AppLinkInput.data.k7e59637e'),
        path: '/pages/user/goods-collect'
      },
      {
        name: t('auto.components.AppLinkInput.data.kf69150fb'),
        path: '/pages/user/goods-log'
      },
      {
        name: t('auto.components.AppLinkInput.data.k045c2d92'),
        path: '/pages/user/address/list'
      },
      {
        name: t('auto.components.AppLinkInput.data.k9250afab'),
        path: '/pages/user/wallet/commission'
      },
      {
        name: t('auto.components.AppLinkInput.data.k10ee39ca'),
        path: '/pages/user/wallet/money'
      },
      {
        name: t('auto.components.AppLinkInput.data.k9898a752'),
        path: '/pages/user/wallet/score'
      }
    ]
  }
] as AppLinkGroup[]
