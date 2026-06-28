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
      <el-form-item
        :label="t('auto.views.member.user.detail.UserPointList.k2268f2d5')"
        prop="bizType"
      >
        <el-select
          v-model="queryParams.bizType"
          :placeholder="t('auto.views.member.user.detail.UserPointList.kb6423b8b')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_POINT_BIZ_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserPointList.k8a90add2')"
        prop="title"
      >
        <el-input
          v-model="queryParams.title"
          :placeholder="t('auto.views.member.user.detail.UserPointList.k70e186ff')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserPointList.k350cb33c')"
        prop="createDate"
      >
        <el-date-picker
          v-model="queryParams.createDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.member.user.detail.UserPointList.k1f291968')"
          :end-placeholder="t('auto.views.member.user.detail.UserPointList.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />
          {{ t('extra.k68728cda') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />
          {{ t('extra.kac217246') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.member.user.detail.UserPointList.k9f42dac6')"
        align="center"
        prop="id"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.member.user.detail.UserPointList.k350cb33c')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.member.user.detail.UserPointList.kdd63f6c0')"
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
      <el-table-column :label="t('extra.ke06ab0e3')" align="center" prop="totalPoint" width="100" />
      <el-table-column :label="t('table.title')" align="center" prop="title" />
      <el-table-column
        :label="t('auto.views.bpm.group.UserGroupForm.k412f54dc')"
        align="center"
        prop="description"
      />
      <el-table-column :label="t('extra.k2c7b6265')" align="center" prop="bizId" />
      <el-table-column
        :label="t('auto.views.mall.trade.brokerage.record.index.k2268f2d5')"
        align="center"
        prop="bizType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.MEMBER_POINT_BIZ_TYPE" :value="scope.row.bizType" />
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
import * as RecordApi from '@/api//member/point/record'
const { t } = useI18n()
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  bizType: undefined,
  title: null,
  createDate: [],
  userId: NaN
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await RecordApi.getRecordPage(queryParams)
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

const { userId } = defineProps({
  userId: {
    type: Number,
    required: true
  }
})

/** 初始化 **/
onMounted(() => {
  queryParams.userId = userId
  getList()
})
</script>
