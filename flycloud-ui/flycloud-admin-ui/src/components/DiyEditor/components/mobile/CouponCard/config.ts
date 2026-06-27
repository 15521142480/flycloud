import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 商品卡片属性 */
const { t } = useI18n()

export interface CouponCardProperty {
  // 列数
  columns: number
  // 背景图
  bgImg: string
  // 文字颜色
  textColor: string
  // 按钮样式
  button: {
    // 颜色
    color: string
    // 背景颜色
    bgColor: string
  }
  // 间距
  space: number
  // 优惠券编号列表
  couponIds: number[]
  // 组件样式
  style: ComponentStyle
}

// 定义组件
export const component = {
  id: 'CouponCard',
  name: t('auto.components.DiyEditor.components.mobile.CouponCard.config.k30f810e7'),
  icon: 'ep:ticket',
  property: {
    columns: 1,
    bgImg: '',
    textColor: '#E9B461',
    button: {
      color: '#434343',
      bgColor: ''
    },
    space: 0,
    couponIds: [],
    style: {
      bgType: 'color',
      bgColor: '',
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<CouponCardProperty>
