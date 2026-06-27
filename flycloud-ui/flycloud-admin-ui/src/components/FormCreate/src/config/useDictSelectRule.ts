import { generateUUID } from '@/utils'
import * as DictDataApi from '@/api/system/dict/dict.type'
import { localeProps, makeRequiredRule } from '@/components/FormCreate/src/utils'
import { selectRule } from '@/components/FormCreate/src/config/selectRule'
import { cloneDeep } from 'lodash-es'
import { useI18n } from '@/hooks/web/useI18n'
/**
 * 字典选择器规则，如果规则使用到动态数据则需要单独配置不能使用 useSelectRule
 */
const { t } = useI18n()

export const useDictSelectRule = () => {
  const label = t('auto.components.FormCreate.src.config.useDictSelectRule.k091e85ed')
  const name = 'DictSelect'
  const rules = cloneDeep(selectRule)
  const dictOptions = ref<{ label: string; value: string }[]>([]) // 字典类型下拉数据
  onMounted(async () => {
    const data = await DictDataApi.getSimpleDictTypeList()
    if (!data || data.length === 0) {
      return
    }
    dictOptions.value =
      data?.map((item: DictDataApi.DictTypeVO) => ({
        label: item.name,
        value: item.type
      })) ?? []
  })
  return {
    icon: 'icon-doc-text',
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
          field: 'dictType',
          title: t('auto.components.FormCreate.src.config.useDictSelectRule.k6dde52f3'),
          value: '',
          options: dictOptions.value
        },
        {
          type: 'select',
          field: 'valueType',
          title: t('auto.components.FormCreate.src.config.useDictSelectRule.ke6d8c68f'),
          value: 'str',
          options: [
            {
              label: t('auto.components.FormCreate.src.config.useDictSelectRule.k7a4dc825'),
              value: 'int'
            },
            {
              label: t('auto.components.FormCreate.src.config.useDictSelectRule.k4dc9621a'),
              value: 'str'
            },
            {
              label: t('auto.components.FormCreate.src.config.useDictSelectRule.k642a2c8a'),
              value: 'bool'
            }
          ]
        },
        ...rules
      ])
    }
  }
}
