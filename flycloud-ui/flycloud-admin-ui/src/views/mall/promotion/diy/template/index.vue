<template>
  <doc-alert
    :title="t('auto.views.mall.promotion.diy.template.index.k5ab6537f')"
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
      <el-form-item
        :label="t('auto.views.mall.promotion.diy.template.index.kbbc511d0')"
        prop="name"
      >
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.mall.promotion.diy.template.index.k86bd4450')"
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
          :start-placeholder="t('auto.views.mall.promotion.diy.template.index.k1f291968')"
          :end-placeholder="t('auto.views.mall.promotion.diy.template.index.kf4b9b2b5')"
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
          v-hasPermi="['promotion:diy-template:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k19e408b4') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.mall.promotion.diy.template.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.diy.template.index.k81946fbf')"
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
        :label="t('auto.views.mall.promotion.diy.template.DiyTemplateForm.kbbc511d0')"
        align="center"
        prop="name"
      />
      <el-table-column :label="t('extra.k7386b590')" align="center" prop="used">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.used" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k01100972')"
        align="center"
        prop="usedTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.remark')" align="center" prop="remark" />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center" width="200">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handleDecorate(scope.row.id)"
            v-hasPermi="['promotion:diy-template:update']"
          >
            {{ t('extra.k81b2cb54') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['promotion:diy-template:update']"
          >
            {{ t('common.edit') }}
          </el-button>
          <template v-if="!scope.row.used">
            <el-button
              link
              type="primary"
              @click="handleUse(scope.row)"
              v-hasPermi="['promotion:diy-template:use']"
            >
              {{ t('extra.kecff77a8') }}
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['promotion:diy-template:delete']"
            >
              {{ t('common.delete') }}
            </el-button>
          </template>
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
  <DiyTemplateForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as DiyTemplateApi from '@/api/mall/promotion/diy/template'
import DiyTemplateForm from './DiyTemplateForm.vue'
import { DICT_TYPE } from '@/utils/dict'

/** 装修模板 */
const { t } = useI18n()
defineOptions({ name: 'DiyTemplate' })

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
    const data = await DiyTemplateApi.getDiyTemplatePage(queryParams)
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
    await DiyTemplateApi.deleteDiyTemplate(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 使用模板 */
const handleUse = async (row: DiyTemplateApi.DiyTemplateVO) => {
  try {
    // 使用模板的二次确认
    await message.confirm(t('extra.kae05a21f', { p0: row.name }))
    // 发起删除
    await DiyTemplateApi.useDiyTemplate(row.id!)
    message.success(t('auto.views.mall.promotion.diy.template.index.kbd1f9ccb'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 打开装修页面 */
const { push } = useRouter()
const handleDecorate = (id: number) => {
  push({ name: 'DiyTemplateDecorate', params: { id } })
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
