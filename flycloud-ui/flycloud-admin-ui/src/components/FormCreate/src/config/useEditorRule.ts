import { generateUUID } from '@/utils'
import { localeProps, makeRequiredRule } from '@/components/FormCreate/src/utils'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const useEditorRule = () => {
  const label = t('auto.components.FormCreate.src.config.useEditorRule.k8368f027')
  const name = 'Editor'
  return {
    icon: 'icon-editor',
    label,
    name,
    rule() {
      return {
        type: name,
        field: generateUUID(),
        title: label,
        info: '',
        $required: false
      }
    },
    props(_, { t }) {
      return localeProps(t, name + '.props', [
        makeRequiredRule(),
        {
          type: 'input',
          field: 'height',
          title: t('auto.components.FormCreate.src.config.useEditorRule.keea51aa4')
        },
        {
          type: 'switch',
          field: 'readonly',
          title: t('auto.components.FormCreate.src.config.useEditorRule.k7fc66b85')
        }
      ])
    }
  }
}
