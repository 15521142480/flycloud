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
      <el-form-item :label="t('auto.views.erp.stock.move.index.kead78d91')" prop="no">
        <el-input
          v-model="queryParams.no"
          :placeholder="t('auto.views.erp.stock.move.index.ka6f7ade4')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.stock.move.index.k6cc98552')" prop="productId">
        <el-select
          v-model="queryParams.productId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.stock.move.index.k59a0d3d1')"
          class="!w-240px"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.stock.move.index.k954ee1d7')" prop="moveTime">
        <el-date-picker
          v-model="queryParams.moveTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.erp.stock.move.index.k1f291968')"
          :end-placeholder="t('auto.views.erp.stock.move.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.stock.move.index.k55914e1c')" prop="fromWarehouseId">
        <el-select
          v-model="queryParams.fromWarehouseId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.stock.move.index.k7aaa3bbb')"
          class="!w-240px"
        >
          <el-option
            v-for="item in warehouseList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.stock.move.index.k787ad1de')" prop="creator">
        <el-select
          v-model="queryParams.creator"
          clearable
          filterable
          :placeholder="t('auto.views.erp.stock.move.index.k953a49d5')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.erp.stock.move.index.kdba277df')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_AUDIT_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="queryParams.remark"
          :placeholder="t('auto.views.erp.stock.move.index.k57e709d9')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
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
          v-hasPermi="['erp:stock-move:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kee05a4cf') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:stock-move:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k12f6f010') }}
        </el-button>
        <el-button
          type="danger"
          plain
          @click="handleDelete(selectionList.map((item) => item.id))"
          v-hasPermi="['erp:stock-move:delete']"
          :disabled="selectionList.length === 0"
        >
          <Icon icon="ep:delete" class="mr-5px" /> {{ t('extra.kc3027901') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :stripe="true"
      :show-overflow-tooltip="true"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        width="30"
        :label="t('auto.views.erp.stock.move.index.k70b20820')"
        type="selection"
      />
      <el-table-column
        min-width="180"
        :label="t('auto.views.erp.stock.move.index.kead78d91')"
        align="center"
        prop="no"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.move.index.k90095856')"
        align="center"
        prop="productNames"
        min-width="200"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.move.index.k954ee1d7')"
        align="center"
        prop="moveTime"
        :formatter="dateFormatter2"
        width="120px"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.move.index.k787ad1de')"
        align="center"
        prop="creatorName"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.move.index.kb9ae8931')"
        align="center"
        prop="totalCount"
        :formatter="erpCountTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.stock.move.index.k34943c40')"
        align="center"
        prop="totalPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('common.status')"
        align="center"
        fixed="right"
        width="90"
        prop="status"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_AUDIT_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="220">
        <template #default="scope">
          <el-button
            link
            @click="openForm('detail', scope.row.id)"
            v-hasPermi="['erp:stock-move:query']"
          >
            详情
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:stock-move:update']"
            :disabled="scope.row.status === 20"
          >
            编辑
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleUpdateStatus(scope.row.id, 20)"
            v-hasPermi="['erp:stock-move:update-status']"
            v-if="scope.row.status === 10"
          >
            审批
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleUpdateStatus(scope.row.id, 10)"
            v-hasPermi="['erp:stock-move:update-status']"
            v-else
          >
            反审批
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete([scope.row.id])"
            v-hasPermi="['erp:stock-move:delete']"
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
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <StockMoveForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import { StockMoveApi, StockMoveVO } from '@/api/erp/stock/move'
import StockMoveForm from './StockMoveForm.vue'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import { UserVO } from '@/api/system/user'
import * as UserApi from '@/api/system/user'
import { erpCountTableColumnFormatter, erpPriceTableColumnFormatter } from '@/utils'

/** ERP 库存调度单列表 */
const { t } = useI18n()
defineOptions({ name: 'ErpStockMove' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<StockMoveVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: undefined,
  productId: undefined,
  fromWarehouseId: undefined,
  moveTime: [],
  status: undefined,
  remark: undefined,
  creator: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const productList = ref<ProductVO[]>([]) // 产品列表
const warehouseList = ref<WarehouseVO[]>([]) // 仓库列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockMoveApi.getStockMovePage(queryParams)
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

/** 删除按钮操作 */
const handleDelete = async (ids: number[]) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await StockMoveApi.deleteStockMove(ids)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
    selectionList.value = selectionList.value.filter((item) => !ids.includes(item.id))
  } catch {}
}

/** 审批/反审批操作 */
const handleUpdateStatus = async (id: number, status: number) => {
  try {
    // 审批的二次确认
    await message.confirm(
      t('extra.kdb7c256a', {
        p0:
          status === 20
            ? t('auto.views.erp.stock.move.index.k5ce60cb7')
            : t('auto.views.erp.stock.move.index.k5e6e19f7')
      })
    )
    // 发起审批
    await StockMoveApi.updateStockMoveStatus(id, status)
    message.success(
      t('extra.k0c3e2ea8', {
        p0:
          status === 20
            ? t('auto.views.erp.stock.move.index.k5ce60cb7')
            : t('auto.views.erp.stock.move.index.k5e6e19f7')
      })
    )
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
    const data = await StockMoveApi.exportStockMove(queryParams)
    download.excel(data, t('auto.views.erp.stock.move.index.k59a532d7'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 选中操作 */
const selectionList = ref<StockMoveVO[]>([])
const handleSelectionChange = (rows: StockMoveVO[]) => {
  selectionList.value = rows
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载产品、仓库列表、客户
  productList.value = await ProductApi.getProductSimpleList()
  warehouseList.value = await WarehouseApi.getWarehouseSimpleList()
  userList.value = await UserApi.getSimpleUserList()
})
// TODO 芋艿：可优化功能：列表界面，支持导入
// TODO 芋艿：可优化功能：详情界面，支持打印
</script>
