import * as CouponTemplateApi from '@/api/mall/promotion/coupon/couponTemplate'
import { CouponTemplateValidityTypeEnum, PromotionDiscountTypeEnum } from '@/utils/constants'
import { floatToFixed2 } from '@/utils'
import { formatDate } from '@/utils/formatTime'
import { object } from 'vue-types'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
export const CouponDiscount = defineComponent({
  name: 'CouponDiscount',
  props: {
    coupon: object<CouponTemplateApi.CouponTemplateVO>()
  },
  setup(props) {
    const coupon = props.coupon as CouponTemplateApi.CouponTemplateVO
    // 折扣
    let value = coupon.discountPercent + ''
    let suffix = t('auto.components.DiyEditor.components.mobile.CouponCard.component.k6d7aac9a')
    // 满减
    if (coupon.discountType === PromotionDiscountTypeEnum.PRICE.type) {
      value = floatToFixed2(coupon.discountPrice)
      suffix = t('auto.components.DiyEditor.components.mobile.CouponCard.component.k622f3c5a')
    }
    return () => (
      <div>
        <span class={'text-20px font-bold'}>{value}</span>
        <span>{suffix}</span>
      </div>
    )
  }
})

// 优惠描述
export const CouponDiscountDesc = defineComponent({
  name: 'CouponDiscountDesc',
  props: {
    coupon: object<CouponTemplateApi.CouponTemplateVO>()
  },
  setup(props) {
    const coupon = props.coupon as CouponTemplateApi.CouponTemplateVO
    // 使用条件
    const useCondition =
      coupon.usePrice > 0 ? t('extra.kf2459ff0', { p0: floatToFixed2(coupon.usePrice) }) : ''
    // 优惠描述
    const discountDesc =
      coupon.discountType === PromotionDiscountTypeEnum.PRICE.type
        ? t('extra.k26de6109', { p0: floatToFixed2(coupon.discountPrice) })
        : t('extra.kc1fc08bc', { p0: coupon.discountPercent })
    return () => (
      <div>
        <span>{useCondition}</span>
        <span>{discountDesc}</span>
      </div>
    )
  }
})

// 有效期
export const CouponValidTerm = defineComponent({
  name: 'CouponValidTerm',
  props: {
    coupon: object<CouponTemplateApi.CouponTemplateVO>()
  },
  setup(props) {
    const coupon = props.coupon as CouponTemplateApi.CouponTemplateVO
    const text =
      coupon.validityType === CouponTemplateValidityTypeEnum.DATE.type
        ? t('extra.ked3e3ad4', {
            p0: formatDate(coupon.validStartTime, 'YYYY-MM-DD'),
            p1: formatDate(coupon.validEndTime, 'YYYY-MM-DD')
          })
        : t('extra.kc163fd68', { p0: coupon.fixedStartTerm, p1: coupon.fixedEndTerm })
    return () => <div>{text}</div>
  }
})
