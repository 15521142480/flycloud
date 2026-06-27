<template>
  <doc-alert
    :title="t('auto.views.mp.user.index.kfb1bfabc')"
    url="https://doc.iocoder.cn/mp/user/"
  />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.mp.user.index.ke48fc0ee')" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
      <el-form-item :label="t('auto.views.mp.user.index.k24afcd30')" prop="openid">
        <el-input
          v-model="queryParams.openid"
          :placeholder="t('auto.views.mp.user.index.kf8c5daed')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.mp.user.index.k25124ed7')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.mp.user.index.k5dbe6b07')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" />{{ t('common.search') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" />{{ t('common.reset') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleSync"
          v-hasPermi="['mp:user:sync']"
          :disabled="queryParams.accountId === 0"
        >
          <Icon icon="ep:refresh" class="mr-5px" /> {{ t('extra.k386c777d') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column :label="t('auto.views.mp.user.index.k9f42dac6')" align="center" prop="id" />
      <el-table-column
        :label="t('auto.views.mp.user.index.k24afcd30')"
        align="center"
        prop="openid"
        width="260"
      />
      <el-table-column
        :label="t('auto.views.mp.user.index.k25124ed7')"
        align="center"
        prop="name"
      />
      <el-table-column :label="t('common.remark')" align="center" prop="remark" />
      <el-table-column
        :label="t('auto.views.mp.user.index.kae0a7afe')"
        align="center"
        prop="tagIds"
        width="200"
      >
        <template #default="scope">
          <span v-for="(tagId, index) in scope.row.tagIds" :key="index">
            <el-tag>{{ tagList.find((tag) => tag.tagId === tagId)?.name }} </el-tag>&nbsp;
          </span>
        </template>
      </el-table-column>
      <el-table-column label="订阅状态" align="center" prop="subscribeStatus">
        <template #default="scope">
          <el-tag v-if="scope.row.subscribeStatus === 0" type="success">已订阅</el-tag>
          <el-tag v-else type="danger">未订阅</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="订阅时间"
        align="center"
        prop="subscribeTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="openForm(scope.row.id)"
            v-hasPermi="['mp:user:update']"
          >
            修改
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

  <!-- 表单弹窗：修改 -->
  <UserForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as MpUserApi from '@/api/mp/user'
import * as MpTagApi from '@/api/mp/tag'
import WxAccountSelect from '@/views/mp/components/wx-account-select'
import type { FormInstance } from 'element-plus'
import UserForm from './UserForm.vue'
const { t } = useI18n()
defineOptions({ name: 'MpUser' })

const message = useMessage() // 消息

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref<any[]>([]) // 列表的数据

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  accountId: -1,
  openid: '',
  name: ''
})
const queryFormRef = ref<FormInstance | null>(null) // 搜索的表单
const tagList = ref<any[]>([]) // 公众号标签列表

/** 侦听公众号变化 **/
const onAccountChanged = (id: number) => {
  queryParams.accountId = id
  queryParams.pageNum = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  try {
    loading.value = true
    const data = await MpUserApi.getUserPage(queryParams)
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
  const accountId = queryParams.accountId
  queryFormRef.value?.resetFields()
  queryParams.accountId = accountId
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref<InstanceType<typeof UserForm> | null>(null)
const openForm = (id: number) => {
  formRef.value?.open(id)
}

/** 同步标签 */
const handleSync = async () => {
  try {
    await message.confirm(t('auto.views.mp.user.index.k217ad3b7'))
    await MpUserApi.syncUser(queryParams.accountId)
    message.success(t('auto.views.mp.user.index.ke0f4231f'))
    await getList()
  } catch {}
}

/** 初始化 */
onMounted(async () => {
  tagList.value = await MpTagApi.getSimpleTagList()
})
</script>
