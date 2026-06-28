<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item :label="t('auto.views.pay.transfer.index.kd3f2bbea')" prop="no">
        <el-input
          v-model="queryParams.no"
          :placeholder="t('auto.views.pay.transfer.index.kff2bf104')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.transfer.index.kb358c815')" prop="channelCode">
        <el-select
          v-model="queryParams.channelCode"
          :placeholder="t('auto.views.pay.transfer.index.kf9bee2a2')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.PAY_CHANNEL_CODE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.transfer.index.k3dc2266a')" prop="merchantTransferId">
        <el-input
          v-model="queryParams.merchantTransferId"
          :placeholder="t('auto.views.pay.transfer.index.k7c0615c3')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.transfer.index.ke4e46c72')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.pay.transfer.index.k97f47b78')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.PAY_TRANSFER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.transfer.index.k1a3ddc07')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.pay.transfer.index.k8dd46b56')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.PAY_TRANSFER_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('auto.views.pay.transfer.index.kad69bce0')" prop="userName">
        <el-input
          v-model="queryParams.userName"
          :placeholder="t('auto.views.pay.transfer.index.k9d80ad0f')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.transfer.index.ke551deaa')" prop="channelTransferNo">
        <el-input
          v-model="queryParams.channelTransferNo"
          :placeholder="t('auto.views.pay.transfer.index.ke551deaa')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.pay.transfer.index.k1f291968')"
          :end-placeholder="t('auto.views.pay.transfer.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
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
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.pay.transfer.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        :label="t('auto.views.pay.transfer.index.k396d9d78')"
        align="center"
        prop="appId"
      />
      <el-table-column
        :label="t('auto.views.pay.transfer.index.ke4e46c72')"
        align="center"
        prop="type"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_TRANSFER_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.pay.transfer.TransferDetail.ka6701065')"
        align="center"
        prop="price"
      >
        <template #default="scope">
          <span>￥{{ (scope.row.price / 100.0).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.pay.transfer.TransferDetail.k1a3ddc07')"
        align="center"
        prop="status"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_TRANSFER_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.pickUpOrder.index.k459868e5')"
        align="left"
        width="300"
      >
        <template #default="scope">
          <p class="transfer-font">
            <el-tag size="small"> {{ t('extra.k9f1ea351') }}</el-tag>
            {{ scope.row.merchantTransferId }}
          </p>
          <p class="transfer-font" v-if="scope.row.no">
            <el-tag size="small" type="warning">{{ t('extra.kacaf665f') }}</el-tag>
            {{ scope.row.no }}
          </p>
          <p class="transfer-font" v-if="scope.row.channelTransferNo">
            <el-tag size="small" type="success">{{ t('extra.kebee0533') }}</el-tag>
            {{ scope.row.channelTransferNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.pay.demo.transfer.DemoTransferForm.kad69bce0')"
        align="center"
        prop="userName"
        width="120"
      />
      <el-table-column :label="t('extra.k31eab821')" align="left" width="200">
        <template #default="scope">
          <p class="transfer-font" v-if="scope.row.alipayLogonId">
            <el-tag size="small">{{ t('extra.kc7e48738') }}</el-tag>
            {{ scope.row.alipayLogonId }}
          </p>
          <p class="transfer-font" v-if="scope.row.openid">
            <el-tag size="small">{{ t('extra.kf3590b06') }}</el-tag>
            {{ scope.row.openid }}
          </p>
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.k682eb4c1')" align="center" prop="subject" width="120" />
      <el-table-column
        :label="t('auto.views.pay.transfer.index.kb358c815')"
        align="center"
        prop="channelCode"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k221632df')"
        align="center"
        prop="successTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openDetail(scope.row.id)">
            {{ t('action.detail') }}
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
    <TransferDetail ref="detailRef" />
  </ContentWrap>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as TransferApi from '@/api/pay/transfer'
import { DICT_TYPE, getStrDictOptions } from '@/utils/dict'
import TransferDetail from './TransferDetail.vue'
const { t } = useI18n()
defineOptions({ name: 'PayTransfer' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: null,
  appId: null,
  channelId: null,
  channelCode: null,
  merchantTransferId: null,
  type: null,
  status: null,
  successTime: [],
  price: null,
  subject: null,
  userName: null,
  alipayLogonId: null,
  openid: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await TransferApi.getTransferPage(queryParams)
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
const detailRef = ref()
const openDetail = (id: number) => {
  detailRef.value.open(id)
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>
.transfer-font {
  padding: 2px 0;
  font-size: 12px;
}
</style>
