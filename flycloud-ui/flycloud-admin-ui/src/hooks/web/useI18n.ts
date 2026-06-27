import { i18n } from '@/plugins/vueI18n/i18nState'

type I18nGlobalTranslation = {
  (key: string): string
  (key: string, locale: string): string
  (key: string, locale: string, list: unknown[]): string
  (key: string, locale: string, named: Record<string, unknown>): string
  (key: string, list: unknown[]): string
  (key: string, named: Record<string, unknown>): string
}

type I18nTranslationRestParameters = [string, any]

const getKey = (namespace: string | undefined, key: string) => {
  if (!namespace) {
    return key
  }
  if (key.startsWith(namespace)) {
    return key
  }
  return `${namespace}.${key}`
}

export const useI18n = (
  namespace?: string
): {
  t: I18nGlobalTranslation
} => {
  const tFn: I18nGlobalTranslation = (key: string, ...arg: any[]) => {
    if (!key) return ''
    if (!i18n) return getKey(namespace, key)
    if (!key.includes('.') && !namespace) return key
    const { t } = i18n.global
    //@ts-ignore
    return t(getKey(namespace, key), ...(arg as I18nTranslationRestParameters))
  }

  const methods = i18n ? i18n.global : {}

  return {
    ...methods,
    t: tFn
  }
}

export const t = (key: string) => key
