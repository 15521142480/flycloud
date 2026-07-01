import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export default {
  401: t('auto.config.axios.errorCode.k9634da8a'),
  403: t('auto.config.axios.errorCode.kf71e0bd7'),
  404: t('auto.config.axios.errorCode.k3c7cac81'),
  default: t('auto.config.axios.errorCode.kf188fb0e')
}
