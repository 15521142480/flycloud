import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { dateFormatter } from '@/utils/formatTime'
import * as MailAccountApi from '@/api/system/mail/account'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

const accountList = await MailAccountApi.getSimpleMailAccountList()

// CrudSchema：https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
  {
    label: t('auto.views.system.mail.log.log_data.k9f42dac6'),
    field: 'id'
  },
  {
    label: t('auto.views.system.mail.log.log_data.k98c64dd6'),
    field: 'sendTime',
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
    },
    detail: {
      dateFormat: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  {
    label: t('auto.views.system.mail.log.log_data.k3f7dab4f'),
    field: 'toMail'
  },
  {
    label: t('auto.views.system.mail.log.log_data.kec750ef6'),
    field: 'userId',
    isSearch: true,
    isTable: false,
    search: {
      componentProps: {
        style: {
          width: '240px'
        }
      }
    }
  },
  {
    label: t('auto.views.system.mail.log.log_data.k31ab92d1'),
    field: 'userType',
    dictType: DICT_TYPE.USER_TYPE,
    dictClass: 'number',
    isSearch: true,
    isTable: false,
    search: {
      componentProps: {
        style: {
          width: '240px'
        }
      }
    }
  },
  {
    label: t('auto.views.system.mail.log.log_data.kb74ef817'),
    field: 'templateTitle'
  },
  {
    label: t('auto.views.system.mail.log.log_data.kc2c69a78'),
    field: 'templateContent',
    isTable: false
  },
  {
    label: t('auto.views.system.mail.log.log_data.kd5d1c2b6'),
    field: 'templateParams',
    isTable: false
  },
  {
    label: t('auto.views.system.mail.log.log_data.k9fa54101'),
    field: 'sendStatus',
    dictType: DICT_TYPE.SYSTEM_MAIL_SEND_STATUS,
    dictClass: 'string',
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
    label: t('auto.views.system.mail.log.log_data.kfa8cbad2'),
    field: 'accountId',
    isTable: false,
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
    }
  },
  {
    label: t('auto.views.system.mail.log.log_data.kbb744a0c'),
    field: 'fromMail',
    table: {
      label: t('auto.views.system.mail.log.log_data.kfa8cbad2')
    }
  },
  {
    label: t('auto.views.system.mail.log.log_data.k3fa1ce06'),
    field: 'templateId',
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
    label: t('auto.views.system.mail.log.log_data.k5695a649'),
    field: 'templateCode',
    isTable: false
  },
  {
    label: t('auto.views.system.mail.log.log_data.kc29292a3'),
    field: 'templateNickname',
    isTable: false
  },
  {
    label: t('auto.views.system.mail.log.log_data.k8ab2dea5'),
    field: 'sendMessageId',
    isTable: false
  },
  {
    label: t('auto.views.system.mail.log.log_data.k4a3f8493'),
    field: 'sendException',
    isTable: false
  },
  {
    label: t('common.createTime'),
    field: 'createTime',
    isTable: false,
    formatter: dateFormatter,
    detail: {
      dateFormat: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  {
    label: t('common.operation'),
    field: 'action',
    isDetail: false
  }
])
export const { allSchemas } = useCrudSchemas(crudSchemas)
