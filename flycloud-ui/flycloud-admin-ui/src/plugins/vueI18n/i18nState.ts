import type { createI18n } from 'vue-i18n'

export let i18n: ReturnType<typeof createI18n> | undefined

export const setI18nInstance = (instance: ReturnType<typeof createI18n>) => {
  i18n = instance
}
