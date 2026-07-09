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
      <el-form-item :label="t('auto.views.mp.message.index.ke48fc0ee')" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
      <el-form-item :label="t('auto.views.mp.message.index.k15218877')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.mp.message.index.k48d631c6')"
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.MP_MESSAGE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.mp.message.index.k24afcd30')" prop="openid">
        <el-input
          v-model="queryParams.openid"
          :placeholder="t('auto.views.mp.message.index.kf8c5daed')"
          clearable
          :v-on="handleQuery"
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
          :start-placeholder="t('auto.views.mp.message.index.k1f291968')"
          :end-placeholder="t('auto.views.mp.message.index.kf4b9b2b5')"
          :default-time="['00:00:00', '23:59:59']"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />
          {{ t('extra.k4ec05ab7') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />
          {{ t('extra.kbaa9bdb4') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <MessageTable :list="list" :loading="loading" @send="handleSend" />
    <Pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 发送消息的弹窗 -->
  <el-dialog
    :title="t('auto.views.mp.message.index.k773af35a')"
    v-model="messageBox.show"
    @click="messageBox.show = true"
    width="50%"
    destroy-on-close
  >
    <WxMsg :user-id="messageBox.userId" />
  </el-dialog>
</template>
<script lang="ts" setup>
import * as MpMessageApi from '@/api/mp/message'
import WxMsg from '@/views/mp/components/wx-msg'
import WxAccountSelect from '@/views/mp/components/wx-account-select'
import MessageTable from './MessageTable.vue'
import { DICT_TYPE, getStrDictOptions } from '@/utils/dict'
import { MsgType } from '@/views/mp/components/wx-msg/types'
import type { FormInstance } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'MpMessage' })

const loading = ref(false)
const total = ref(0) // 数据的总页数
const list = ref<any[]>([]) // 当前页的列表数据

// 搜索参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  openid: '',
  accountId: -1,
  type: MsgType.Text,
  createTime: []
})
const queryFormRef = ref<FormInstance | null>(null) // 搜索的表单

// 消息对话框
const messageBox = reactive({
  show: false,
  userId: 0
})

/** 侦听accountId */
const onAccountChanged = (id: number) => {
  queryParams.accountId = id
  queryParams.pageNum = 1
  handleQuery()
}

/** 查询列表 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const getList = async () => {
  try {
    loading.value = true
    const data = await MpMessageApi.getMessagePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = async () => {
  // 暂存 accountId，并在 reset 后恢复
  const accountId = queryParams.accountId
  queryFormRef.value?.resetFields()
  queryParams.accountId = accountId
  handleQuery()
}

/** 打开消息发送窗口 */
const handleSend = async (userId: string) => {
  messageBox.userId = userId
  messageBox.show = true
}
</script>
