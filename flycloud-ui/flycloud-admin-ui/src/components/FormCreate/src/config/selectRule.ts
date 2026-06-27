import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
const selectRule = [
  {
    type: 'select',
    field: 'selectType',
    title: t('auto.components.FormCreate.src.config.selectRule.kb83a459d'),
    value: 'select',
    options: [
      { label: t('auto.components.FormCreate.src.config.selectRule.k2c2c1ef2'), value: 'select' },
      { label: t('auto.components.FormCreate.src.config.selectRule.k95f15ec4'), value: 'radio' },
      { label: t('auto.components.FormCreate.src.config.selectRule.kf64b2d95'), value: 'checkbox' }
    ],
    // 参考 https://www.form-create.com/v3/guide/control 组件联动，单选框和多选框不需要多选属性
    control: [
      {
        value: 'select',
        condition: '==',
        method: 'hidden',
        rule: [
          'multiple',
          'clearable',
          'collapseTags',
          'multipleLimit',
          'allowCreate',
          'filterable',
          'noMatchText',
          'remote',
          'remoteMethod',
          'reserveKeyword',
          'defaultFirstOption',
          'automaticDropdown'
        ]
      }
    ]
  },
  {
    type: 'switch',
    field: 'filterable',
    title: t('auto.components.FormCreate.src.config.selectRule.keb4aa6f1')
  },
  {
    type: 'switch',
    field: 'multiple',
    title: t('auto.components.FormCreate.src.config.selectRule.kdd177d59')
  },
  {
    type: 'switch',
    field: 'disabled',
    title: t('auto.components.FormCreate.src.config.selectRule.k9858a9a3')
  },
  {
    type: 'switch',
    field: 'clearable',
    title: t('auto.components.FormCreate.src.config.selectRule.k152873ef')
  },
  {
    type: 'switch',
    field: 'collapseTags',
    title: t('auto.components.FormCreate.src.config.selectRule.k2a50d4ab')
  },
  {
    type: 'inputNumber',
    field: 'multipleLimit',
    title: t('auto.components.FormCreate.src.config.selectRule.k4fff277d'),
    props: { min: 0 }
  },
  {
    type: 'input',
    field: 'autocomplete',
    title: t('auto.components.FormCreate.src.config.selectRule.k3939b22d')
  },
  {
    type: 'input',
    field: 'placeholder',
    title: t('auto.components.FormCreate.src.config.selectRule.ka6ea6403')
  },
  {
    type: 'switch',
    field: 'allowCreate',
    title: t('auto.components.FormCreate.src.config.selectRule.ka9f60f79')
  },
  {
    type: 'input',
    field: 'noMatchText',
    title: t('auto.components.FormCreate.src.config.selectRule.k80787d7a')
  },
  {
    type: 'input',
    field: 'noDataText',
    title: t('auto.components.FormCreate.src.config.selectRule.k2ae3ba8b')
  },
  {
    type: 'switch',
    field: 'reserveKeyword',
    title: t('auto.components.FormCreate.src.config.selectRule.k150840a1')
  },
  {
    type: 'switch',
    field: 'defaultFirstOption',
    title: t('auto.components.FormCreate.src.config.selectRule.k394dd545')
  },
  {
    type: 'switch',
    field: 'popperAppendToBody',
    title: t('auto.components.FormCreate.src.config.selectRule.kd9bedfe8'),
    value: true
  },
  {
    type: 'switch',
    field: 'automaticDropdown',
    title: t('auto.components.FormCreate.src.config.selectRule.k0bfe1d3a')
  }
]

const apiSelectRule = [
  {
    type: 'input',
    field: 'url',
    title: t('auto.components.FormCreate.src.config.selectRule.k4ce76c49'),
    props: {
      placeholder: '/system/user/simple-list'
    }
  },
  {
    type: 'select',
    field: 'method',
    title: t('auto.components.FormCreate.src.config.selectRule.k5289f35e'),
    value: 'GET',
    options: [
      { label: 'GET', value: 'GET' },
      { label: 'POST', value: 'POST' }
    ],
    control: [
      {
        value: 'GET',
        condition: '!=',
        method: 'hidden',
        rule: [
          {
            type: 'input',
            field: 'data',
            title: t('auto.components.FormCreate.src.config.selectRule.k1e308214'),
            props: {
              autosize: true,
              type: 'textarea',
              placeholder: '{"type": 1}'
            }
          }
        ]
      }
    ]
  },
  {
    type: 'input',
    field: 'labelField',
    title: t('auto.components.FormCreate.src.config.selectRule.k4615ec53'),
    info: t('auto.components.FormCreate.src.config.selectRule.ka4d987f6'),
    props: {
      placeholder: 'name'
    }
  },
  {
    type: 'input',
    field: 'valueField',
    title: t('auto.components.FormCreate.src.config.selectRule.k530c5c7b'),
    info: t('auto.components.FormCreate.src.config.selectRule.ka4d987f6'),
    props: {
      placeholder: 'id'
    }
  },
  {
    type: 'input',
    field: 'parseFunc',
    title: t('auto.components.FormCreate.src.config.selectRule.kac529c19'),
    info: t('extra.kd975d142'),
    props: {
      autosize: true,
      rows: { minRows: 2, maxRows: 6 },
      type: 'textarea',
      placeholder: `
        function (data) {
            console.log(data)
            return data.list.map(item=> ({label: item.name,value: item.id}))
        }`
    }
  },
  {
    type: 'switch',
    field: 'remote',
    info: t('auto.components.FormCreate.src.config.selectRule.keb4aa6f1'),
    title: t('auto.components.FormCreate.src.config.selectRule.k9e1cd941')
  },
  {
    type: 'input',
    field: 'remoteField',
    title: t('auto.components.FormCreate.src.config.selectRule.k1f9ac54b'),
    info: t('auto.components.FormCreate.src.config.selectRule.k841a3395')
  }
]

export { selectRule, apiSelectRule }
