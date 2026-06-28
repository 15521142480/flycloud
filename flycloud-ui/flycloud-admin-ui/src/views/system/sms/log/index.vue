<template>
  <doc-alert
    :title="t('auto.views.system.sms.log.index.k7168c04b')"
    url="https://doc.iocoder.cn/sms/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item :label="t('auto.views.system.sms.log.index.k5a9cc5e8')" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          :placeholder="t('auto.views.system.sms.log.index.k5ecce333')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.log.index.k7575f737')" prop="channelId">
        <el-select
          v-model="queryParams.channelId"
          :placeholder="t('auto.views.system.sms.log.index.k00492a8c')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="channel in channelList"
            :key="channel.id"
            :value="channel.id"
            :label="
              channel.signature +
              `【 ${getDictLabel(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE, channel.code)}】`
            "
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.log.index.k3fa1ce06')" prop="templateId">
        <el-input
          v-model="queryParams.templateId"
          :placeholder="t('auto.views.system.sms.log.index.k52848084')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.log.index.k9fa54101')" prop="sendStatus">
        <el-select
          v-model="queryParams.sendStatus"
          :placeholder="t('auto.views.system.sms.log.index.kcd9518bd')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_SEND_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.log.index.k98c64dd6')" prop="sendTime">
        <el-date-picker
          v-model="queryParams.sendTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.system.sms.log.index.k1f291968')"
          :end-placeholder="t('auto.views.system.sms.log.index.kf4b9b2b5')"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.log.index.k6eafa91f')" prop="receiveStatus">
        <el-select
          v-model="queryParams.receiveStatus"
          :placeholder="t('auto.views.system.sms.log.index.k51323de7')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.log.index.k407c9467')" prop="receiveTime">
        <el-date-picker
          v-model="queryParams.receiveTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.system.sms.log.index.k1f291968')"
          :end-placeholder="t('auto.views.system.sms.log.index.kf4b9b2b5')"
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
          v-hasPermi="['system:sms-log:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.kac7ffd2f') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.sms.log.index.k9f42dac6')"
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
        :label="t('auto.views.system.sms.log.index.k5a9cc5e8')"
        align="center"
        prop="mobile"
        width="120"
      >
        <template #default="scope">
          <div>{{ scope.row.mobile }}</div>
          <div v-if="scope.row.userType && scope.row.userId">
            <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
            {{ '(' + scope.row.userId + ')' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.sms.log.SmsLogDetail.k687fd112')"
        align="center"
        prop="templateContent"
        width="300"
      />
      <el-table-column
        :label="t('auto.views.system.mail.log.log_data.k9fa54101')"
        align="center"
        width="180"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_SEND_STATUS" :value="scope.row.sendStatus" />
          <div>{{ formatDate(scope.row.sendTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.sms.log.index.k6eafa91f')"
        align="center"
        width="180"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS" :value="scope.row.receiveStatus" />
          <div>{{ formatDate(scope.row.receiveTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.sms.log.SmsLogDetail.k7575f737')"
        align="center"
        width="120"
      >
        <template #default="scope">
          <div>
            {{ channelList.find((channel) => channel.id === scope.row.channelId)?.signature }}
          </div>
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.mail.log.log_data.k3fa1ce06')"
        align="center"
        prop="templateId"
      />
      <el-table-column
        :label="t('auto.views.system.sms.template.SmsTemplateForm.k7599547f')"
        align="center"
        prop="templateType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="scope.row.templateType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.operation')"
        align="center"
        fixed="right"
        class-name="fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row)"
            v-hasPermi="['system:sms-log:query']"
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
  <SmsLogDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions, getDictLabel } from '@/utils/dict'
import { dateFormatter, formatDate } from '@/utils/formatTime'
import download from '@/utils/download'
import * as SmsChannelApi from '@/api/system/sms/smsChannel'
import * as SmsLogApi from '@/api/system/sms/smsLog'
import SmsLogDetail from './SmsLogDetail.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemSmsLog' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  channelId: null,
  templateId: null,
  mobile: '',
  sendStatus: null,
  receiveStatus: null,
  sendTime: [],
  receiveTime: []
})
const exportLoading = ref(false) // 导出的加载中
const channelList = ref([]) // 短信渠道列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SmsLogApi.getSmsLogPage(queryParams)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await SmsLogApi.exportSmsLog(queryParams)
    download.excel(data, t('auto.views.system.sms.log.index.k678d02b5'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 详情操作 */
const detailRef = ref()
const openDetail = (data: SmsLogApi.SmsLogVO) => {
  detailRef.value.open(data)
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载渠道列表
  channelList.value = await SmsChannelApi.getSimpleSmsChannelList()
})
</script>
