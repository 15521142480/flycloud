import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { dateFormatter } from '@/utils/formatTime'

// 表单校验
const { t } = useI18n()

export const rules = reactive({
  mail: [
    { required: true, message: t('profile.rules.mail'), trigger: 'blur' },
    {
      type: 'email',
      message: t('profile.rules.truemail'),
      trigger: ['blur', 'change']
    }
  ],
  username: [required],
  password: [required],
  host: [required],
  port: [required],
  sslEnable: [required],
  starttlsEnable: [required]
})

// CrudSchema：https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
  {
    label: t('auto.views.system.mail.account.account_data.k9ed627bc'),
    field: 'mail',
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
    label: t('auto.views.system.mail.account.account_data.ka1aaf352'),
    field: 'username',
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
    label: t('auto.views.system.mail.account.account_data.kc839a8ff'),
    field: 'password',
    isTable: false
  },
  {
    label: t('auto.views.system.mail.account.account_data.k67500f39'),
    field: 'host'
  },
  {
    label: t('auto.views.system.mail.account.account_data.kb2757fdd'),
    field: 'port',
    form: {
      component: 'InputNumber',
      value: 465
    }
  },
  {
    label: t('auto.views.system.mail.account.account_data.k83228f16'),
    field: 'sslEnable',
    dictType: DICT_TYPE.INFRA_BOOLEAN_STRING,
    dictClass: 'boolean',
    form: {
      component: 'Radio'
    }
  },
  {
    label: t('auto.views.system.mail.account.account_data.k8f99daea'),
    field: 'starttlsEnable',
    dictType: DICT_TYPE.INFRA_BOOLEAN_STRING,
    dictClass: 'boolean',
    form: {
      component: 'Radio'
    }
  },
  {
    label: t('common.createTime'),
    field: 'createTime',
    isForm: false,
    formatter: dateFormatter,
    detail: {
      dateFormat: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  {
    label: t('common.operation'),
    field: 'action',
    isForm: false,
    isDetail: false
  }
])
export const { allSchemas } = useCrudSchemas(crudSchemas)
