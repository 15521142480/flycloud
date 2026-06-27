import { useTimeAgo as useTimeAgoCore, UseTimeAgoMessages } from '@vueuse/core'
import { useLocaleStoreWithOut } from '@/store/modules/locale'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
const TIME_AGO_MESSAGE_MAP: {
  'zh-CN': UseTimeAgoMessages
  en: UseTimeAgoMessages
} = {
  // @ts-ignore
  'zh-CN': {
    justNow: t('auto.hooks.web.useTimeAgo.k9e636642'),
    past: (n) => (n.match(/\d/) ? t('extra.k56dc3ba9', { p0: n }) : n),
    future: (n) => (n.match(/\d/) ? t('extra.k9cc11dd7', { p0: n }) : n),
    month: (n, past) =>
      n === 1
        ? past
          ? t('auto.hooks.web.useTimeAgo.k69bb1687')
          : t('auto.hooks.web.useTimeAgo.k8c9b38e2')
        : t('extra.k7f3d30c6', { p0: n }),
    year: (n, past) =>
      n === 1
        ? past
          ? t('auto.hooks.web.useTimeAgo.k1fd586b4')
          : t('auto.hooks.web.useTimeAgo.kc78156bb')
        : t('extra.k8a4dcd89', { p0: n }),
    day: (n, past) =>
      n === 1
        ? past
          ? t('auto.hooks.web.useTimeAgo.k59c4fcb0')
          : t('auto.hooks.web.useTimeAgo.kb76ce230')
        : t('extra.k725e526e', { p0: n }),
    week: (n, past) =>
      n === 1
        ? past
          ? t('auto.hooks.web.useTimeAgo.k39678c8d')
          : t('auto.hooks.web.useTimeAgo.k1b029a74')
        : t('extra.kcdf09bc8', { p0: n }),
    hour: (n) => t('extra.k16c7e652', { p0: n }),
    minute: (n) => t('extra.k6e42750b', { p0: n }),
    second: (n) => t('extra.k8cffb0cd', { p0: n })
  },
  // @ts-ignore
  en: {
    justNow: 'just now',
    past: (n) => (n.match(/\d/) ? `${n} ago` : n),
    future: (n) => (n.match(/\d/) ? `in ${n}` : n),
    month: (n, past) =>
      n === 1 ? (past ? 'last month' : 'next month') : `${n} month${n > 1 ? 's' : ''}`,
    year: (n, past) =>
      n === 1 ? (past ? 'last year' : 'next year') : `${n} year${n > 1 ? 's' : ''}`,
    day: (n, past) => (n === 1 ? (past ? 'yesterday' : 'tomorrow') : `${n} day${n > 1 ? 's' : ''}`),
    week: (n, past) =>
      n === 1 ? (past ? 'last week' : 'next week') : `${n} week${n > 1 ? 's' : ''}`,
    hour: (n) => `${n} hour${n > 1 ? 's' : ''}`,
    minute: (n) => `${n} minute${n > 1 ? 's' : ''}`,
    second: (n) => `${n} second${n > 1 ? 's' : ''}`
  }
}

export const useTimeAgo = (time: Date | number | string) => {
  const localeStore = useLocaleStoreWithOut()

  const currentLocale = computed(() => localeStore.getCurrentLocale)

  const timeAgo = useTimeAgoCore(time, {
    messages: TIME_AGO_MESSAGE_MAP[unref(currentLocale).lang]
  })

  return timeAgo
}
