<template>
  <doc-alert
    :title="t('auto.views.system.sms.template.index.k7168c04b')"
    url="https://doc.iocoder.cn/sms/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="150px"
    >
      <el-form-item :label="t('auto.views.system.sms.template.index.k7599547f')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.system.sms.template.index.k4c7b42da')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.template.index.k6bbda1b1')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.system.sms.template.index.k312f5310')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.template.index.k5695a649')" prop="code">
        <el-input
          v-model="queryParams.code"
          :placeholder="t('auto.views.system.sms.template.index.kefa5df04')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.sms.template.index.kb7e5b1c6')"
        prop="apiTemplateId"
      >
        <el-input
          v-model="queryParams.apiTemplateId"
          :placeholder="t('auto.views.system.sms.template.index.k7edd038f')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.template.index.k7575f737')" prop="channelId">
        <el-select
          v-model="queryParams.channelId"
          :placeholder="t('auto.views.system.sms.template.index.k00492a8c')"
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
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          style="width: 240px"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
          :start-placeholder="t('auto.views.system.sms.template.index.k1f291968')"
          :end-placeholder="t('auto.views.system.sms.template.index.kf4b9b2b5')"
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
          v-hasPermi="['system:sms-template:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" />{{ t('extra.k95792bc9') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:sms-template:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.kf58b26fe') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.sms.template.index.k5695a649')"
        align="center"
        prop="code"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('auto.views.system.sms.template.index.kbbc511d0')"
        align="center"
        prop="name"
        width="120"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('auto.views.system.sms.template.index.kdc362463')"
        align="center"
        prop="content"
        width="200"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('auto.views.system.sms.template.index.k7599547f')"
        align="center"
        prop="type"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.status')" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.remark')" align="center" prop="remark" />
      <el-table-column
        :label="t('auto.views.system.sms.template.index.kb7e5b1c6')"
        align="center"
        prop="apiTemplateId"
        width="200"
        :show-overflow-tooltip="true"
      />
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
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column :label="t('common.operation')" align="center" width="210" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['system:sms-template:update']"
          >
            {{ t('extra.k4c512392') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="openSendForm(scope.row.id)"
            v-hasPermi="['system:sms-template:send-sms']"
          >
            {{ t('action.test') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['system:sms-template:delete']"
          >
            {{ t('common.delete') }}
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
  <SmsTemplateForm ref="formRef" @success="getList" />
  <!-- 表单弹窗：测试发送 -->
  <SmsTemplateSendForm ref="sendFormRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions, getDictLabel } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as SmsTemplateApi from '@/api/system/sms/smsTemplate'
import * as SmsChannelApi from '@/api/system/sms/smsChannel'
import download from '@/utils/download'
import SmsTemplateForm from './SmsTemplateForm.vue'
import SmsTemplateSendForm from './SmsTemplateSendForm.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemSmsTemplate' })

const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  type: undefined,
  status: undefined,
  code: '',
  content: '',
  apiTemplateId: '',
  channelId: undefined,
  createTime: []
})
const exportLoading = ref(false) // 导出的加载中
const channelList = ref<SmsChannelApi.SmsChannelVO[]>([]) // 短信渠道列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SmsTemplateApi.getSmsTemplatePage(queryParams)
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

/** 发送短信按钮 */
const sendFormRef = ref()
const openSendForm = (id: number) => {
  sendFormRef.value.open(id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await SmsTemplateApi.deleteSmsTemplate(id)
    message.success(t('common.delSuccess'))
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
    const data = await SmsTemplateApi.exportSmsTemplate(queryParams)
    download.excel(data, t('auto.views.system.sms.template.index.k7cab7b6c'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载渠道列表
  channelList.value = await SmsChannelApi.getSimpleSmsChannelList()
})
</script>
