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
      <el-form-item :label="t('auto.views.crm.product.index.kabc0ac79')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.crm.product.index.k8d76e9ae')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.crm.product.index.kdba277df')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.CRM_PRODUCT_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}
        </el-button>
        <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:product:create']">
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k88e0cc73') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['crm:product:export']"
        >
          <Icon icon="ep:download" class="mr-5px" />
          {{ t('extra.k67f0095a') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.crm.product.index.kabc0ac79')"
        align="center"
        prop="name"
        width="160"
      >
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.product.ProductForm.k93b6f015')"
        align="center"
        prop="categoryName"
        width="160"
      />
      <el-table-column
        :label="t('auto.views.crm.product.ProductForm.k298ff4b9')"
        align="center"
        prop="unit"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_PRODUCT_UNIT" :value="scope.row.unit" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.product.ProductForm.k440ef986')"
        align="center"
        prop="no"
      />
      <el-table-column
        :label="t('extra.k3a50fbd4')"
        align="center"
        prop="price"
        :formatter="erpPriceTableColumnFormatter"
        width="100"
      />
      <el-table-column
        :label="t('auto.views.crm.product.ProductForm.k03917006')"
        align="center"
        prop="description"
        width="150"
      />
      <el-table-column
        :label="t('auto.views.crm.product.ProductForm.k461446e9')"
        align="center"
        prop="status"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_PRODUCT_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.business.BusinessForm.k974d383f')"
        align="center"
        prop="ownerUserName"
        width="120"
      />
      <el-table-column
        :label="t('common.updateTime')"
        align="center"
        prop="updateTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
        align="center"
        prop="creatorName"
        width="120"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center" fixed="right" width="160">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['crm:product:update']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:product:delete']"
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
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ProductForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ProductApi from '@/api/crm/product'
import ProductForm from './ProductForm.vue'
import { erpPriceTableColumnFormatter } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'CrmProduct' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProductApi.getProductPage(queryParams)
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
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 打开详情 */
const { currentRoute, push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmProductDetail', params: { id } })
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ProductApi.deleteProduct(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProductApi.exportProduct(queryParams)
    download.excel(data, t('auto.views.crm.product.index.k02eb085b'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 激活时 */
onActivated(() => {
  getList()
})

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
