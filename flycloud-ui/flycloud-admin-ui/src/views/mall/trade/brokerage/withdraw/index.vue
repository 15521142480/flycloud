<template>
  <doc-alert
    :title="t('auto.views.mall.trade.brokerage.withdraw.index.k50f5e3f4')"
    url="https://doc.iocoder.cn/mall/trade-brokerage/"
  />

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
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.kec750ef6')"
        prop="userId"
      >
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.ke756194f')"
        prop="type"
      >
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.ke4bc442b')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_WITHDRAW_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.k90138491')"
        prop="accountNo"
      >
        <el-input
          v-model="queryParams.accountNo"
          :placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.kdfcc8446')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.k641a9208')"
        prop="bankName"
      >
        <el-select
          v-model="queryParams.bankName"
          :placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.kadfde72c')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.BROKERAGE_BANK_NAME)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.kdba277df')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_WITHDRAW_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.ke85ad6ea')"
        prop="createTime"
      >
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.k1f291968')"
          :end-placeholder="t('auto.views.mall.trade.brokerage.withdraw.index.kf4b9b2b5')"
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
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.k9f42dac6')"
        align="left"
        prop="id"
        min-width="60px"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.brokerage.withdraw.index.k55c26aba')"
        align="left"
        min-width="120px"
      >
        <template #default="scope">
          <div>{{ t('extra.k35a4edf3', { p0: scope.row.userId }) }}</div>
          <div>{{ t('extra.k0b53584e', { p0: scope.row.userNickname }) }}</div>
        </template>
      </el-table-column>
      <el-table-column label="提现金额" align="left" prop="price" min-width="80px">
        <template #default="scope">
          <div>金　额：￥{{ fenToYuan(scope.row.price) }}</div>
          <div>手续费：￥{{ fenToYuan(scope.row.feePrice) }}</div>
        </template>
      </el-table-column>
      <el-table-column label="提现方式" align="left" prop="type" min-width="120px">
        <template #default="scope">
          <div v-if="scope.row.type === BrokerageWithdrawTypeEnum.WALLET.type"> 余额 </div>
          <div v-else>
            {{ getDictLabel(DICT_TYPE.BROKERAGE_WITHDRAW_TYPE, scope.row.type) }}
            <span v-if="scope.row.accountNo">账号：{{ scope.row.accountNo }}</span>
          </div>
          <template v-if="scope.row.type === BrokerageWithdrawTypeEnum.BANK.type">
            <div>真实姓名：{{ scope.row.name }}</div>
            <div>
              银行名称：
              <dict-tag :type="DICT_TYPE.BROKERAGE_BANK_NAME" :value="scope.row.bankName" />
            </div>
            <div>开户地址：{{ scope.row.bankAddress }}</div>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="收款码" align="left" prop="accountQrCodeUrl" min-width="70px">
        <template #default="scope">
          <el-image
            v-if="scope.row.accountQrCodeUrl"
            :src="scope.row.accountQrCodeUrl"
            class="h-40px w-40px"
            :preview-src-list="[scope.row.accountQrCodeUrl]"
            preview-teleported
          />
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请时间"
        align="left"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="备注" align="left" prop="remark" />
      <el-table-column label="状态" align="left" prop="status" min-width="120px">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BROKERAGE_WITHDRAW_STATUS" :value="scope.row.status" />
          <div v-if="scope.row.auditTime" class="text-xs">
            时间：{{ formatDate(scope.row.auditTime) }}
          </div>
          <div v-if="scope.row.auditReason" class="text-xs">
            原因：{{ scope.row.auditReason }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="left" width="110px" fixed="right">
        <template #default="scope">
          <template v-if="scope.row.status === BrokerageWithdrawStatusEnum.AUDITING.status">
            <el-button
              link
              type="primary"
              @click="handleApprove(scope.row.id)"
              v-hasPermi="['trade:brokerage-withdraw:audit']"
            >
              通过
            </el-button>
            <el-button
              link
              type="danger"
              @click="openForm(scope.row.id)"
              v-hasPermi="['trade:brokerage-withdraw:audit']"
            >
              驳回
            </el-button>
          </template>
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
  <BrokerageWithdrawRejectForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { DICT_TYPE, getDictLabel, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { dateFormatter, formatDate } from '@/utils/formatTime'
import * as BrokerageWithdrawApi from '@/api/mall/trade/brokerage/withdraw'
import BrokerageWithdrawRejectForm from './BrokerageWithdrawRejectForm.vue'
import { BrokerageWithdrawStatusEnum, BrokerageWithdrawTypeEnum } from '@/utils/constants'
import { fenToYuanFormat } from '@/utils/formatter'
import { fenToYuan } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'BrokerageWithdraw' })
const message = useMessage() // 消息弹窗
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  type: null,
  name: null,
  accountNo: null,
  bankName: null,
  status: null,
  auditReason: null,
  auditTime: [],
  remark: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await BrokerageWithdrawApi.getBrokerageWithdrawPage(queryParams)
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
const openForm = (id: number) => {
  formRef.value.open(id)
}

/** 审核通过 */
const handleApprove = async (id: number) => {
  try {
    loading.value = true
    await message.confirm(t('auto.views.mall.trade.brokerage.withdraw.index.k2aeb21c7'))
    await BrokerageWithdrawApi.approveBrokerageWithdraw(id)
    await message.success(t('common.success'))
    await getList()
  } finally {
    loading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
