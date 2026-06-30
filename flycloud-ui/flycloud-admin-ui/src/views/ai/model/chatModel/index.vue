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
      <el-form-item :label="t('auto.views.ai.model.chatModel.index.ka11691fb')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.ai.model.chatModel.index.k02d5516c')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatModel.index.k3a818387')" prop="model">
        <el-input
          v-model="queryParams.model"
          :placeholder="t('auto.views.ai.model.chatModel.index.kf34bb3b2')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatModel.index.kc5e0b6d6')" prop="platform">
        <el-input
          v-model="queryParams.platform"
          :placeholder="t('auto.views.ai.model.chatModel.index.k816470ea')"
          clearable
          @keyup.enter="handleQuery"
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
          v-hasPermi="['ai:chat-model:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kf201d37c') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.ai.model.chatModel.index.k275b3ed8')"
        align="center"
        prop="platform"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_PLATFORM" :value="scope.row.platform" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.ka11691fb')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k3a818387')"
        align="center"
        prop="model"
      />
      <el-table-column
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k0b2b2cbe')"
        align="center"
        prop="keyId"
        min-width="140"
      >
        <template #default="scope">
          <span>{{ apiKeyList.find((item) => item.id === scope.row.keyId)?.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.sort')" align="center" prop="sort" />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.chat.index.components.conversation.k80a0d1e0')"
        align="center"
        prop="temperature"
      />
      <el-table-column
        :label="t('auto.views.ai.chat.index.components.conversation.kd27629ce')"
        align="center"
        prop="maxTokens"
        min-width="140"
      />
      <el-table-column
        :label="t('auto.views.ai.chat.index.components.conversation.k3f4045aa')"
        align="center"
        prop="maxContexts"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['ai:chat-model:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['ai:chat-model:delete']"
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
  <ChatModelForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { ChatModelApi, ChatModelVO } from '@/api/ai/model/chatModel'
import ChatModelForm from './ChatModelForm.vue'
import { DICT_TYPE } from '@/utils/dict'
import { ApiKeyApi, ApiKeyVO } from '@/api/ai/model/apiKey'

/** API 聊天模型 列表 */
const { t } = useI18n()
defineOptions({ name: 'AiChatModel' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<ChatModelVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  model: undefined,
  platform: undefined
})
const queryFormRef = ref() // 搜索的表单
const apiKeyList = ref([] as ApiKeyVO[]) // API 密钥列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ChatModelApi.getChatModelPage(queryParams)
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

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ChatModelApi.deleteChatModel(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(async () => {
  getList()
  // 获得下拉数据
  apiKeyList.value = await ApiKeyApi.getApiKeySimpleList()
})
</script>
