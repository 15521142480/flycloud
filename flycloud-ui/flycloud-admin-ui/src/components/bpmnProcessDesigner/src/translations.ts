import { useI18n } from '@/hooks/web/useI18n'
/**
 * This is a sample file that should be replaced with the actual translation.
 *
 * Checkout https://github.com/bpmn-io/bpmn-js-i18n for a list of available
 * translations and labels to translate.
 */
const { t } = useI18n()

export default {
  'Exclusive Gateway': 'Exklusives Gateway',
  'Parallel Gateway': 'Paralleles Gateway',
  'Inclusive Gateway': 'Inklusives Gateway',
  'Complex Gateway': 'Komplexes Gateway',
  'Event based Gateway': 'Ereignis-basiertes Gateway',
  'Message Start Event': t('auto.components.bpmnProcessDesigner.src.translations.kb6ae12cd'),
  'Timer Start Event': t('auto.components.bpmnProcessDesigner.src.translations.kacd04d84'),
  'Conditional Start Event': t('auto.components.bpmnProcessDesigner.src.translations.k72450c92'),
  'Signal Start Event': t('auto.components.bpmnProcessDesigner.src.translations.k785e1a12'),
  'Error Start Event': t('auto.components.bpmnProcessDesigner.src.translations.k17047b59'),
  'Escalation Start Event': t('auto.components.bpmnProcessDesigner.src.translations.k6e711707'),
  'Compensation Start Event': t('auto.components.bpmnProcessDesigner.src.translations.ka2a6e618'),
  'Message Start Event (non-interrupting)': t(
    'auto.components.bpmnProcessDesigner.src.translations.kaa5c0da3'
  ),
  'Timer Start Event (non-interrupting)': t(
    'auto.components.bpmnProcessDesigner.src.translations.kb7c59e53'
  ),
  'Conditional Start Event (non-interrupting)': t(
    'auto.components.bpmnProcessDesigner.src.translations.k1951ae5f'
  ),
  'Signal Start Event (non-interrupting)': t(
    'auto.components.bpmnProcessDesigner.src.translations.k7009ec7f'
  ),
  'Escalation Start Event (non-interrupting)': t(
    'auto.components.bpmnProcessDesigner.src.translations.k27f80819'
  )
}
