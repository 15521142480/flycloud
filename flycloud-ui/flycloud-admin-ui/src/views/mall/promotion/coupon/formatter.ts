import { CouponTemplateValidityTypeEnum, PromotionDiscountTypeEnum } from '@/utils/constants'
import { formatDate } from '@/utils/formatTime'
import { CouponTemplateVO } from '@/api/mall/promotion/coupon/couponTemplate'
import { floatToFixed2 } from '@/utils'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
export const discountFormat = (row: CouponTemplateVO) => {
  if (row.discountType === PromotionDiscountTypeEnum.PRICE.type) {
    return `￥${floatToFixed2(row.discountPrice)}`
  }
  if (row.discountType === PromotionDiscountTypeEnum.PERCENT.type) {
    return `${row.discountPercent}%`
  }
  return t('auto.views.mall.promotion.coupon.formatter.kb583b1a0') + row.discountType + '】'
}

// 格式化【领取上限】
export const takeLimitCountFormat = (row: CouponTemplateVO) => {
  if (row.takeLimitCount) {
    if (row.takeLimitCount === -1) {
      return t('auto.views.mall.promotion.coupon.formatter.k0b063143')
    }
    return t('extra.kb3fb5cb1', { p0: row.takeLimitCount })
  } else {
    return ' '
  }
}

// 格式化【有效期限】
export const validityTypeFormat = (row: CouponTemplateVO) => {
  if (row.validityType === CouponTemplateValidityTypeEnum.DATE.type) {
    return t('extra.ka28cc5cb', {
      p0: formatDate(row.validStartTime),
      p1: formatDate(row.validEndTime)
    })
  }
  if (row.validityType === CouponTemplateValidityTypeEnum.TERM.type) {
    return t('extra.k29762443', { p0: row.fixedStartTerm, p1: row.fixedEndTerm })
  }
  return t('auto.views.mall.promotion.coupon.formatter.kb583b1a0') + row.validityType + '】'
}

// 格式化【totalCount】
export const totalCountFormat = (row: CouponTemplateVO) => {
  if (row.totalCount === -1) {
    return t('auto.views.mall.promotion.coupon.formatter.k204f25ff')
  }
  return row.totalCount
}

// 格式化【剩余数量】
export const remainedCountFormat = (row: CouponTemplateVO) => {
  if (row.totalCount === -1) {
    return t('auto.views.mall.promotion.coupon.formatter.k204f25ff')
  }
  return row.totalCount - row.takeCount
}

// 格式化【最低消费】
export const usePriceFormat = (row: CouponTemplateVO) => {
  return `￥${floatToFixed2(row.usePrice)}`
}
