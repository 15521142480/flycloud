<template>
  <doc-alert
    :title="t('auto.views.system.sms.channel.index.k7168c04b')"
    url="https://doc.iocoder.cn/sms/"
  />

  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.system.sms.channel.index.kf57fae84')" prop="signature">
        <el-input
          v-model="queryParams.signature"
          :placeholder="t('auto.views.system.sms.channel.index.k41738330')"
          clearable
          class="!w-240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.channel.index.k5691b379')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.system.sms.channel.index.kcff6b1c1')"
          class="!w-240px"
          clearable
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
          :start-placeholder="t('auto.views.system.sms.channel.index.k1f291968')"
          :end-placeholder="t('auto.views.system.sms.channel.index.kf4b9b2b5')"
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
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['system:sms-channel:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('common.create') }}</el-button
        >
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.sms.channel.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.system.sms.channel.index.kf57fae84')"
        align="center"
        prop="signature"
      />
      <el-table-column
        :label="t('auto.views.system.sms.channel.index.k82233330')"
        align="center"
        prop="code"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.code" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.sms.channel.SmsChannelForm.k5691b379')"
        align="center"
        prop="status"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.remark')"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('auto.views.system.sms.channel.SmsChannelForm.k4df60a58')"
        align="center"
        prop="apiKey"
        :show-overflow-tooltip="true"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.system.sms.channel.SmsChannelForm.kae001512')"
        align="center"
        prop="apiSecret"
        :show-overflow-tooltip="true"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.system.sms.channel.SmsChannelForm.kbe9e0cf3')"
        align="center"
        prop="callbackUrl"
        :show-overflow-tooltip="true"
        width="180"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['system:sms-channel:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['system:sms-channel:delete']"
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
  <SmsChannelForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as SmsChannelApi from '@/api/system/sms/smsChannel'
import SmsChannelForm from './SmsChannelForm.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemSmsChannel' })
const message = useMessage() // 消息弹窗

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryFormRef = ref() // 搜索的表单
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  signature: undefined,
  status: undefined,
  createTime: []
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SmsChannelApi.getSmsChannelPage(queryParams)
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
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: string) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await SmsChannelApi.deleteSmsChannel(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
