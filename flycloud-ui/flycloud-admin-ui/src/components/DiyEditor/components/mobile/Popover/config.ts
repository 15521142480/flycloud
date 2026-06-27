import { DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 弹窗广告属性 */
const { t } = useI18n()

export interface PopoverProperty {
  list: PopoverItemProperty[]
}

export interface PopoverItemProperty {
  // 图片地址
  imgUrl: string
  // 跳转连接
  url: string
  // 显示类型：仅显示一次、每次启动都会显示
  showType: 'once' | 'always'
}

// 定义组件
export const component = {
  id: 'Popover',
  name: t('auto.components.DiyEditor.components.mobile.Popover.config.kefdea874'),
  icon: 'carbon:popup',
  position: 'fixed',
  property: {
    list: [{ showType: 'once' }]
  }
} as DiyComponent<PopoverProperty>
