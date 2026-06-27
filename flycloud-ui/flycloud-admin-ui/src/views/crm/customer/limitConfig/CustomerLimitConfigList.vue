<template>
  <el-button plain @click="handleQuery">
    <Icon icon="ep:refresh" class="mr-5px" />
    {{ t('auto.views.crm.customer.limitConfig.CustomerLimitConfigList.k38108eaa') }}
  </el-button>
  <el-button
    type="primary"
    plain
    @click="openForm('create')"
    v-hasPermi="['crm:customer-limit-config:create']"
  >
    <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.ka30e2f1d') }}
  </el-button>
  <el-table
    v-loading="loading"
    :data="list"
    :stripe="true"
    :show-overflow-tooltip="true"
    class="mt-4"
  >
    <el-table-column
      :label="t('auto.views.crm.customer.limitConfig.CustomerLimitConfigList.k9f42dac6')"
      align="center"
      prop="id"
    />
    <el-table-column
      :label="t('auto.views.crm.customer.limitConfig.CustomerLimitConfigList.kc4698aef')"
      align="center"
      :formatter="(row) => row.users?.map((user: any) => user.name).join('，')"
    />
    <el-table-column
      :label="t('auto.views.crm.customer.limitConfig.CustomerLimitConfigList.kcfcb34f1')"
      align="center"
      :formatter="(row) => row.depts?.map((dept: any) => dept.name).join('，')"
    />
    <el-table-column
      :label="
        confType === LimitConfType.CUSTOMER_QUANTITY_LIMIT
          ? t('extra.kb0786d6f')
          : t('extra.k7f98a6f4')
      "
      align="center"
      prop="maxCount"
    />
    <el-table-column
      v-if="confType === LimitConfType.CUSTOMER_QUANTITY_LIMIT"
      :label="t('auto.views.crm.customer.limitConfig.CustomerLimitConfigList.k4531f29a')"
      align="center"
      prop="dealCountEnabled"
      min-width="100"
    >
      <template #default="scope">
        <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.dealCountEnabled" />
      </template>
    </el-table-column>
    <el-table-column
      label="创建时间"
      align="center"
      prop="createTime"
      :formatter="dateFormatter"
      width="180px"
    />
    <el-table-column label="操作" align="center" min-width="110" fixed="right">
      <template #default="scope">
        <el-button
          link
          type="primary"
          @click="openForm('update', scope.row.id)"
          v-hasPermi="['crm:customer-limit-config:update']"
        >
          编辑
        </el-button>
        <el-button
          link
          type="danger"
          @click="handleDelete(scope.row.id)"
          v-hasPermi="['crm:customer-limit-config:delete']"
        >
          删除
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

  <!-- 表单弹窗：添加/修改 -->
  <CustomerLimitConfigForm ref="formRef" @success="getList" />
</template>
<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as CustomerLimitConfigApi from '@/api/crm/customer/limitConfig'
import CustomerLimitConfigForm from './CustomerLimitConfigForm.vue'
import { DICT_TYPE } from '@/utils/dict'
import { LimitConfType } from '@/api/crm/customer/limitConfig'
const { t } = useI18n()
defineOptions({ name: 'CustomerLimitConfigList' })

const message = useMessage() // 消息弹窗

const { confType } = defineProps<{ confType: LimitConfType }>()

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  type: confType
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CustomerLimitConfigApi.getCustomerLimitConfigPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, confType, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await CustomerLimitConfigApi.deleteCustomerLimitConfig(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
