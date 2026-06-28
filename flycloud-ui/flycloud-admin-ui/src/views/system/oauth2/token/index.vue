<template>
  <doc-alert
    :title="t('auto.views.system.oauth2.token.index.kcf78c257')"
    url="https://doc.iocoder.cn/oauth2/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="90px"
    >
      <el-form-item :label="t('auto.views.system.oauth2.token.index.kec750ef6')" prop="userId">
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.system.oauth2.token.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.oauth2.token.index.k31ab92d1')" prop="userType">
        <el-select
          v-model="queryParams.userType"
          :placeholder="t('auto.views.system.oauth2.token.index.k8d91841e')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.oauth2.token.index.k090a075b')" prop="clientId">
        <el-input
          v-model="queryParams.clientId"
          :placeholder="t('auto.views.system.oauth2.token.index.k146a2f93')"
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
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.oauth2.token.index.k73bf8411')"
        align="center"
        prop="accessToken"
        width="300"
      />
      <el-table-column
        :label="t('auto.views.system.oauth2.token.index.kf4a36e0d')"
        align="center"
        prop="refreshToken"
        width="300"
      />
      <el-table-column
        :label="t('auto.views.system.oauth2.token.index.kec750ef6')"
        align="center"
        prop="userId"
      />
      <el-table-column
        :label="t('auto.views.system.oauth2.token.index.k31ab92d1')"
        align="center"
        prop="userType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.pay.cashier.index.k27aefecf')"
        align="center"
        prop="expiresTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="danger"
            @click="handleForceLogout(scope.row.accessToken)"
            v-hasPermi="['system:oauth2-token:delete']"
          >
            {{ t('extra.k4a69ec74') }}
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

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as OAuth2AccessTokenApi from '@/api/system/oauth2/token'
const { t } = useI18n()
defineOptions({ name: 'SystemTokenClient' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  userType: undefined,
  clientId: null
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await OAuth2AccessTokenApi.getAccessTokenPage(queryParams)
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

/** 强制退出操作 */
const handleForceLogout = async (accessToken: string) => {
  try {
    // 删除的二次确认
    await message.confirm(t('auto.views.system.oauth2.token.index.k924e081c'))
    // 发起删除
    await OAuth2AccessTokenApi.deleteAccessToken(accessToken)
    message.success(t('common.success'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
