<template>
  <doc-alert
    :title="t('auto.views.mall.promotion.diy.page.index.k5ab6537f')"
    url="https://doc.iocoder.cn/mall/diy/"
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
      <el-form-item :label="t('auto.views.mall.promotion.diy.page.index.k4a2d79ff')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.mall.promotion.diy.page.index.ke41e27e6')"
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
          :start-placeholder="t('auto.views.mall.promotion.diy.page.index.k1f291968')"
          :end-placeholder="t('auto.views.mall.promotion.diy.page.index.kf4b9b2b5')"
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
          v-hasPermi="['promotion:diy-page:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kb9af69e0') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.mall.promotion.diy.page.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.diy.page.index.k81946fbf')"
        align="center"
        prop="previewPicUrls"
      >
        <template #default="scope">
          <el-image
            class="h-40px max-w-40px"
            v-for="(url, index) in scope.row.previewPicUrls"
            :key="index"
            :src="url"
            :preview-src-list="scope.row.previewPicUrls"
            :initial-index="index"
            preview-teleported
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mall.promotion.diy.page.DiyPageForm.k4a2d79ff')"
        align="center"
        prop="name"
      />
      <el-table-column :label="t('common.remark')" align="center" prop="remark" />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handleDecorate(scope.row.id)"
            v-hasPermi="['promotion:diy-page:update']"
          >
            {{ t('extra.k81b2cb54') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['promotion:diy-page:update']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['promotion:diy-page:delete']"
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
  <DiyPageForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as DiyPageApi from '@/api/mall/promotion/diy/page'
import DiyPageForm from './DiyPageForm.vue'

/** 装修页面 */
const { t } = useI18n()
defineOptions({ name: 'DiyPage' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DiyPageApi.getDiyPagePage(queryParams)
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
    await DiyPageApi.deleteDiyPage(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 打开装修页面 */
const { push } = useRouter()
const handleDecorate = (id: number) => {
  push({ name: 'DiyPageDecorate', params: { id } })
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
