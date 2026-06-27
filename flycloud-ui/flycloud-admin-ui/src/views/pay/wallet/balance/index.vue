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
      <el-form-item :label="t('auto.views.pay.wallet.balance.index.kec750ef6')" prop="userId">
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.pay.wallet.balance.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.wallet.balance.index.k31ab92d1')" prop="userType">
        <el-select
          v-model="queryParams.userType"
          :placeholder="t('auto.views.pay.wallet.balance.index.k8d91841e')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
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
          :start-placeholder="t('auto.views.pay.wallet.balance.index.k1f291968')"
          :end-placeholder="t('auto.views.pay.wallet.balance.index.kf4b9b2b5')"
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
        :label="t('auto.views.pay.wallet.balance.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.pay.wallet.balance.index.kec750ef6')"
        align="center"
        prop="userId"
      />
      <el-table-column
        :label="t('auto.views.pay.wallet.balance.index.k31ab92d1')"
        align="center"
        prop="userType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column label="余额" align="center" prop="balance">
        <template #default="{ row }"> {{ fenToYuan(row.balance) }} 元</template>
      </el-table-column>
      <el-table-column label="累计支出" align="center" prop="totalExpense">
        <template #default="{ row }"> {{ fenToYuan(row.totalExpense) }} 元</template>
      </el-table-column>
      <el-table-column label="累计充值" align="center" prop="totalRecharge">
        <template #default="{ row }"> {{ fenToYuan(row.totalRecharge) }} 元</template>
      </el-table-column>
      <el-table-column label="冻结金额" align="center" prop="freezePrice">
        <template #default="{ row }"> {{ fenToYuan(row.freezePrice) }} 元</template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="openForm(scope.row.id)">详情</el-button>
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

  <!-- 弹窗 -->
  <WalletForm ref="formRef" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { fenToYuan } from '@/utils'
import * as WalletApi from '@/api/pay/wallet/balance'
import WalletForm from './WalletForm.vue'
const { t } = useI18n()
defineOptions({ name: 'WalletBalance' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  userType: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await WalletApi.getWalletPage(queryParams)
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
const openForm = (id?: number) => {
  formRef.value.open(id)
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
