import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 用户订单属性 */
const { t } = useI18n()

export interface UserOrderProperty {
  // 组件样式
  style: ComponentStyle
}

// 定义组件
export const component = {
  id: 'UserOrder',
  name: t('auto.components.DiyEditor.components.mobile.UserOrder.config.ka332d0e6'),
  icon: 'ep:list',
  property: {
    style: {
      bgType: 'color',
      bgColor: '',
      marginLeft: 8,
      marginRight: 8,
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<UserOrderProperty>
