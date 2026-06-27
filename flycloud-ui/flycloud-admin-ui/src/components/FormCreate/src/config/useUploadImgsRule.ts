import { generateUUID } from '@/utils'
import { localeProps, makeRequiredRule } from '@/components/FormCreate/src/utils'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const useUploadImgsRule = () => {
  const label = t('auto.components.FormCreate.src.config.useUploadImgsRule.kac4790e7')
  const name = 'UploadImgs'
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
          type: 'switch',
          field: 'drag',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.kb5aa6b9f'),
          value: false
        },
        {
          type: 'select',
          field: 'fileType',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.kf7ccb3bb'),
          value: ['image/jpeg', 'image/png', 'image/gif'],
          options: [
            { label: 'image/apng', value: 'image/apng' },
            { label: 'image/bmp', value: 'image/bmp' },
            { label: 'image/gif', value: 'image/gif' },
            { label: 'image/jpeg', value: 'image/jpeg' },
            { label: 'image/pjpeg', value: 'image/pjpeg' },
            { label: 'image/svg+xml', value: 'image/svg+xml' },
            { label: 'image/tiff', value: 'image/tiff' },
            { label: 'image/webp', value: 'image/webp' },
            { label: 'image/x-icon', value: 'image/x-icon' }
          ],
          props: {
            multiple: true
          }
        },
        {
          type: 'inputNumber',
          field: 'fileSize',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.k55eeefd7'),
          value: 5,
          props: { min: 0 }
        },
        {
          type: 'inputNumber',
          field: 'limit',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.kd79419e2'),
          value: 5,
          props: { min: 0 }
        },
        {
          type: 'input',
          field: 'height',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.kced5a328'),
          value: '150px'
        },
        {
          type: 'input',
          field: 'width',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.k58bab27d'),
          value: '150px'
        },
        {
          type: 'input',
          field: 'borderradius',
          title: t('auto.components.FormCreate.src.config.useUploadImgsRule.k4dde50a6'),
          value: '8px'
        }
      ])
    }
  }
}
