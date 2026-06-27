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
      <el-form-item :label="t('auto.views.ai.write.manager.index.kec750ef6')" prop="userId">
        <el-select
          v-model="queryParams.userId"
          clearable
          :placeholder="t('auto.views.ai.write.manager.index.kb719fb8a')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.write.manager.index.k2e533a22')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.ai.write.manager.index.kd22a1228')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.AI_WRITE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.write.manager.index.ke4b9d694')" prop="platform">
        <el-select
          v-model="queryParams.platform"
          :placeholder="t('auto.views.ai.write.manager.index.ke8fbea8b')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.AI_PLATFORM)"
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
          :start-placeholder="t('auto.views.ai.write.manager.index.k1f291968')"
          :end-placeholder="t('auto.views.ai.write.manager.index.kf4b9b2b5')"
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
          v-hasPermi="['ai:write:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k632d8e98') }}
        </el-button>
        <!-- TODO @YunaiV  目前没有导出接口，需要导出吗 -->
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['ai:write:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k3686a969') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.ai.write.manager.index.k9f42dac6')"
        align="center"
        prop="id"
        width="120"
        fixed="left"
      />
      <el-table-column
        :label="t('auto.views.ai.write.manager.index.k9ba763ea')"
        align="center"
        prop="userId"
        width="180"
      >
        <template #default="scope">
          <span>{{ userList.find((item) => item.id === scope.row.userId)?.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="写作类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_WRITE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="平台" align="center" prop="platform" width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_PLATFORM" :value="scope.row.platform" />
        </template>
      </el-table-column>
      <el-table-column label="模型" align="center" prop="model" width="180" />
      <el-table-column
        label="生成内容提示"
        align="center"
        prop="prompt"
        width="180"
        show-overflow-tooltip
      />
      <el-table-column label="生成的内容" align="center" prop="generatedContent" width="180" />
      <el-table-column label="原文" align="center" prop="originalContent" width="180" />
      <el-table-column label="长度" align="center" prop="length">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_WRITE_LENGTH" :value="scope.row.length" />
        </template>
      </el-table-column>
      <el-table-column label="格式" align="center" prop="format">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_WRITE_FORMAT" :value="scope.row.format" />
        </template>
      </el-table-column>
      <el-table-column label="语气" align="center" prop="tone">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_WRITE_TONE" :value="scope.row.tone" />
        </template>
      </el-table-column>
      <el-table-column label="语言" align="center" prop="language">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.AI_WRITE_LANGUAGE" :value="scope.row.language" />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="错误信息" align="center" prop="errorMessage" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <!--          TODO @YunaiV 目前没有修改接口，写作要可以更改吗-->
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['ai:write:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['ai:write:delete']"
          >
            删除
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
</template>

<script setup lang="ts">
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { useRouter } from 'vue-router'
import { WriteApi, AiWritePageReqVO, AiWriteRespVo } from '@/api/ai/write'
import * as UserApi from '@/api/system/user'

/** AI 写作列表 */
const { t } = useI18n()
defineOptions({ name: 'AiWriteManager' })

const message = useMessage() // 消息弹窗
const router = useRouter() // 路由

const loading = ref(true) // 列表的加载中
const list = ref<AiWriteRespVo[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive<AiWritePageReqVO>({
  pageNum: 1,
  pageSize: 10,
  userId: undefined,
  type: undefined,
  platform: undefined,
  createTime: undefined
})
const queryFormRef = ref() // 搜索的表单
const userList = ref<UserApi.UserVO[]>([]) // 用户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await WriteApi.getWritePage(queryParams)
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

/** 新增方法，跳转到写作页面 **/
const openForm = (type: string, id?: number) => {
  switch (type) {
    case 'create':
      router.push('/ai/write')
      break
  }
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await WriteApi.deleteWrite(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(async () => {
  getList()
  // 获得用户列表
  userList.value = await UserApi.getSimpleUserList()
})
</script>
