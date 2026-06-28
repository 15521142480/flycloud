<template>
  <doc-alert
    :title="t('auto.views.mall.trade.brokerage.record.index.k50f5e3f4')"
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
        :label="t('auto.views.mall.trade.brokerage.record.index.kec750ef6')"
        prop="userId"
      >
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.mall.trade.brokerage.record.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.brokerage.record.index.k2268f2d5')"
        prop="bizType"
      >
        <el-select
          v-model="queryParams.bizType"
          :placeholder="t('auto.views.mall.trade.brokerage.record.index.kb6423b8b')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_RECORD_BIZ_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.mall.trade.brokerage.record.index.kdba277df')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_RECORD_STATUS)"
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
          :start-placeholder="t('auto.views.mall.trade.brokerage.record.index.k1f291968')"
          :end-placeholder="t('auto.views.mall.trade.brokerage.record.index.kf4b9b2b5')"
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
        :label="t('auto.views.mall.trade.brokerage.record.index.k9f42dac6')"
        align="center"
        prop="id"
        min-width="60"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.brokerage.record.index.kec750ef6')"
        align="center"
        prop="userId"
        min-width="80"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.brokerage.record.index.k4ceeeb31')"
        align="center"
        prop="userAvatar"
        width="70px"
      >
        <template #default="scope">
          <el-avatar :src="scope.row.userAvatar" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('system.user.nickname')"
        align="center"
        prop="userNickname"
        min-width="80px"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.brokerage.record.index.k2268f2d5')"
        align="center"
        prop="bizType"
        min-width="85"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BROKERAGE_RECORD_BIZ_TYPE" :value="scope.row.bizType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.operatelog.OperateLogDetail.k9c5f5763')"
        align="center"
        prop="bizId"
        min-width="80"
      />
      <el-table-column :label="t('table.title')" align="center" prop="title" min-width="110" />
      <el-table-column
        :label="t('auto.views.erp.home.components.TimeSummaryChart.k34943c40')"
        align="center"
        prop="price"
        min-width="60"
        :formatter="fenToYuanFormat"
      />
      <el-table-column
        :label="t('extra.kf411d0f1')"
        align="center"
        prop="description"
        min-width="120"
      />
      <el-table-column :label="t('common.status')" align="center" prop="status" min-width="85">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BROKERAGE_RECORD_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k80d0e2aa')"
        align="center"
        prop="unfreezeTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>

<script setup lang="ts">
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as BrokerageRecordApi from '@/api/mall/trade/brokerage/record'
import { fenToYuanFormat } from '@/utils/formatter'
const { t } = useI18n()
defineOptions({ name: 'TradeBrokerageRecord' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  bizType: null,
  price: null,
  status: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await BrokerageRecordApi.getBrokerageRecordPage(queryParams)
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
