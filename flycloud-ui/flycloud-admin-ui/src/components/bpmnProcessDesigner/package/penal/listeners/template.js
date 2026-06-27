import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
export const template = (isTaskListener) => {
  return t('extra.ka4b49192', {
    p0: isTaskListener
      ? t('auto.components.bpmnProcessDesigner.package.penal.listeners.template.k521cc099') +
        "<el-select v-model='listenerForm.eventDefinitionType'>" +
        t('auto.components.bpmnProcessDesigner.package.penal.listeners.template.k598f2636') +
        t('auto.components.bpmnProcessDesigner.package.penal.listeners.template.ke5429061') +
        t('auto.components.bpmnProcessDesigner.package.penal.listeners.template.k99e24a16') +
        t('auto.components.bpmnProcessDesigner.package.penal.listeners.template.kd16d8f4f') +
        '</el-select>' +
        '</el-form-item>' +
        t('auto.components.bpmnProcessDesigner.package.penal.listeners.template.k152ccb11') +
        "<el-input v-model='listenerForm.eventDefinitions' clearable />" +
        '</el-form-item>'
      : ''
  })
}
