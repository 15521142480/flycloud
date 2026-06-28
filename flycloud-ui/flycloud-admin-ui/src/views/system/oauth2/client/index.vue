<template>
  <doc-alert
    :title="t('auto.views.system.oauth2.client.index.kcf78c257')"
    url="https://doc.iocoder.cn/oauth2/"
  />

  <!-- 搜索 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.system.oauth2.client.index.k6c19fe01')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.system.oauth2.client.index.k445d8859')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.system.oauth2.client.index.kdba277df')"
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
      <el-form-item>
        <el-button @click="handleQuery"
          ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
        >
        <el-button @click="resetQuery"
          ><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button
        >
        <el-button
          plain
          type="primary"
          @click="openForm('create')"
          v-hasPermi="['system:oauth2-client:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k7c50bf93') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.oauth2.client.index.k090a075b')"
        align="center"
        prop="clientId"
      />
      <el-table-column
        :label="t('auto.views.system.oauth2.client.index.k65f8d895')"
        align="center"
        prop="secret"
      />
      <el-table-column
        :label="t('auto.views.system.oauth2.client.index.k6c19fe01')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.system.oauth2.client.index.kf93d423d')"
        align="center"
        prop="logo"
      >
        <template #default="scope">
          <img width="40px" height="40px" :src="scope.row.logo" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.oauth2.client.ClientForm.kaa4907c8')"
        align="center"
        prop="accessTokenValiditySeconds"
      >
        <template #default="scope"
          >{{ scope.row.accessTokenValiditySeconds }}
          {{ t('auto.utils.formatTime.keb6aaba1') }}</template
        >
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.oauth2.client.ClientForm.k904a9d49')"
        align="center"
        prop="refreshTokenValiditySeconds"
      >
        <template #default="scope"
          >{{ scope.row.refreshTokenValiditySeconds }}
          {{ t('auto.utils.formatTime.keb6aaba1') }}</template
        >
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.oauth2.client.ClientForm.k3006e441')"
        align="center"
        prop="authorizedGrantTypes"
      >
        <template #default="scope">
          <el-tag
            :disable-transitions="true"
            :key="index"
            v-for="(authorizedGrantType, index) in scope.row.authorizedGrantTypes"
            :index="index"
            class="mr-5px"
          >
            {{ authorizedGrantType }}
          </el-tag>
        </template>
      </el-table-column>
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
            v-hasPermi="['system:oauth2-client:update']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['system:oauth2-client:delete']"
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
  <ClientForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as ClientApi from '@/api/system/oauth2/client'
import ClientForm from './ClientForm.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemOAuth2Client' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ClientApi.getOAuth2ClientPage(queryParams)
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
    await ClientApi.deleteOAuth2Client(id)
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
