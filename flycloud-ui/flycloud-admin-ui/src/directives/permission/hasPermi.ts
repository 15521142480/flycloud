import type { App } from 'vue'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

const { t } = useI18n() // 国际化

export function hasPermi(app: App<Element>) {

  app.directive('hasPermi', (el, binding) => {

    const { wsCache } = useCache()
    const { value } = binding
    const all_permission = '*:*:*'
    const permissionList = wsCache.get(CACHE_KEY.USER).permissionList

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = permissionList.some((permission: string) => {
        return all_permission === permission || permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(t('permission.hasPermission'))
    }

    return true
  })
}
