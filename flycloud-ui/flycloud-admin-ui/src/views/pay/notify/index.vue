<template>
  <doc-alert
    :title="t('auto.views.pay.notify.index.kbacecf2d')"
    url="https://doc.iocoder.cn/pay/build/"
  />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item :label="t('auto.views.pay.notify.index.k396d9d78')" prop="appId">
        <el-select
          v-model="queryParams.appId"
          :placeholder="t('auto.views.pay.notify.index.kb926b59e')"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.notify.index.k1cf9ae8f')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.pay.notify.index.ka66c882c')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.PAY_NOTIFY_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.notify.index.k8913e0a8')" prop="dataId">
        <el-input
          v-model="queryParams.dataId"
          :placeholder="t('auto.views.pay.notify.index.ke9506984')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.notify.index.kcca88a70')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.pay.notify.index.kc818af38')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.PAY_NOTIFY_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.notify.index.kf51b03a6')" prop="merchantOrderId">
        <el-input
          v-model="queryParams.merchantOrderId"
          :placeholder="t('auto.views.pay.notify.index.k6b6fdbe7')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          style="width: 240px"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          range-separator="-"
          :start-placeholder="t('auto.views.pay.notify.index.k1f291968')"
          :end-placeholder="t('auto.views.pay.notify.index.kf4b9b2b5')"
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
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.pay.notify.index.k017af56c')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.pay.notify.index.k396d9d78')"
        align="center"
        prop="appName"
      />
      <el-table-column
        :label="t('auto.views.pay.notify.index.kf51b03a6')"
        align="center"
        prop="merchantOrderId"
      />
      <el-table-column
        :label="t('auto.views.pay.notify.index.k1cf9ae8f')"
        align="center"
        prop="type"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_NOTIFY_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.pay.notify.NotifyDetail.k8913e0a8')"
        align="center"
        prop="dataId"
      />
      <el-table-column
        :label="t('auto.views.pay.notify.NotifyDetail.kcca88a70')"
        align="center"
        prop="status"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_NOTIFY_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.pay.notify.NotifyDetail.ka8943cc4')"
        align="center"
        prop="lastExecuteTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.pay.notify.NotifyDetail.kf9dac926')"
        align="center"
        prop="nextNotifyTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.pay.notify.NotifyDetail.kec63d1a8')"
        align="center"
        prop="notifyTimes"
      >
        <template #default="scope">
          <el-tag size="small" type="success">
            {{ scope.row.notifyTimes }} / {{ scope.row.maxNotifyTimes }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row.id)"
            v-hasPermi="['pay:notify:query']"
          >
            {{ t('extra.k5b48dbb8') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：预览 -->
  <NotifyDetail ref="detailRef" />
</template>

<script lang="ts" setup>
import * as PayNotifyApi from '@/api/pay/notify'
import * as PayAppApi from '@/api/pay/app'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import NotifyDetail from './NotifyDetail.vue'
const { t } = useI18n()
defineOptions({ name: 'PayNotify' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref() // 列表的数据
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  appId: null,
  type: null,
  dataId: null,
  status: null,
  merchantOrderId: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const appList = ref([]) // 支付应用列表集合
// 是否显示弹出层
const open = ref(false)
// 通知详情
const notifyDetail = ref<any>({
  logs: []
})

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await PayNotifyApi.getNotifyTaskPage(queryParams.value)
    list.value = data.list
    total.value = data.total
    loading.value = false
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 详情按钮操作 */
const detailRef = ref()
const openDetail = (id: number) => {
  detailRef.value.open(id)
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 获得筛选项
  appList.value = await PayAppApi.getAppList()
})
</script>
