<template>
  <doc-alert :title="t('auto.views.mp.tag.index.k4bac1943')" url="https://doc.iocoder.cn/mp/tag/" />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.mp.tag.index.ke48fc0ee')" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['mp:tag:saveOrUpdate']"
          :disabled="queryParams.accountId === 0"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.ke9c5073c') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleSync"
          v-hasPermi="['mp:tag:sync']"
          :disabled="queryParams.accountId === 0"
        >
          <Icon icon="ep:refresh" class="mr-5px" /> {{ t('extra.k7ca6f5d0') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column :label="t('auto.views.mp.tag.index.k9f42dac6')" align="center" prop="id" />
      <el-table-column :label="t('auto.views.mp.tag.index.k182295d4')" align="center" prop="name" />
      <el-table-column
        :label="t('auto.views.mp.tag.index.k799620a8')"
        align="center"
        prop="count"
      />
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
            v-hasPermi="['mp:tag:saveOrUpdate']"
          >
            {{ t('extra.k4c512392') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['mp:tag:delete']"
          >
            {{ t('extra.k3250aea3') }}
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
  <TagForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as MpTagApi from '@/api/mp/tag'
import TagForm from './TagForm.vue'
import WxAccountSelect from '@/views/mp/components/wx-account-select'
const { t } = useI18n()
defineOptions({ name: 'MpTag' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref<any[]>([]) // 列表的数据

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  accountId: -1
})

const formRef = ref<InstanceType<typeof TagForm> | null>(null)

/** 侦听公众号变化 **/
const onAccountChanged = (id: number) => {
  queryParams.accountId = id
  queryParams.pageNum = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  try {
    loading.value = true
    const data = await MpTagApi.getTagPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const openForm = (type: 'create' | 'update', id?: number) => {
  formRef.value?.open(type, queryParams.accountId, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await MpTagApi.deleteTag(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 同步操作 */
const handleSync = async () => {
  try {
    await message.confirm(t('auto.views.mp.tag.index.k40108ee3'))
    await MpTagApi.syncTag(queryParams.accountId as number)
    message.success(t('auto.views.mp.tag.index.k8850c001'))
    await getList()
  } catch {}
}
</script>
