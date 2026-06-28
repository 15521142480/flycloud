<template>
  <doc-alert
    :title="t('auto.views.member.signin.record.index.k1bee1a27')"
    url="https://doc.iocoder.cn/member/level/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.member.signin.record.index.kb864e7d5')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.member.signin.record.index.kccc2513f')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.signin.record.index.kdf7d081d')" prop="day">
        <el-input
          v-model="queryParams.day"
          :placeholder="t('auto.views.member.signin.record.index.ke60c000c')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.signin.record.index.ke7653484')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.member.signin.record.index.k1f291968')"
          :end-placeholder="t('auto.views.member.signin.record.index.kf4b9b2b5')"
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
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.member.signin.record.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.member.signin.record.index.kb864e7d5')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.member.signin.record.index.kdf7d081d')"
        align="center"
        prop="day"
        :formatter="
          (_, __, cellValue) => [t('extra.k82bb0b7d'), cellValue, t('extra.k94ca9b37')].join(' ')
        "
      />
      <el-table-column
        :label="t('auto.views.member.signin.record.index.kdd63f6c0')"
        align="center"
        prop="point"
        width="100"
      >
        <template #default="scope">
          <el-tag v-if="scope.row.point > 0" class="ml-2" type="success" effect="dark">
            +{{ scope.row.point }}
          </el-tag>
          <el-tag v-else class="ml-2" type="danger" effect="dark"> {{ scope.row.point }} </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.member.signin.record.index.ke7653484')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
      />
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
import { dateFormatter } from '@/utils/formatTime'
import * as SignInRecordApi from '@/api/member/signin/record'
const { t } = useI18n()
defineOptions({ name: 'SignInRecord' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  day: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SignInRecordApi.getSignInRecordPage(queryParams)
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
