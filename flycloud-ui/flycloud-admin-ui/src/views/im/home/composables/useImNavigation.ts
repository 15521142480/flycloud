import { computed, inject, provide, type ComputedRef, type InjectionKey } from 'vue'
import { useRoute, useRouter } from 'vue-router'

/** IM 主区域可切换的页面。 */
export type ImNavigationPage = 'conversation' | 'contact'

/** IM 内部导航能力：全屏版切路由，菜单内嵌版切当前组件状态。 */
export interface ImNavigation {
  activePage: ComputedRef<ImNavigationPage>
  goConversation: () => void
  goContact: () => void
}

/** IM 导航上下文标识，供工具栏、通讯录及全局信息卡复用。 */
const IM_NAVIGATION_KEY: InjectionKey<ImNavigation> = Symbol('IM_NAVIGATION_KEY')

interface ImEmbeddedNavigationOptions {
  embedded: true
  activePage: ComputedRef<ImNavigationPage>
  changePage: (page: ImNavigationPage) => void
}

interface ImRouteNavigationOptions {
  embedded: false
}

type ImNavigationOptions = ImEmbeddedNavigationOptions | ImRouteNavigationOptions

/**
 * 在 IM 主壳中提供导航能力。
 *
 * 全屏模式沿用原有子路由；内嵌模式只通知外层切换页面，保证后台菜单地址和标签页不变化。
 */
export function provideImNavigation(options: ImNavigationOptions) {
  const route = useRoute()
  const router = useRouter()
  const activePage = computed<ImNavigationPage>(() => {
    if (options.embedded) {
      return options.activePage.value
    }
    return route.name === 'ImHomeContact' ? 'contact' : 'conversation'
  })

  const goPage = (page: ImNavigationPage) => {
    if (options.embedded) {
      options.changePage(page)
      return
    }
    const routeName = page === 'conversation' ? 'ImHomeConversation' : 'ImHomeContact'
    if (route.name !== routeName) {
      void router.push({ name: routeName })
    }
  }

  const navigation: ImNavigation = {
    activePage,
    goConversation: () => goPage('conversation'),
    goContact: () => goPage('contact')
  }
  provide(IM_NAVIGATION_KEY, navigation)
}

/**
 * 获取 IM 内部导航能力。
 *
 * 正常由 IM 主壳提供；保留路由回退，方便独立挂载子组件时仍能正常跳转。
 */
export function useImNavigation(): ImNavigation {
  const navigation = inject(IM_NAVIGATION_KEY)
  if (navigation) {
    return navigation
  }

  const route = useRoute()
  const router = useRouter()
  const activePage = computed<ImNavigationPage>(() =>
    route.name === 'ImHomeContact' ? 'contact' : 'conversation'
  )
  return {
    activePage,
    goConversation: () => {
      if (route.name !== 'ImHomeConversation') {
        void router.push({ name: 'ImHomeConversation' })
      }
    },
    goContact: () => {
      if (route.name !== 'ImHomeContact') {
        void router.push({ name: 'ImHomeContact' })
      }
    }
  }
}
