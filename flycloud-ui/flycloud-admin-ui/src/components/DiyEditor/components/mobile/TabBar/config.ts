import { DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 底部导航菜单属性 */
const { t } = useI18n()

export interface TabBarProperty {
  // 选项列表
  items: TabBarItemProperty[]
  // 主题
  theme: string
  // 样式
  style: TabBarStyle
}

// 选项属性
export interface TabBarItemProperty {
  // 标签文字
  text: string
  // 链接
  url: string
  // 默认图标链接
  iconUrl: string
  // 选中的图标链接
  activeIconUrl: string
}

// 样式
export interface TabBarStyle {
  // 背景类型
  bgType: 'color' | 'img'
  // 背景颜色
  bgColor: string
  // 图片链接
  bgImg: string
  // 默认颜色
  color: string
  // 选中的颜色
  activeColor: string
}

// 定义组件
export const component = {
  id: 'TabBar',
  name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k0aa6b303'),
  icon: 'fluent:table-bottom-row-16-filled',
  property: {
    theme: 'red',
    style: {
      bgType: 'color',
      bgColor: '#fff',
      color: '#282828',
      activeColor: '#fc4141'
    },
    items: [
      {
        text: t('auto.components.DiyEditor.components.mobile.TabBar.config.kff93ad0e'),
        url: '/pages/index/index',
        iconUrl: 'http://mall.yudao.iocoder.cn/static/images/1-001.png',
        activeIconUrl: 'http://mall.yudao.iocoder.cn/static/images/1-002.png'
      },
      {
        text: t('auto.components.DiyEditor.components.mobile.TabBar.config.k435c5259'),
        url: '/pages/index/category/3',
        iconUrl: 'http://mall.yudao.iocoder.cn/static/images/2-001.png',
        activeIconUrl: 'http://mall.yudao.iocoder.cn/static/images/2-002.png'
      },
      {
        text: t('auto.components.DiyEditor.components.mobile.TabBar.config.k5c81c6bc'),
        url: '/pages/index/cart',
        iconUrl: 'http://mall.yudao.iocoder.cn/static/images/3-001.png',
        activeIconUrl: 'http://mall.yudao.iocoder.cn/static/images/3-002.png'
      },
      {
        text: t('auto.components.DiyEditor.components.mobile.TabBar.config.ka82c993d'),
        url: '/pages/index/user',
        iconUrl: 'http://mall.yudao.iocoder.cn/static/images/4-001.png',
        activeIconUrl: 'http://mall.yudao.iocoder.cn/static/images/4-002.png'
      }
    ]
  }
} as DiyComponent<TabBarProperty>

export const THEME_LIST = [
  {
    id: 'red',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.kd4c3e47d'),
    icon: 'icon-park-twotone:theme',
    color: '#d10019'
  },
  {
    id: 'orange',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k1df98b50'),
    icon: 'icon-park-twotone:theme',
    color: '#f37b1d'
  },
  {
    id: 'gold',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k981dab6a'),
    icon: 'icon-park-twotone:theme',
    color: '#fbbd08'
  },
  {
    id: 'green',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k5387bb6b'),
    icon: 'icon-park-twotone:theme',
    color: '#8dc63f'
  },
  {
    id: 'cyan',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k6d93103c'),
    icon: 'icon-park-twotone:theme',
    color: '#1cbbb4'
  },
  {
    id: 'blue',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.kc7b80367'),
    icon: 'icon-park-twotone:theme',
    color: '#0081ff'
  },
  {
    id: 'purple',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k02c6a531'),
    icon: 'icon-park-twotone:theme',
    color: '#6739b6'
  },
  {
    id: 'brightRed',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k8ad0cbd6'),
    icon: 'icon-park-twotone:theme',
    color: '#e54d42'
  },
  {
    id: 'forestGreen',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k7de831c7'),
    icon: 'icon-park-twotone:theme',
    color: '#39b54a'
  },
  {
    id: 'mauve',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.ka979c1d9'),
    icon: 'icon-park-twotone:theme',
    color: '#9c26b0'
  },
  {
    id: 'pink',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k5dd930ab'),
    icon: 'icon-park-twotone:theme',
    color: '#e03997'
  },
  {
    id: 'brown',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.kf9e309af'),
    icon: 'icon-park-twotone:theme',
    color: '#a5673f'
  },
  {
    id: 'grey',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k2375439d'),
    icon: 'icon-park-twotone:theme',
    color: '#8799a3'
  },
  {
    id: 'gray',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k75a7cfbf'),
    icon: 'icon-park-twotone:theme',
    color: '#aaaaaa'
  },
  {
    id: 'black',
    name: t('auto.components.DiyEditor.components.mobile.TabBar.config.k727c92f2'),
    icon: 'icon-park-twotone:theme',
    color: '#333333'
  }
]
