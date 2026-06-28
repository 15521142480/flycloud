<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
    :disabled="disabled"
  >
    <el-table :data="formData" show-summary :summary-method="getSummaries" class="-mt-10px">
      <el-table-column :label="t('common.index')" type="index" align="center" width="60" />
      <el-table-column
        :label="t('auto.views.erp.stock.check.components.StockCheckItemForm.kc9084dc8')"
        min-width="125"
      >
        <template #default="{ row, $index }">
          <el-form-item
            :prop="`${$index}.warehouseId`"
            :rules="formRules.warehouseId"
            class="mb-0px!"
          >
            <el-select
              v-model="row.warehouseId"
              clearable
              filterable
              :placeholder="t('auto.views.erp.stock.check.components.StockCheckItemForm.kf2f948fa')"
              @change="onChangeWarehouse($event, row)"
            >
              <el-option
                v-for="item in warehouseList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.business.components.BusinessProductForm.kabc0ac79')"
        min-width="180"
      >
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.productId`" :rules="formRules.productId" class="mb-0px!">
            <el-select
              v-model="row.productId"
              clearable
              filterable
              @change="onChangeProduct($event, row)"
              :placeholder="t('auto.views.crm.business.components.BusinessProductForm.k59a0d3d1')"
            >
              <el-option
                v-for="item in productList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.k79c2375f')" min-width="100">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.stockCount" :formatter="erpCountInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.erp.product.product.ProductForm.kb1085310')"
        min-width="150"
      >
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.productBarCode" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.erp.product.product.ProductForm.k8ec8c8c7')"
        min-width="80"
      >
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.productUnitName" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.k28b9a9d0')" fixed="right" min-width="140">
        <template #default="{ row, $index }">
          <el-form-item
            :prop="`${$index}.actualCount`"
            :rules="formRules.actualCount"
            class="mb-0px!"
          >
            <el-input-number
              v-model="row.actualCount"
              controls-position="right"
              :precision="3"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.kfb469944')" prop="count" fixed="right" min-width="110">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.count`" :rules="formRules.count" class="mb-0px!">
            <el-input
              disabled
              v-model="row.count"
              :formatter="erpCountInputFormatter"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.ke3fff305')" fixed="right" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.productPrice`" class="mb-0px!">
            <el-input-number
              v-model="row.productPrice"
              controls-position="right"
              :min="0.01"
              :precision="2"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k21f695cf')"
        prop="totalPrice"
        fixed="right"
        min-width="100"
      >
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.totalPrice`" class="mb-0px!">
            <el-input disabled v-model="row.totalPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.remark')" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.remark`" class="mb-0px!">
            <el-input
              v-model="row.remark"
              :placeholder="t('auto.views.crm.business.BusinessForm.k57e709d9')"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" :label="t('common.operation')" width="60">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3" v-if="!disabled">
    <el-button @click="handleAdd" round>{{ t('extra.k1c245d18') }}</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import { StockApi } from '@/api/erp/stock/stock'
import {
  erpCountInputFormatter,
  erpPriceInputFormatter,
  erpPriceMultiply,
  getSumValue
} from '@/utils'
const { t } = useI18n()
const props = defineProps<{
  items: undefined
  disabled: false
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref([])
const formRules = reactive({
  inId: [
    {
      required: true,
      message: t('auto.views.erp.stock.check.components.StockCheckItemForm.kb6185f3a'),
      trigger: 'blur'
    }
  ],
  warehouseId: [
    {
      required: true,
      message: t('auto.views.erp.stock.check.components.StockCheckItemForm.k7f4d1c67'),
      trigger: 'blur'
    }
  ],
  productId: [
    {
      required: true,
      message: t('auto.views.erp.stock.check.components.StockCheckItemForm.k0b3cde2b'),
      trigger: 'blur'
    }
  ],
  count: [
    {
      required: true,
      message: t('auto.views.erp.stock.check.components.StockCheckItemForm.k55ce5fbe'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref([]) // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表
const warehouseList = ref<WarehouseVO[]>([]) // 仓库列表
const defaultWarehouse = ref<WarehouseVO>(undefined) // 默认仓库

/** 初始化设置盘点项 */
watch(
  () => props.items,
  async (val) => {
    formData.value = val
  },
  { immediate: true }
)

/** 监听合同产品变化，计算合同产品总价 */
watch(
  () => formData.value,
  (val) => {
    if (!val || val.length === 0) {
      return
    }
    // 循环处理
    val.forEach((item) => {
      if (item.stockCount != null && item.actualCount != null) {
        item.count = item.actualCount - item.stockCount
      } else {
        item.count = undefined
      }
      item.totalPrice = erpPriceMultiply(item.productPrice, item.count)
    })
  },
  { deep: true }
)

/** 合计 */
const getSummaries = (param: SummaryMethodProps) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = t('auto.views.erp.stock.check.components.StockCheckItemForm.k92bcbf71')
      return
    }
    if (['count', 'totalPrice'].includes(column.property)) {
      const sum = getSumValue(data.map((item) => Number(item[column.property])))
      sums[index] =
        column.property === 'count' ? erpCountInputFormatter(sum) : erpPriceInputFormatter(sum)
    } else {
      sums[index] = ''
    }
  })

  return sums
}

/** 新增按钮操作 */
const handleAdd = () => {
  const row = {
    id: undefined,
    warehouseId: defaultWarehouse.value?.id,
    productId: undefined,
    productUnitName: undefined, // 产品单位
    productBarCode: undefined, // 产品条码
    productPrice: undefined,
    stockCount: undefined,
    actualCount: undefined,
    count: undefined,
    totalPrice: undefined,
    remark: undefined
  }
  formData.value.push(row)
}

/** 删除按钮操作 */
const handleDelete = (index) => {
  formData.value.splice(index, 1)
}

/** 处理仓库变更 */
const onChangeWarehouse = (warehouseId, row) => {
  // 加载库存
  setStockCount(row)
}

/** 处理产品变更 */
const onChangeProduct = (productId, row) => {
  const product = productList.value.find((item) => item.id === productId)
  if (product) {
    row.productUnitName = product.unitName
    row.productBarCode = product.barCode
    row.productPrice = product.minPrice
  }
  // 加载库存
  setStockCount(row)
}

/** 加载库存 */
const setStockCount = async (row) => {
  if (!row.productId || !row.warehouseId) {
    return
  }
  const stock = await StockApi.getStock2(row.productId, row.warehouseId)
  row.stockCount = stock ? stock.count : 0
  row.actualCount = row.stockCount
}

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
}
defineExpose({ validate })

/** 初始化 */
onMounted(async () => {
  productList.value = await ProductApi.getProductSimpleList()
  warehouseList.value = await WarehouseApi.getWarehouseSimpleList()
  defaultWarehouse.value = warehouseList.value.find((item) => item.defaultStatus)
  // 默认添加一个
  if (formData.value.length === 0) {
    handleAdd()
  }
})
</script>
