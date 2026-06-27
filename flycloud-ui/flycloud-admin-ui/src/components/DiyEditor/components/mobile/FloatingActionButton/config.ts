import { DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export interface FloatingActionButtonProperty {
  // 展开方向
  direction: 'horizontal' | 'vertical'
  // 是否显示文字
  showText: boolean
  // 按钮列表
  list: FloatingActionButtonItemProperty[]
}

// 悬浮按钮项属性
export interface FloatingActionButtonItemProperty {
  // 图片地址
  imgUrl: string
  // 跳转连接
  url: string
  // 文字
  text: string
  // 文字颜色
  textColor: string
}

// 定义组件
export const component = {
  id: 'FloatingActionButton',
  name: t('auto.components.DiyEditor.components.mobile.FloatingActionButton.config.k7cfd3ab9'),
  icon: 'tabler:float-right',
  position: 'fixed',
  property: {
    direction: 'vertical',
    showText: true,
    list: [{ textColor: '#fff' }]
  }
} as DiyComponent<FloatingActionButtonProperty>
