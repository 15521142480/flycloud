<template>
  <doc-alert
    :title="t('auto.views.pay.order.index.k1637286a')"
    url="https://doc.iocoder.cn/pay/alipay-pay-demo/"
  />
  <doc-alert
    :title="t('auto.views.pay.order.index.k5028fc5c')"
    url="https://doc.iocoder.cn/pay/wx-pub-pay-demo/"
  />
  <doc-alert
    :title="t('auto.views.pay.order.index.k6cbc9bd1')"
    url="https://doc.iocoder.cn/pay/wx-lite-pay-demo/"
  />

  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item :label="t('auto.views.pay.order.index.k396d9d78')" prop="appId">
        <el-select
          clearable
          v-model="queryParams.appId"
          :placeholder="t('auto.views.pay.order.index.kb926b59e')"
          class="!w-240px"
        >
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.order.index.k88b37342')" prop="channelCode">
        <el-select
          v-model="queryParams.channelCode"
          :placeholder="t('auto.views.pay.order.index.kf9bee2a2')"
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
      <el-form-item :label="t('auto.views.pay.order.index.k3dc2266a')" prop="merchantOrderId">
        <el-input
          v-model="queryParams.merchantOrderId"
          :placeholder="t('auto.views.pay.order.index.k7c0615c3')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.order.index.k956bb6be')" prop="no">
        <el-input
          v-model="queryParams.no"
          :placeholder="t('auto.views.pay.order.index.ka3e1d5db')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.order.index.ke551deaa')" prop="channelOrderNo">
        <el-input
          v-model="queryParams.channelOrderNo"
          :placeholder="t('auto.views.pay.order.index.k2836b261')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.order.index.kb9f11a33')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.pay.order.index.kd505bfd1')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.PAY_ORDER_STATUS)"
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
          :start-placeholder="t('auto.views.pay.order.index.k1f291968')"
          :end-placeholder="t('auto.views.pay.order.index.kf4b9b2b5')"
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
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:tenant:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.kbb762561') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.pay.order.index.k9f42dac6')"
        align="center"
        prop="id"
        width="80"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.pay.order.index.kb579703e')"
        align="center"
        prop="price"
        width="100"
      >
        <template #default="scope"> ￥{{ parseFloat(scope.row.price / 100).toFixed(2) }} </template>
      </el-table-column>
      <el-table-column label="退款金额" align="center" prop="refundPrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.refundPrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="手续金额" align="center" prop="channelFeePrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.channelFeePrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="left" width="300">
        <template #default="scope">
          <p class="order-font">
            <el-tag size="small"> 商户</el-tag> {{ scope.row.merchantOrderId }}
          </p>
          <p class="order-font" v-if="scope.row.no">
            <el-tag size="small" type="warning">支付</el-tag> {{ scope.row.no }}
          </p>
          <p class="order-font" v-if="scope.row.channelOrderNo">
            <el-tag size="small" type="success">渠道</el-tag> {{ scope.row.channelOrderNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_ORDER_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="支付渠道" align="center" prop="channelCode" width="140">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
        label="支付时间"
        align="center"
        prop="successTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="支付应用" align="center" prop="appName" width="100" />
      <el-table-column label="商品标题" align="center" prop="subject" width="180" />
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
  <OrderDetail ref="detailRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as OrderApi from '@/api/pay/order'
import OrderDetail from './OrderDetail.vue'
import download from '@/utils/download'
const { t } = useI18n()
defineOptions({ name: 'PayOrder' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  appId: null,
  channelCode: null,
  merchantOrderId: null,
  channelOrderNo: null,
  no: null,
  status: null,
  createTime: []
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
    const data = await OrderApi.getOrderPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await OrderApi.exportOrder(queryParams)
    download.excel(data, t('auto.views.pay.order.index.k8e9584f0'))
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
})
</script>
<style>
.order-font {
  padding: 2px 0;
  font-size: 12px;
}
</style>
