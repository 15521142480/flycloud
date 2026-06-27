import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 图片展示属性 */
const { t } = useI18n()

export interface ImageBarProperty {
  // 图片链接
  imgUrl: string
  // 跳转链接
  url: string
  // 组件样式
  style: ComponentStyle
}

// 定义组件
export const component = {
  id: 'ImageBar',
  name: t('auto.components.DiyEditor.components.mobile.ImageBar.config.k8e143818'),
  icon: 'ep:picture',
  property: {
    imgUrl: '',
    url: '',
    style: {
      bgType: 'color',
      bgColor: '#fff',
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<ImageBarProperty>
