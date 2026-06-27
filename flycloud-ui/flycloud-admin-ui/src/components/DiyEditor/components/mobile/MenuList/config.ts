import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { cloneDeep } from 'lodash-es'
import { useI18n } from '@/hooks/web/useI18n'
/** 列表导航属性 */
const { t } = useI18n()

export interface MenuListProperty {
  // 导航菜单列表
  list: MenuListItemProperty[]
  // 组件样式
  style: ComponentStyle
}

/** 列表导航项目属性 */
export interface MenuListItemProperty {
  // 图标链接
  iconUrl: string
  // 标题
  title: string
  // 标题颜色
  titleColor: string
  // 副标题
  subtitle: string
  // 副标题颜色
  subtitleColor: string
  // 链接
  url: string
}

export const EMPTY_MENU_LIST_ITEM_PROPERTY = {
  title: t('auto.components.DiyEditor.components.mobile.MenuList.config.k748d7dc7'),
  titleColor: '#333',
  subtitle: t('auto.components.DiyEditor.components.mobile.MenuList.config.k8344831e'),
  subtitleColor: '#bbb'
}

// 定义组件
export const component = {
  id: 'MenuList',
  name: t('auto.components.DiyEditor.components.mobile.MenuList.config.k92d14243'),
  icon: 'fa-solid:list',
  property: {
    list: [cloneDeep(EMPTY_MENU_LIST_ITEM_PROPERTY)],
    style: {
      bgType: 'color',
      bgColor: '#fff',
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<MenuListProperty>
