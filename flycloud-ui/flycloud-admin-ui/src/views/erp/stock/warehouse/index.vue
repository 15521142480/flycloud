<!-- ERP 仓库列表 -->
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
      <el-form-item :label="t('auto.views.erp.stock.warehouse.index.k6f55a5a5')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.erp.stock.warehouse.index.k2c2b2339')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.stock.warehouse.index.kc89e2565')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.erp.stock.warehouse.index.k9ce39923')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
          v-hasPermi="['erp:warehouse:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k085ab298') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:warehouse:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k08bf21ea') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.erp.stock.warehouse.index.k6f55a5a5')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.warehouse.index.k6b470c46')"
        align="center"
        prop="address"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.warehouse.index.kf8c432d9')"
        align="center"
        prop="warehousePrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.warehouse.index.k2a8526e4')"
        align="center"
        prop="truckagePrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.warehouse.index.k974d383f')"
        align="center"
        prop="principal"
      />
      <el-table-column :label="t('common.remark')" align="center" prop="remark" />
      <el-table-column :label="t('common.sort')" align="center" prop="sort" />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.member.user.detail.UserAddressList.k8f91be14')"
        align="center"
        prop="defaultStatus"
      >
        <template #default="scope">
          <el-switch
            v-model="scope.row.defaultStatus"
            :active-value="true"
            :inactive-value="false"
            @change="handleDefaultStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:warehouse:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:warehouse:delete']"
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
  <WarehouseForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import WarehouseForm from './WarehouseForm.vue'
import { erpPriceTableColumnFormatter } from '@/utils'

/** ERP 仓库列表 */
const { t } = useI18n()
defineOptions({ name: 'ErpWarehouse' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<WarehouseVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
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
    const data = await WarehouseApi.getWarehousePage(queryParams)
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
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: string) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await WarehouseApi.deleteWarehouse(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 修改默认状态 */
const handleDefaultStatusChange = async (row: WarehouseVO) => {
  try {
    // 修改状态的二次确认
    const text = row.defaultStatus
      ? t('auto.views.erp.stock.warehouse.index.k7debf9cb')
      : t('common.cancel')
    await message.confirm(
      t('auto.views.erp.stock.warehouse.index.k2be38185') +
        text +
        '"' +
        row.name +
        t('auto.views.erp.stock.warehouse.index.k02c8bacb')
    )
    // 发起修改状态
    await WarehouseApi.updateWarehouseDefaultStatus(row.id, row.defaultStatus)
    // 刷新列表
    await getList()
  } catch (e) {
    // 取消后，进行恢复按钮
    row.defaultStatus = !row.defaultStatus
  }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await WarehouseApi.exportWarehouse(queryParams)
    download.excel(data, t('auto.views.erp.stock.warehouse.index.kfffba64a'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
