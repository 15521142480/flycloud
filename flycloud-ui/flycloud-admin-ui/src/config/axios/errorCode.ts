import { useI18n } from '@/hooks/web/useI18n'

export const getErrorMessage = (code?: number | string) => {
  const { t } = useI18n()

  const errorCode: Record<string, string> = {
    401: t('auto.config.axios.errorCode.k9634da8a'),
    403: t('auto.config.axios.errorCode.kf71e0bd7'),
    404: t('auto.config.axios.errorCode.k3c7cac81'),
    default: t('auto.config.axios.errorCode.kf188fb0e')
  }

  return errorCode[String(code)] || errorCode.default
}
