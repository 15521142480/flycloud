import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 标题栏属性 */
const { t } = useI18n()

export interface TitleBarProperty {
  // 背景图
  bgImgUrl: string
  // 偏移
  marginLeft: number
  // 显示位置
  textAlign: 'left' | 'center'
  // 主标题
  title: string
  // 副标题
  description: string
  // 标题大小
  titleSize: number
  // 描述大小
  descriptionSize: number
  // 标题粗细
  titleWeight: number
  // 描述粗细
  descriptionWeight: number
  // 标题颜色
  titleColor: string
  // 描述颜色
  descriptionColor: string
  // 查看更多
  more: {
    // 是否显示查看更多
    show: false
    // 样式选择
    type: 'text' | 'icon' | 'all'
    // 自定义文字
    text: string
    // 链接
    url: string
  }
  // 组件样式
  style: ComponentStyle
}

// 定义组件
export const component = {
  id: 'TitleBar',
  name: t('auto.components.DiyEditor.components.mobile.TitleBar.config.ke8bef90e'),
  icon: 'material-symbols:line-start',
  property: {
    title: t('auto.components.DiyEditor.components.mobile.TitleBar.config.ke6dc2df4'),
    description: t('auto.components.DiyEditor.components.mobile.TitleBar.config.k8344831e'),
    titleSize: 16,
    descriptionSize: 12,
    titleWeight: 400,
    textAlign: 'left',
    descriptionWeight: 200,
    titleColor: 'rgba(50, 50, 51, 10)',
    descriptionColor: 'rgba(150, 151, 153, 10)',
    more: {
      //查看更多
      show: false,
      type: 'icon',
      text: t('auto.components.DiyEditor.components.mobile.TitleBar.config.k120b8697'),
      url: ''
    },
    style: {
      bgType: 'color',
      bgColor: '#fff'
    } as ComponentStyle
  }
} as DiyComponent<TitleBarProperty>
