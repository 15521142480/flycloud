<template>
  <doc-alert
    :title="t('auto.views.system.social.client.index.ke3469914')"
    url="https://doc.iocoder.cn/social-user/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="130px"
    >
      <el-form-item :label="t('auto.views.system.social.client.index.k6c19fe01')" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.social.client.index.k445d8859')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.social.client.index.kf6445550')" prop="socialType">
        <el-select
          v-model="queryParams.socialType"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.social.client.index.k5ff0db53')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SOCIAL_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.social.client.index.k31ab92d1')" prop="userType">
        <el-select
          v-model="queryParams.userType"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.social.client.index.k8d91841e')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.social.client.index.k090a075b')" prop="clientId">
        <el-input
          v-model="queryParams.clientId"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.social.client.index.k146a2f93')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.system.social.client.index.kdba277df')"
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
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k60ae0148') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k79f4d7aa') }}
        </el-button>
        <el-button
          v-hasPermi="['system:social-client:saveOrUpdate']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k158c52a8') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        :label="t('auto.views.system.social.client.index.k9f42dac6')"
        prop="id"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.system.social.client.index.k6c19fe01')"
        prop="name"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.system.social.client.index.kf6445550')"
        prop="socialType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SOCIAL_TYPE" :value="scope.row.socialType" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.infra.apiAccessLog.index.k31ab92d1')"
        prop="userType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.system.oauth2.client.ClientForm.k090a075b')"
        prop="clientId"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.status')" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.operation')">
        <template #default="scope">
          <el-button
            v-hasPermi="['system:social-client:saveOrUpdate']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['system:social-client:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <SocialClientForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as SocialClientApi from '@/api/system/social/client'
import SocialClientForm from './SocialClientForm.vue'
const { t } = useI18n()
defineOptions({ name: 'SocialClient' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  socialType: undefined,
  userType: undefined,
  clientId: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SocialClientApi.getSocialClientPage(queryParams)
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
    await SocialClientApi.deleteSocialClient(id)
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
