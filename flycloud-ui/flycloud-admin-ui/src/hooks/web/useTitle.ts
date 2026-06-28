import { watch, ref } from 'vue'
import { isString } from '@/utils/is'
import { useI18n } from '@/hooks/web/useI18n'

const { t } = useI18n()

export const useTitle = (newTitle?: string) => {
  const title = ref(newTitle ? `${t('app.title')} - ${t(newTitle as string)}` : t('app.title'))

  watch(
    title,
    (n, o) => {
      if (isString(n) && n !== o && document) {
        document.title = n
      }
    },
    { immediate: true }
  )

  return title
}
