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
      <el-form-item :label="t('auto.views.crm.clue.index.k733eabce')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.crm.clue.index.kcafc53c2')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.crm.clue.index.kec35bcb8')" prop="transformStatus">
        <el-select v-model="queryParams.transformStatus" class="!w-240px">
          <el-option :value="false" :label="t('auto.views.crm.clue.index.kc55bee51')" />
          <el-option :value="true" :label="t('auto.views.crm.clue.index.k88592f25')" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.crm.clue.index.k5a9cc5e8')" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          :placeholder="t('auto.views.crm.clue.index.k5ecce333')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.crm.clue.index.kf39c6b62')" prop="telephone">
        <el-input
          v-model="queryParams.telephone"
          :placeholder="t('auto.views.crm.clue.index.k902a7fb3')"
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
        <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:clue:saveOrUpdate']">
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.ke5c77d7a') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['crm:clue:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k13ce82dc') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('auto.views.crm.clue.index.k0bbf4db1')" name="1" />
      <el-tab-pane :label="t('auto.views.crm.clue.index.k1640a0ff')" name="2" />
      <el-tab-pane :label="t('auto.views.crm.clue.index.k72715f2e')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.crm.clue.index.k733eabce')"
        align="center"
        prop="name"
        fixed="left"
        width="160"
      >
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.clue.detail.ClueDetailsHeader.k673239bf')"
        align="center"
        prop="source"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_SOURCE" :value="scope.row.source" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.clue.ClueForm.k9c01ad09')"
        align="center"
        prop="mobile"
        width="120"
      />
      <el-table-column
        :label="t('system.dept.phone')"
        align="center"
        prop="telephone"
        width="130"
      />
      <el-table-column :label="t('system.user.email')" align="center" prop="email" width="180" />
      <el-table-column
        :label="t('auto.views.crm.clue.ClueForm.k67d2d797')"
        align="center"
        prop="detailAddress"
        width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.k7b39ef2d')"
        prop="industryId"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_INDUSTRY" :value="scope.row.industryId" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.kbb7208b8')"
        prop="level"
        width="135"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="scope.row.level" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')"
        prop="contactNextTime"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.remark')" prop="remark" width="200" />
      <el-table-column
        :label="t('extra.kffa0750f')"
        align="center"
        prop="contactLastTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        align="center"
        :label="t('extra.k1afb06c4')"
        prop="contactLastContent"
        width="200"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.business.BusinessForm.k974d383f')"
        prop="ownerUserName"
        width="100px"
      />
      <el-table-column
        align="center"
        :label="t('profile.user.dept')"
        prop="ownerUserDeptName"
        width="100"
      />
      <el-table-column
        :label="t('common.updateTime')"
        align="center"
        prop="updateTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
        prop="creatorName"
        width="100px"
      />
      <el-table-column :label="t('common.operation')" align="center" min-width="110" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['crm:clue:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:clue:delete']"
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
  <ClueForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
// @ts-nocheck
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ClueApi from '@/api/crm/clue'
import ClueForm from './ClueForm.vue'
import { TabsPaneContext } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'CrmClue' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sceneType: '1', // 默认和 activeName 相等
  name: null,
  telephone: null,
  mobile: null,
  transformStatus: false
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const activeName = ref('1') // 列表 tab

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ClueApi.getCluePage(queryParams)
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

/** tab 切换 */
const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName
  handleQuery()
}

/** 打开线索详情 */
const { push } = useRouter()
const openDetail = (id: string) => {
  push({ name: 'CrmClueDetail', params: { id } })
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
    await ClueApi.deleteClue(id)
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
    const data = await ClueApi.exportClue(queryParams)
    download.excel(data, t('auto.views.crm.clue.index.k548e597d'))
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
