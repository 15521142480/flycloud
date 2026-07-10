<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form class="-mb-15px" :inline="true">
      <el-form-item>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['infra:data-source-config:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k45083681') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.infra.dataSourceConfig.index.kfe2678c5')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.infra.dataSourceConfig.index.kd716d61b')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.infra.dataSourceConfig.index.k06589cdc')"
        align="center"
        prop="url"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('auto.views.infra.dataSourceConfig.index.ka1aaf352')"
        align="center"
        prop="username"
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
            v-hasPermi="['infra:data-source-config:saveOrUpdate']"
            :disabled="scope.row.id === 0"
          >
            {{ t('extra.kf776bedb') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['infra:data-source-config:delete']"
            :disabled="scope.row.id === 0"
          >
            {{ t('extra.kcc66a14f') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <DataSourceConfigForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as DataSourceConfigApi from '@/api/infra/dataSourceConfig'
import DataSourceConfigForm from './DataSourceConfigForm.vue'
const { t } = useI18n()
defineOptions({ name: 'InfraDataSourceConfig' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref([]) // 列表的数据

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    list.value = await DataSourceConfigApi.getDataSourceConfigList()
  } finally {
    loading.value = false
  }
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
    await DataSourceConfigApi.deleteDataSourceConfig(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
