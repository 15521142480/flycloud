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
        :label="t('auto.views.member.user.detail.UserExperienceRecordList.k2268f2d5')"
        prop="bizType"
      >
        <el-select
          v-model="queryParams.bizType"
          :placeholder="t('auto.views.member.user.detail.UserExperienceRecordList.kb6423b8b')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.MEMBER_EXPERIENCE_BIZ_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserExperienceRecordList.k748d7dc7')"
        prop="title"
      >
        <el-input
          v-model="queryParams.title"
          :placeholder="t('auto.views.member.user.detail.UserExperienceRecordList.k901722e5')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.member.user.detail.UserExperienceRecordList.k1f291968')"
          :end-placeholder="t('auto.views.member.user.detail.UserExperienceRecordList.kf4b9b2b5')"
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
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.member.user.detail.UserExperienceRecordList.k9f42dac6')"
        align="center"
        prop="id"
        width="150px"
      />
      <el-table-column
        :label="t('auto.views.member.user.detail.UserExperienceRecordList.k350cb33c')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.member.user.detail.UserExperienceRecordList.kdd5a3ee9')"
        align="center"
        prop="experience"
        width="150px"
      >
        <template #default="scope">
          <el-tag v-if="scope.row.experience > 0" class="ml-2" type="success" effect="dark">
            +{{ scope.row.experience }}
          </el-tag>
          <el-tag v-else class="ml-2" type="danger" effect="dark">
            {{ scope.row.experience }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="总经验" align="center" prop="totalExperience" width="150px">
        <template #default="scope">
          <el-tag class="ml-2" effect="dark">
            {{ scope.row.totalExperience }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" width="150px" />
      <el-table-column label="描述" align="center" prop="description" />
      <el-table-column label="业务编号" align="center" prop="bizId" width="150px" />
      <el-table-column label="业务类型" align="center" prop="bizType" width="150px">
        <!--   TODO 芋艿：此处应创建对应的字典 -->
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.MEMBER_EXPERIENCE_BIZ_TYPE" :value="scope.row.bizType" />
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
import { dateFormatter } from '@/utils/formatTime'
import * as ExperienceRecordApi from '@/api/member/experience-record/index'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
const { t } = useI18n()
defineOptions({ name: 'UserExperienceRecordList' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  bizId: null,
  bizType: null,
  title: null,
  description: null,
  experience: null,
  totalExperience: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ExperienceRecordApi.getExperienceRecordPage(queryParams)
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
