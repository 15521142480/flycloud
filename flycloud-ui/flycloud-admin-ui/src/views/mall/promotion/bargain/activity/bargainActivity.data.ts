import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { dateFormatter2 } from '@/utils/formatTime'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const rules = reactive({
  name: [required],
  startTime: [required],
  endTime: [required],
  helpMaxCount: [required],
  bargainCount: [required],
  singleLimitCount: [required]
})

// CrudSchema https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
  {
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k90ffb5db'),
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
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k658b1a2e'),
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
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kcdd4e446'),
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
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.ke22d41fa'),
    field: 'helpMaxCount',
    isSearch: false,
    form: {
      component: 'InputNumber',
      labelMessage: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kf414e85e'),
      value: 2
    }
  },
  {
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kf87d008f'),
    field: 'bargainCount',
    isSearch: false,
    form: {
      component: 'InputNumber',
      labelMessage: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kf414e85e'),
      value: 2
    }
  },
  {
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kc71a1741'),
    field: 'totalLimitCount',
    isSearch: false,
    form: {
      component: 'InputNumber',
      labelMessage: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kf9bb3cb2'),
      value: 0
    }
  },
  {
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k406431e6'),
    field: 'randomMinPrice',
    isSearch: false,
    isTable: false,
    form: {
      component: 'InputNumber',
      componentProps: {
        min: 0,
        precision: 2,
        step: 0.1
      },
      labelMessage: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k4f028baf'),
      value: 0
    }
  },
  {
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k256f3f0e'),
    field: 'randomMaxPrice',
    isSearch: false,
    isTable: false,
    form: {
      component: 'InputNumber',
      componentProps: {
        min: 0,
        precision: 2,
        step: 0.1
      },
      labelMessage: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k9b5c6f39'),
      value: 0
    }
  },
  {
    label: t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.ka2bb9357'),
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
