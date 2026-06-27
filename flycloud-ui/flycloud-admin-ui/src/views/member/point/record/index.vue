<template>
  <doc-alert
    :title="t('auto.views.member.point.record.index.k1bee1a27')"
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
      <el-form-item :label="t('auto.views.member.point.record.index.k9ba763ea')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.member.point.record.index.k359da8d3')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.point.record.index.k2268f2d5')" prop="bizType">
        <el-select
          v-model="queryParams.bizType"
          :placeholder="t('auto.views.member.point.record.index.kb6423b8b')"
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
      <el-form-item :label="t('auto.views.member.point.record.index.k8a90add2')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="t('auto.views.member.point.record.index.k70e186ff')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.point.record.index.k350cb33c')" prop="createDate">
        <el-date-picker
          v-model="queryParams.createDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.member.point.record.index.k1f291968')"
          :end-placeholder="t('auto.views.member.point.record.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />
          {{ t('extra.k101d96f3') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />
          {{ t('extra.ke09065e5') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.member.point.record.index.k9f42dac6')"
        align="center"
        prop="id"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.member.point.record.index.k350cb33c')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.member.point.record.index.k9ba763ea')"
        align="center"
        prop="name"
        width="200"
      />
      <el-table-column
        :label="t('auto.views.member.point.record.index.kdd63f6c0')"
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
      <el-table-column label="总积分" align="center" prop="totalPoint" width="100" />
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="描述" align="center" prop="description" />
      <el-table-column label="业务编码" align="center" prop="bizId" />
      <el-table-column label="业务类型" align="center" prop="bizType">
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

  <!-- 表单弹窗：添加/修改 -->
  <RecordForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as RecordApi from '@/api/member/point/record'
const { t } = useI18n()
defineOptions({ name: 'PointRecord' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  bizType: null,
  title: null,
  createDate: []
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
