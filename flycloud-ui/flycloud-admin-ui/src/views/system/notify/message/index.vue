<template>
  <doc-alert
    :title="t('auto.views.system.notify.message.index.k182bebee')"
    url="https://doc.iocoder.cn/notify/"
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
      <el-form-item :label="t('auto.views.system.notify.message.index.kec750ef6')" prop="userId">
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.system.notify.message.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.notify.message.index.k31ab92d1')" prop="userType">
        <el-select
          v-model="queryParams.userType"
          :placeholder="t('auto.views.system.notify.message.index.k8d91841e')"
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
      <el-form-item
        :label="t('auto.views.system.notify.message.index.k5695a649')"
        prop="templateCode"
      >
        <el-input
          v-model="queryParams.templateCode"
          :placeholder="t('auto.views.system.notify.message.index.kefa5df04')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.notify.message.index.k653d8b5b')"
        prop="templateType"
      >
        <el-select
          v-model="queryParams.templateType"
          :placeholder="t('auto.views.system.notify.message.index.kb51a336c')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE)"
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
          :start-placeholder="t('auto.views.system.notify.message.index.k1f291968')"
          :end-placeholder="t('auto.views.system.notify.message.index.kf4b9b2b5')"
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
        :label="t('auto.views.system.notify.message.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.system.notify.message.index.k31ab92d1')"
        align="center"
        prop="userType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column :label="t('system.user.id')" align="center" prop="userId" width="80" />
      <el-table-column
        :label="t('auto.views.system.mail.log.log_data.k5695a649')"
        align="center"
        prop="templateCode"
        width="80"
      />
      <el-table-column
        :label="t('auto.views.system.mail.template.template_data.k008bf359')"
        align="center"
        prop="templateNickname"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k54ebbb0d')"
        align="center"
        prop="templateContent"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k59bcfa64')"
        align="center"
        prop="templateParams"
        width="180"
        show-overflow-tooltip
      >
        <template #default="scope"> {{ scope.row.templateParams }}</template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k653d8b5b')"
        align="center"
        prop="templateType"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="scope.row.templateType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.kbfc6b329')"
        align="center"
        prop="readStatus"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.readStatus" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k9af1a262')"
        align="center"
        prop="readTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column :label="t('common.operation')" align="center" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row)"
            v-hasPermi="['system:notify-message:query']"
          >
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
  </ContentWrap>

  <!-- 表单弹窗：详情 -->
  <NotifyMessageDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as NotifyMessageApi from '@/api/system/notify/message'
import NotifyMessageDetail from './NotifyMessageDetail.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemNotifyMessage' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userType: undefined,
  userId: undefined,
  templateCode: undefined,
  templateType: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await NotifyMessageApi.getNotifyMessagePage(queryParams)
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

/** 详情操作 */
const detailRef = ref()
const openDetail = (data: NotifyMessageApi.NotifyMessageVO) => {
  detailRef.value.open(data)
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
