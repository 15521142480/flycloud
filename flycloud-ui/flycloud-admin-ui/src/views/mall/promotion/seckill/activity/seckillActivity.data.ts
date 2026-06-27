import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { dateFormatter2 } from '@/utils/formatTime'
import { SeckillConfigApi } from '@/api/mall/promotion/seckill/seckillConfig'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const rules = reactive({
  spuId: [required],
  name: [required],
  startTime: [required],
  endTime: [required],
  sort: [required],
  configIds: [required],
  totalLimitCount: [required],
  singleLimitCount: [required],
  totalStock: [required]
})

// CrudSchema https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
  {
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.kdf6e5940'),
    field: 'name',
    isSearch: true,
    form: {
      colProps: {
        span: 24
      }
    },
    table: {
      width: 120
    }
  },
  {
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.k658b1a2e'),
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
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.kcdd4e446'),
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
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.kd38ded65'),
    field: 'configIds',
    form: {
      component: 'Select',
      componentProps: {
        multiple: true,
        optionsAlias: {
          labelField: 'name',
          valueField: 'id'
        }
      },
      api: SeckillConfigApi.getSimpleSeckillConfigList
    },
    table: {
      width: 300
    }
  },
  {
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.kc71a1741'),
    field: 'totalLimitCount',
    form: {
      component: 'InputNumber',
      value: 0
    },
    table: {
      width: 120
    }
  },
  {
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.kf934a1eb'),
    field: 'singleLimitCount',
    form: {
      component: 'InputNumber',
      value: 0
    },
    table: {
      width: 120
    }
  },
  {
    label: t('common.sort'),
    field: 'sort',
    form: {
      component: 'InputNumber',
      value: 0
    },
    table: {
      width: 80
    }
  },
  {
    label: t('auto.views.mall.promotion.seckill.activity.seckillActivity_data.k5dcdcf26'),
    field: 'spuId',
    isTable: true,
    isSearch: false,
    form: {
      colProps: {
        span: 24
      }
    },
    table: {
      width: 300
    }
  },
  {
    label: t('common.remark'),
    field: 'remark',
    isSearch: false,
    form: {
      component: 'Input',
      componentProps: {
        type: 'textarea',
        rows: 4
      },
      colProps: {
        span: 24
      }
    },
    table: {
      width: 300
    }
  }
])
export const { allSchemas } = useCrudSchemas(crudSchemas)
