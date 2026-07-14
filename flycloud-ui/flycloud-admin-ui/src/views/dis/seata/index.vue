<template>
  <div class="seata-page">
    <el-row :gutter="16" class="test-panel">
      <el-col :xs="24" :lg="10">
        <section class="input-section">
          <div class="section-title">测试数据</div>
          <div class="input-fields">
            <div class="input-field">
              <div class="input-label">数据源一的测试数据</div>
              <el-input
                v-model="dataSourceOneTestData"
                type="textarea"
                :rows="3"
                :maxlength="512"
                show-word-limit
                resize="none"
                placeholder="请输入数据源一的测试数据"
                :disabled="executing"
              />
            </div>
            <div class="input-field">
              <div class="input-label">数据源二的测试数据</div>
              <el-input
                v-model="dataSourceTwoTestData"
                type="textarea"
                :rows="3"
                :maxlength="512"
                show-word-limit
                resize="none"
                placeholder="请输入数据源二的测试数据"
                :disabled="executing"
              />
            </div>
          </div>
          <div class="action-group">
            <el-button :loading="executing" :disabled="executing" @click="seataTest(0)">
              {{ t('auto.views.dis.seata.index.k12a6778a') }}
            </el-button>
            <el-button type="primary" :loading="executing" :disabled="executing" @click="seataTest(1)">
              {{ t('auto.views.dis.seata.index.kbed4c98e') }}
            </el-button>
          </div>
        </section>
      </el-col>

      <el-col :xs="24" :lg="14">
        <section class="instruction-section">
          <div class="section-title">测试说明</div>
          <ol>
            <li>{{ t('extra.kbdeb2eea') }}</li>
            <li>{{ t('extra.k2fb71000') }}</li>
            <li>{{ t('extra.kf0f0d734') }}</li>
            <li>{{ t('extra.kd8370bd5') }}</li>
            <li>{{ t('extra.k9b3e87cf') }}</li>
          </ol>
        </section>
      </el-col>
    </el-row>

    <el-divider class="page-divider" />

    <div ref="dataRowRef" class="data-row-wrapper">
      <el-row :gutter="16" class="data-row">
        <el-col :xs="24" :lg="12" class="data-column">
          <section class="data-section">
            <div class="data-section__header">
              <span>数据源一的数据</span>
              <span class="data-section__database">fly_cloud</span>
            </div>
            <el-table v-loading="listLoading" :data="dataSourceOneList" border stripe :height="tableHeight">
              <el-table-column prop="id" label="数据ID" width="100" align="center" />
              <el-table-column prop="databaseName" label="数据源名称" min-width="130" />
              <el-table-column prop="testData" label="测试数据" min-width="180" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" min-width="180" :formatter="dateFormatter" />
            </el-table>
          </section>
        </el-col>

        <el-col :xs="24" :lg="12" class="data-column">
          <section class="data-section">
            <div class="data-section__header">
              <span>数据源二的数据</span>
              <span class="data-section__database">fly_seata</span>
            </div>
            <el-table v-loading="listLoading" :data="dataSourceTwoList" border stripe :height="tableHeight">
              <el-table-column prop="id" label="数据ID" width="100" align="center" />
              <el-table-column prop="databaseName" label="数据源名称" min-width="130" />
              <el-table-column prop="testData" label="测试数据" min-width="180" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" min-width="180" :formatter="dateFormatter" />
            </el-table>
          </section>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script lang="ts" setup>
import * as SeataApi from '@/api/dis/seata'
import { dateFormatter } from '@/utils/formatTime'

const { t } = useI18n()
const message = useMessage()

defineOptions({ name: 'SeataTest' })

const dataSourceOneTestData = ref('')
const dataSourceTwoTestData = ref('')
const executing = ref(false)
const listLoading = ref(false)
const dataSourceOneList = ref<SeataApi.SeataTestDataVO[]>([])
const dataSourceTwoList = ref<SeataApi.SeataTestDataVO[]>([])
const dataRowRef = ref<HTMLElement>()
const tableHeight = ref(300)
let dataRowResizeObserver: ResizeObserver | undefined

/** 根据剩余可视区域计算表格高度，使数据在表格内部滚动。 */
const updateTableHeight = () => {
  const dataRowHeight = dataRowRef.value?.clientHeight
  tableHeight.value = dataRowHeight ? Math.max(180, dataRowHeight - 54) : 300
}

/** 查询两个数据源各自最新的十条测试数据。 */
const getTestDataList = async () => {
  listLoading.value = true
  try {
    const data = await SeataApi.getSeataTestDataList()
    dataSourceOneList.value = data.dataSourceOne ?? []
    dataSourceTwoList.value = data.dataSourceTwo ?? []
  } finally {
    listLoading.value = false
  }
}

/** 执行 Seata 分布式事务测试。 */
const seataTest = async (isRollback: number) => {
  const sourceOneTestData = dataSourceOneTestData.value.trim()
  const sourceTwoTestData = dataSourceTwoTestData.value.trim()
  if (!sourceOneTestData || !sourceTwoTestData) {
    message.warning('请填写两个数据源的测试数据')
    return
  }

  const confirmText =
    isRollback === 0
      ? t('auto.views.dis.seata.index.kbeb513d1')
      : t('auto.views.dis.seata.index.k62995029')

  try {
    await message.confirm(confirmText)
  } catch {
    return
  }

  executing.value = true
  try {
    await SeataApi.seataTestApi(isRollback, {
      dataSourceOneTestData: sourceOneTestData,
      dataSourceTwoTestData: sourceTwoTestData
    })
    message.success(t('auto.views.dis.seata.index.k94b6e63d'))
  } finally {
    executing.value = false
    dataSourceOneTestData.value = ''
    dataSourceTwoTestData.value = ''
    await getTestDataList()
  }
}

onMounted(async () => {
  getTestDataList()
  await nextTick()
  updateTableHeight()
  if (dataRowRef.value) {
    dataRowResizeObserver = new ResizeObserver(updateTableHeight)
    dataRowResizeObserver.observe(dataRowRef.value)
  }
})

onBeforeUnmount(() => {
  dataRowResizeObserver?.disconnect()
})
</script>

<style lang="scss" scoped>
.test-panel {
  flex: 0 0 242px;
  align-items: stretch;
}

.seata-page {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  height: calc(100vh - 150px);
  min-height: 0;
  margin-top: -8px;
  padding: 12px 16px 16px;
  overflow: hidden;
}

.input-section,
.instruction-section,
.data-section {
  height: 100%;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  background: var(--el-bg-color);
}

.input-section {
  display: flex;
  flex-direction: column;
  padding: 16px;
  text-align: center;
}

.input-fields {
  display: flex;
  gap: 12px;
  margin-top: 10px;
}

.input-field {
  flex: 1;
  min-width: 0;
  text-align: left;
}

.input-label {
  margin-bottom: 6px;
  color: var(--el-text-color-regular);
  font-size: 13px;
}

.instruction-section {
  padding: 16px 28px;
}

.section-title {
  color: var(--el-text-color-primary);
  font-size: 16px;
  font-weight: 600;
}

.section-description {
  margin: 6px 0 10px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

.instruction-section ol {
  margin: 8px 0 0;
  padding-left: 22px;
  color: var(--el-text-color-regular);
  line-height: 25px;
}

.action-group {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 12px;
}

.page-divider {
  flex: 0 0 auto;
  margin: 12px 0;
}

.data-row-wrapper {
  flex: 1;
  min-height: 0;
}

.data-row {
  height: 100%;
}

.data-column {
  display: flex;
  min-height: 0;
}

.data-section {
  display: flex;
  flex: 1;
  flex-direction: column;
  overflow: hidden;
}

.data-section__header {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 14px 16px;
  color: var(--el-text-color-primary);
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.data-section__database {
  position: absolute;
  right: 16px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  font-weight: 400;
}

@media (max-width: 1199px) {
  .seata-page {
    overflow-y: auto;
  }

  .test-panel {
    flex-basis: auto;
  }

  .data-section {
    margin-bottom: 16px;
  }
}
</style>
