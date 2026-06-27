import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { dateFormatter } from '@/utils/formatTime'
import { TableColumn } from '@/types/table'
import * as MailAccountApi from '@/api/system/mail/account'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

const accountList = await MailAccountApi.getSimpleMailAccountList()

// 表单校验
export const rules = reactive({
  name: [required],
  code: [required],
  accountId: [required],
  label: [required],
  content: [required],
  params: [required],
  status: [required]
})

// CrudSchema：https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
  {
    label: t('auto.views.system.mail.template.template_data.k5695a649'),
    field: 'code',
    isSearch: true,
    search: {
      componentProps: {
        style: {
          width: '240px'
        }
      }
    }
  },
  {
    label: t('auto.views.system.mail.template.template_data.kbbc511d0'),
    field: 'name',
    isSearch: true,
    search: {
      componentProps: {
        style: {
          width: '240px'
        }
      }
    }
  },
  {
    label: t('auto.views.system.mail.template.template_data.k778d92f7'),
    field: 'title'
  },
  {
    label: t('auto.views.system.mail.template.template_data.kdc362463'),
    field: 'content',
    form: {
      component: 'Editor',
      componentProps: {
        valueHtml: '',
        height: 200
      }
    }
  },
  {
    label: t('auto.views.system.mail.template.template_data.kfa8cbad2'),
    field: 'accountId',
    width: '200px',
    formatter: (_: Recordable, __: TableColumn, cellValue: number) => {
      return accountList.find((account) => account.id === cellValue)?.mail
    },
    search: {
      show: true,
      component: 'Select',
      api: () => accountList,
      componentProps: {
        optionsAlias: {
          labelField: 'mail',
          valueField: 'id'
        },
        style: {
          width: '240px'
        }
      }
    },
    form: {
      component: 'Select',
      api: () => accountList,
      componentProps: {
        optionsAlias: {
          labelField: 'mail',
          valueField: 'id'
        }
      }
    }
  },
  {
    label: t('auto.views.system.mail.template.template_data.k008bf359'),
    field: 'name'
  },
  {
    label: t('auto.views.system.mail.template.template_data.k6bbda1b1'),
    field: 'status',
    isSearch: true,
    dictType: DICT_TYPE.COMMON_STATUS,
    dictClass: 'number',
    search: {
      componentProps: {
        style: {
          width: '240px'
        }
      }
    }
  },
  {
    label: t('common.remark'),
    field: 'remark',
    isTable: false
  },
  {
    label: t('common.createTime'),
    field: 'createTime',
    isForm: false,
    formatter: dateFormatter,
    search: {
      show: true,
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        type: 'daterange',
        defaultTime: [new Date('1 00:00:00'), new Date('1 23:59:59')],
        style: {
          width: '240px'
        }
      }
    }
  },
  {
    label: t('common.operation'),
    field: 'action',
    isForm: false
  }
])
export const { allSchemas } = useCrudSchemas(crudSchemas)
