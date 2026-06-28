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
      <el-form-item :label="t('auto.views.system.post.index.kf7e2b452')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.system.post.index.ka1330101')"
          clearable
          class="!w-240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.post.index.k0fc3644a')" prop="code">
        <el-input
          v-model="queryParams.code"
          :placeholder="t('auto.views.system.post.index.k6524110e')"
          clearable
          class="!w-240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.system.post.index.kdba277df')"
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
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['sys:post:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kaa946f09') }}
        </el-button>
        <!--        <el-button-->
        <!--          type="success"-->
        <!--          plain-->
        <!--          @click="handleExport"-->
        <!--          :loading="exportLoading"-->
        <!--          v-hasPermi="['sys:post:download']"-->
        <!--        >-->
        <!--          <Icon icon="ep:download" class="mr-5px" /> 导出-->
        <!--        </el-button>-->
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.post.index.kcbef772a')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.system.post.index.kf7e2b452')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.system.post.index.k0fc3644a')"
        align="center"
        prop="code"
      />
      <el-table-column
        :label="t('auto.views.system.post.index.k4d834af1')"
        width="80"
        align="center"
        prop="sort"
      />
      <el-table-column
        :label="t('auto.views.system.post.index.ka6e9cffc')"
        align="center"
        prop="remark"
      />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
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
            v-hasPermi="['sys:post:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['sys:post:delete']"
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
  <PostForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as PostApi from '@/api/system/post'
import PostForm from './PostForm.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemPost' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  code: '',
  name: '',
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询岗位列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await PostApi.getPostPage(queryParams)
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
    await PostApi.deletePost(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await PostApi.exportPost(queryParams)
    download.excel(data, t('auto.views.system.post.index.k21223665'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
