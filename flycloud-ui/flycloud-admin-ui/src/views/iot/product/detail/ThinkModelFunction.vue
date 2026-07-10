<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunction.ke18bdddb')"
        prop="name"
      >
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunction.k0ea12ac9')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_PRODUCT_FUNCTION_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"
          ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
        >
        <el-button @click="resetQuery"
          ><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button
        >
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['iot:think-model-function:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k24fcd440') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>
  <ContentWrap>
    <el-tabs>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
        <el-table-column
          :label="t('auto.views.iot.product.detail.ThinkModelFunction.ke18bdddb')"
          align="center"
          prop="type"
        >
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.IOT_PRODUCT_FUNCTION_TYPE" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column
          :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k785d1756')"
          align="center"
          prop="name"
        />
        <el-table-column
          :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k85803da5')"
          align="center"
          prop="identifier"
        />
        <el-table-column :label="t('common.operation')" align="center">
          <template #default="scope">
            <el-button
              link
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="[`iot:think-model-function:update`]"
            >
              {{ t('common.edit') }}
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['iot:think-model-function:delete']"
            >
              {{ t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <Pagination
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-tabs>
  </ContentWrap>
  <!-- 表单弹窗：添加/修改 -->
  <ThinkModelFunctionForm ref="formRef" :product="product" @success="getList" />
</template>
<script setup lang="ts">
import { ProductVO } from '@/api/iot/product'
import { ThinkModelFunctionApi, ThinkModelFunctionVO } from '@/api/iot/thinkmodelfunction'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ThinkModelFunctionForm from '@/views/iot/product/detail/ThinkModelFunctionForm.vue'
const { t } = useI18n()
const props = defineProps<{ product: ProductVO }>()
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<ThinkModelFunctionVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  type: undefined,
  productId: -1
})

const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    queryParams.productId = props.product.id
    const data = await ThinkModelFunctionApi.getThinkModelFunctionPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}
/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  queryParams.type = undefined
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: string) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ThinkModelFunctionApi.deleteThinkModelFunction(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
