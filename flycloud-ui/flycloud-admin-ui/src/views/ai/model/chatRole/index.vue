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
      <el-form-item :label="t('auto.views.ai.model.chatRole.index.k3aa1f085')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.ai.model.chatRole.index.kb7c17b9e')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatRole.index.kcda79ba6')" prop="category">
        <el-input
          v-model="queryParams.category"
          :placeholder="t('auto.views.ai.model.chatRole.index.k8343a95c')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatRole.index.k5857cbaf')" prop="publicStatus">
        <el-select
          v-model="queryParams.publicStatus"
          :placeholder="t('auto.views.ai.model.chatRole.index.k94b52191')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['ai:chat-role:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kd4e1f008') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.index.k3aa1f085')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.index.k5b7d951b')"
        align="center"
        prop="modelName"
      />
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.index.k9b7423ef')"
        align="center"
        prop="avatar"
      >
        <template #default="scope">
          <el-image :src="getFilePreviewUrl(scope?.row.avatar)" class="w-32px h-32px" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.kcda79ba6')"
        align="center"
        prop="category"
      />
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.ka0a62a00')"
        align="center"
        prop="description"
      />
      <el-table-column
        :label="t('auto.views.ai.chat.index.components.conversation.k338affe3')"
        align="center"
        prop="systemMessage"
      />
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k5857cbaf')"
        align="center"
        prop="publicStatus"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.publicStatus" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.kf86bd91f')"
        align="center"
        prop="sort"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['ai:chat-role:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['ai:chat-role:delete']"
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
  <ChatRoleForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
// @ts-nocheck
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
import { getBoolDictOptions, DICT_TYPE } from '@/utils/dict'
import { ChatRoleApi, ChatRoleVO } from '@/api/ai/model/chatRole'
import ChatRoleForm from './ChatRoleForm.vue'

/** AI 聊天角色 列表 */
const { t } = useI18n()
defineOptions({ name: 'AiChatRole' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<ChatRoleVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  category: undefined,
  publicStatus: true
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ChatRoleApi.getChatRolePage(queryParams)
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
    await ChatRoleApi.deleteChatRole(id)
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
