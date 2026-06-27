import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 搜索框属性 */
const { t } = useI18n()

export interface SearchProperty {
  height: number // 搜索栏高度
  showScan: boolean // 显示扫一扫
  borderRadius: number // 框体样式
  placeholder: string // 占位文字
  placeholderPosition: PlaceholderPosition // 占位文字位置
  backgroundColor: string // 框体颜色
  textColor: string // 字体颜色
  hotKeywords: string[] // 热词
  style: ComponentStyle
}

// 文字位置
export type PlaceholderPosition = 'left' | 'center'

// 定义组件
export const component = {
  id: 'SearchBar',
  name: t('auto.components.DiyEditor.components.mobile.SearchBar.config.k44d7587d'),
  icon: 'ep:search',
  property: {
    height: 28,
    showScan: false,
    borderRadius: 0,
    placeholder: t('auto.components.DiyEditor.components.mobile.SearchBar.config.k81447af6'),
    placeholderPosition: 'left',
    backgroundColor: 'rgb(238, 238, 238)',
    textColor: 'rgb(150, 151, 153)',
    hotKeywords: [],
    style: {
      bgType: 'color',
      bgColor: '#fff',
      marginBottom: 8,
      paddingTop: 8,
      paddingRight: 8,
      paddingBottom: 8,
      paddingLeft: 8
    } as ComponentStyle
  }
} as DiyComponent<SearchProperty>
