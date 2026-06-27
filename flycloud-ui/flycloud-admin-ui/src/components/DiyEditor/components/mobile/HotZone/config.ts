import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 热区属性 */
const { t } = useI18n()

export interface HotZoneProperty {
  // 图片地址
  imgUrl: string
  // 导航菜单列表
  list: HotZoneItemProperty[]
  // 组件样式
  style: ComponentStyle
}

/** 热区项目属性 */
export interface HotZoneItemProperty {
  // 链接的名称
  name: string
  // 链接
  url: string
  // 宽
  width: number
  // 高
  height: number
  // 上
  top: number
  // 左
  left: number
}

// 定义组件
export const component = {
  id: 'HotZone',
  name: t('auto.components.DiyEditor.components.mobile.HotZone.config.k5edbb2d3'),
  icon: 'tabler:hand-click',
  property: {
    imgUrl: '',
    list: [] as HotZoneItemProperty[],
    style: {
      bgType: 'color',
      bgColor: '#fff',
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<HotZoneProperty>
