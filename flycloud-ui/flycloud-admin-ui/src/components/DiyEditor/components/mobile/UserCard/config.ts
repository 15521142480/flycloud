import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 用户卡片属性 */
const { t } = useI18n()

export interface UserCardProperty {
  // 组件样式
  style: ComponentStyle
}

// 定义组件
export const component = {
  id: 'UserCard',
  name: t('auto.components.DiyEditor.components.mobile.UserCard.config.kf10483d2'),
  icon: 'mdi:user-card-details',
  property: {
    style: {
      bgType: 'color',
      bgColor: '',
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<UserCardProperty>
