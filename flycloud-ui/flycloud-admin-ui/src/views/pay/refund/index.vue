<template>
  <doc-alert
    :title="t('auto.views.pay.refund.index.ka8355392')"
    url="https://doc.iocoder.cn/pay/refund-demo/"
  />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item :label="t('auto.views.pay.refund.index.k396d9d78')" prop="appId">
        <el-select
          v-model="queryParams.appId"
          clearable
          :placeholder="t('auto.views.pay.refund.index.kb926b59e')"
          class="!w-240px"
        >
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.refund.index.k7917cc84')" prop="channelCode">
        <el-select
          v-model="queryParams.channelCode"
          :placeholder="t('auto.views.pay.refund.index.kc7b33d1a')"
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
      <el-form-item :label="t('auto.views.pay.refund.index.k9445bf49')" prop="merchantOrderId">
        <el-input
          v-model="queryParams.merchantOrderId"
          :placeholder="t('auto.views.pay.refund.index.k37e69683')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.refund.index.kac63f2e3')" prop="merchantRefundId">
        <el-input
          v-model="queryParams.merchantRefundId"
          :placeholder="t('auto.views.pay.refund.index.k755b4877')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.refund.index.k249a8a8d')" prop="channelOrderNo">
        <el-input
          v-model="queryParams.channelOrderNo"
          :placeholder="t('auto.views.pay.refund.index.kd76ae7e1')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.refund.index.k90b08b3a')" prop="channelRefundNo">
        <el-input
          v-model="queryParams.channelRefundNo"
          :placeholder="t('auto.views.pay.refund.index.k576fb7e8')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.refund.index.k7d1f4b47')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.pay.refund.index.k4d97c509')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.PAY_REFUND_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.pay.refund.index.k1f291968')"
          :end-placeholder="t('auto.views.pay.refund.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:tenant:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.kaa873de5') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.pay.refund.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.pay.refund.index.kb579703e')"
        align="center"
        prop="payPrice"
        width="100"
      >
        <template #default="scope">
          ￥{{ parseFloat(scope.row.payPrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="退款金额" align="center" prop="refundPrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.refundPrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="退款订单号" align="left" width="300">
        <template #default="scope">
          <p class="order-font">
            <el-tag size="small">商户</el-tag> {{ scope.row.merchantRefundId }}
          </p>
          <p class="order-font">
            <el-tag size="small" type="warning">退款</el-tag> {{ scope.row.no }}
          </p>
          <p class="order-font" v-if="scope.row.channelRefundNo">
            <el-tag size="small" type="success">渠道</el-tag> {{ scope.row.channelRefundNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="支付订单号" align="left" width="300">
        <template #default="scope">
          <p class="order-font">
            <el-tag size="small">商户</el-tag> {{ scope.row.merchantOrderId }}
          </p>
          <p class="order-font">
            <el-tag size="small" type="success">渠道</el-tag> {{ scope.row.channelOrderNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_REFUND_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="退款渠道" align="center" width="140">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
        label="成功时间"
        align="center"
        prop="successTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="支付应用" align="center" prop="successTime" width="100">
        <template #default="scope">
          <span>{{ scope.row.appName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="openDetail(scope.row.id)"
            v-hasPermi="['pay:order:query']"
          >
            详情
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

  <!-- 表单弹窗：预览 -->
  <RefundDetail ref="detailRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as RefundApi from '@/api/pay/refund'
import * as AppApi from '@/api/pay/app'
import RefundDetail from './RefundDetail.vue'
import download from '@/utils/download'
const { t } = useI18n()
defineOptions({ name: 'PayRefund' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表遮罩层
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  merchantId: undefined,
  appId: undefined,
  channelCode: undefined,
  merchantOrderId: undefined,
  merchantRefundId: undefined,
  status: undefined,
  payPrice: undefined,
  refundPrice: undefined,
  channelOrderNo: undefined,
  channelRefundNo: undefined,
  createTime: [],
  successTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出等待
const appList = ref([]) // 支付应用列表集合

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await RefundApi.getRefundPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await RefundApi.exportRefund(queryParams)
    download.excel(data, t('auto.views.pay.refund.index.k8e9584f0'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 预览详情 */
const detailRef = ref()
const openDetail = (id: number) => {
  detailRef.value.open(id)
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  appList.value = await AppApi.getAppList()
})
</script>
<style>
.order-font {
  padding: 2px 0;
  font-size: 12px;
}
</style>
