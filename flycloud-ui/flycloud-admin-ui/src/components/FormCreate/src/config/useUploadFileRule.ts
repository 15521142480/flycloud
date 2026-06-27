import { generateUUID } from '@/utils'
import { localeProps, makeRequiredRule } from '@/components/FormCreate/src/utils'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const useUploadFileRule = () => {
  const label = t('auto.components.FormCreate.src.config.useUploadFileRule.k39c04b3d')
  const name = 'UploadFile'
  return {
    icon: 'icon-upload',
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
          type: 'select',
          field: 'fileType',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.ka5998b46'),
          value: ['doc', 'xls', 'ppt', 'txt', 'pdf'],
          options: [
            { label: 'doc', value: 'doc' },
            { label: 'xls', value: 'xls' },
            { label: 'ppt', value: 'ppt' },
            { label: 'txt', value: 'txt' },
            { label: 'pdf', value: 'pdf' }
          ],
          props: {
            multiple: true
          }
        },
        {
          type: 'switch',
          field: 'autoUpload',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.k82c7234f'),
          value: true
        },
        {
          type: 'switch',
          field: 'drag',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.kb5aa6b9f'),
          value: false
        },
        {
          type: 'switch',
          field: 'isShowTip',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.k6cf5213b'),
          value: true
        },
        {
          type: 'inputNumber',
          field: 'fileSize',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.k55eeefd7'),
          value: 5,
          props: { min: 0 }
        },
        {
          type: 'inputNumber',
          field: 'limit',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.kd79419e2'),
          value: 5,
          props: { min: 0 }
        },
        {
          type: 'switch',
          field: 'disabled',
          title: t('auto.components.FormCreate.src.config.useUploadFileRule.k9858a9a3'),
          value: false
        }
      ])
    }
  }
}
