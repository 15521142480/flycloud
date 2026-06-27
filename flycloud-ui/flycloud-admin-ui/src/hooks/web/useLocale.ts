import { i18n } from '@/plugins/vueI18n/i18nState'
import { useLocaleStoreWithOut } from '@/store/modules/locale'
import { setHtmlPageLang } from '@/plugins/vueI18n/helper'

const setI18nLanguage = (locale: LocaleType) => {
  const localeStore = useLocaleStoreWithOut()

  if (!i18n) return

  if (i18n.mode === 'legacy') {
    i18n.global.locale = locale
  } else {
    ;(i18n.global.locale as any).value = locale
  }
  localeStore.setCurrentLocale({
    lang: locale
  })
  setHtmlPageLang(locale)
}

export const useLocale = () => {
  // Switching the language will change the locale of useI18n
  // And submit to configuration modification
  const changeLocale = async (locale: LocaleType) => {
    if (!i18n) return
    const globalI18n = i18n.global

    const langModule = await import(`../../locales/${locale}.ts`)
    const autoModule = await import(`../../locales/auto/${locale}.ts`)
    const extraModule = await import(`../../locales/extra/${locale}.ts`)

    globalI18n.setLocaleMessage(locale, {
      auto: autoModule.default ?? {},
      extra: extraModule.default ?? {},
      ...(langModule.default ?? {})
    })

    setI18nLanguage(locale)
  }

  return {
    changeLocale
  }
}
