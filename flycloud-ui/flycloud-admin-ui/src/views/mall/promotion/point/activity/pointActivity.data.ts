import type { CrudSchema } from '@/hooks/web/useCrudSchemas'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const rules = reactive({
  spuId: [required],
  sort: [required]
})

// CrudSchema https://doc.iocoder.cn/vue3/crud-schema/
const crudSchemas = reactive<CrudSchema[]>([
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
    label: t('auto.views.mall.promotion.point.activity.pointActivity_data.k324aa117'),
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
