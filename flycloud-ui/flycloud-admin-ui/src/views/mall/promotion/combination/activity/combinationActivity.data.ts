import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { dateFormatter2 } from '@/utils/formatTime'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const rules = reactive({
  name: [required],
  totalLimitCount: [required],
  singleLimitCount: [required],
  startTime: [required],
  endTime: [required],
  userSize: [required],
  limitDuration: [required],
  virtualGroup: [required]
})

// CrudSchema https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.kc64794f3'),
    field: 'name',
    isSearch: true,
    isTable: false,
    form: {
      colProps: {
        span: 24
      }
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.k658b1a2e'),
    field: 'startTime',
    formatter: dateFormatter2,
    isSearch: true,
    search: {
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
        type: 'daterange'
      }
    },
    form: {
      component: 'DatePicker',
      componentProps: {
        type: 'date',
        valueFormat: 'x'
      }
    },
    table: {
      width: 120
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.kcdd4e446'),
    field: 'endTime',
    formatter: dateFormatter2,
    isSearch: true,
    search: {
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
        type: 'daterange'
      }
    },
    form: {
      component: 'DatePicker',
      componentProps: {
        type: 'date',
        valueFormat: 'x'
      }
    },
    table: {
      width: 120
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.kf2f555f3'),
    field: 'userSize',
    isSearch: false,
    form: {
      component: 'InputNumber',
      labelMessage: t(
        'auto.views.mall.promotion.combination.activity.combinationActivity_data.kf414e85e'
      ),
      value: 2
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.kc2746f69'),
    field: 'limitDuration',
    isSearch: false,
    isTable: false,
    form: {
      component: 'InputNumber',
      labelMessage: t(
        'auto.views.mall.promotion.combination.activity.combinationActivity_data.k52d6bbca'
      ),
      componentProps: {
        placeholder: t(
          'auto.views.mall.promotion.combination.activity.combinationActivity_data.kfedaaaab'
        )
      }
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.kc71a1741'),
    field: 'totalLimitCount',
    isSearch: false,
    isTable: false,
    form: {
      component: 'InputNumber',
      value: 0
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.k3719b703'),
    field: 'singleLimitCount',
    isSearch: false,
    isTable: false,
    form: {
      component: 'InputNumber',
      value: 0
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.k0faadec1'),
    field: 'virtualGroup',
    dictType: DICT_TYPE.INFRA_BOOLEAN_STRING,
    dictClass: 'boolean',
    isSearch: true,
    form: {
      component: 'Radio',
      value: false
    }
  },
  {
    label: t('auto.views.mall.promotion.combination.activity.combinationActivity_data.kcc137447'),
    field: 'spuId',
    isSearch: false,
    form: {
      colProps: {
        span: 24
      }
    }
  }
])
export const { allSchemas } = useCrudSchemas(crudSchemas)
