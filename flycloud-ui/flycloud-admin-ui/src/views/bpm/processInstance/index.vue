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
      <el-form-item label="" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.bpm.processInstance.index.ke9399a13')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>

      <el-form-item>
        <el-button @click="handleQuery"
          ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
        >
      </el-form-item>

      <!-- TODO @ tuituji：style 可以使用 unocss -->
      <el-form-item label="" prop="category" :style="{ position: 'absolute', right: '130px' }">
        <!-- TODO @tuituji：应该选择好分类，就触发搜索啦。 -->
        <el-select
          v-model="queryParams.category"
          :placeholder="t('auto.views.bpm.processInstance.index.k8c96f9ce')"
          clearable
          class="!w-155px"
        >
          <el-option
            v-for="category in categoryList"
            :key="category.code"
            :label="category.name"
            :value="category.code"
          />
        </el-select>
      </el-form-item>

      <!-- 高级筛选 -->
      <!-- TODO @ tuituji：style 可以使用 unocss -->
      <el-form-item :style="{ position: 'absolute', right: '0px' }">
        <el-button v-popover="popoverRef" v-click-outside="onClickOutside" :icon="List">
          {{ t('extra.k541a6eb0') }}
        </el-button>
        <el-popover
          ref="popoverRef"
          trigger="click"
          virtual-triggering
          persistent
          :width="400"
          :show-arrow="false"
          placement="bottom-end"
        >
          <el-form-item
            :label="t('auto.views.bpm.processInstance.index.k340010f7')"
            class="bold-label"
            label-position="top"
            prop="category"
          >
            <el-select
              v-model="queryParams.category"
              :placeholder="t('auto.views.bpm.processInstance.index.k7638b8ab')"
              clearable
              class="!w-390px"
            >
              <el-option
                v-for="category in categoryList"
                :key="category.code"
                :label="category.name"
                :value="category.code"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.bpm.processInstance.index.k3003243e')"
            class="bold-label"
            label-position="top"
            prop="processDefinitionKey"
          >
            <el-input
              v-model="queryParams.processDefinitionKey"
              :placeholder="t('auto.views.bpm.processInstance.index.kc1b278c5')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-390px"
            />
          </el-form-item>
          <el-form-item
            :label="t('auto.views.bpm.processInstance.index.k3416ba2b')"
            class="bold-label"
            label-position="top"
            prop="status"
          >
            <el-select
              v-model="queryParams.status"
              :placeholder="t('auto.views.bpm.processInstance.index.k64c1d7f9')"
              clearable
              class="!w-390px"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.bpm.processInstance.index.k44042ce0')"
            class="bold-label"
            label-position="top"
            prop="createTime"
          >
            <el-date-picker
              v-model="queryParams.createTime"
              value-format="YYYY-MM-DD HH:mm:ss"
              type="daterange"
              :start-placeholder="t('auto.views.bpm.processInstance.index.k1f291968')"
              :end-placeholder="t('auto.views.bpm.processInstance.index.kf4b9b2b5')"
              :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
              class="!w-240px"
            />
          </el-form-item>
        </el-popover>
        <!-- TODO @tuituji：这里应该有确认，和取消、清空搜索条件，三个按钮。 -->
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" height="calc(100vh - 320px)">
      <el-table-column
        :label="t('auto.views.bpm.processInstance.index.k323a8305')"
        align="center"
        prop="name"
        min-width="200px"
        fixed="left"
      />
      <el-table-column
        :label="t('auto.views.bpm.processInstance.index.k8377bb01')"
        align="center"
        prop="categoryName"
        min-width="100"
        fixed="left"
      />
      <!-- TODO ：摘要 -->
      <!-- TODO @tuituji：流程状态。可见需求文档里  -->
      <el-table-column
        :label="t('auto.views.bpm.processInstance.index.k3416ba2b')"
        prop="status"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        label="发起时间"
        align="center"
        prop="startTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        label="结束时间"
        align="center"
        prop="endTime"
        width="180"
        :formatter="dateFormatter"
      />
      <!--<el-table-column align="center" label="耗时" prop="durationInMillis" width="160">
        <template #default="scope">
          {{ scope.row.durationInMillis > 0 ? formatPast2(scope.row.durationInMillis) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="当前审批任务" align="center" prop="tasks" min-width="120px">
        <template #default="scope">
          <el-button type="primary" v-for="task in scope.row.tasks" :key="task.id" link>
            <span>{{ task.name }}</span>
          </el-button>
        </template>
      </el-table-column>
      -->
      <el-table-column label="操作" align="center" fixed="right" width="180">
        <template #default="scope">
          <el-button
            link
            type="primary"
            v-hasPermi="['bpm:audit:my:detail']"
            @click="handleDetail(scope.row)"
          >
            详情
          </el-button>
          <el-button
            link
            type="primary"
            v-if="scope.row.status === 1"
            v-hasPermi="['bpm:audit:my:cancel']"
            @click="handleCancel(scope.row)"
          >
            取消
          </el-button>
          <el-button link type="primary" v-else @click="handleCreate(scope.row)">
            重新发起
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
</template>
<script lang="ts" setup>
// TODO @tuituji：List 改成 <Icon icon="ep:plus" class="mr-5px" /> 类似这种组件哈。
import { List } from '@element-plus/icons-vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { ElMessageBox } from 'element-plus'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import { CategoryApi, CategoryVO } from '@/api/bpm/category'
import { ProcessInstanceVO } from '@/api/bpm/processInstance'
import * as DefinitionApi from '@/api/bpm/definition'
const { t } = useI18n()
defineOptions({ name: 'BpmProcessInstanceMy' })

const router = useRouter() // 路由
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  processDefinitionKey: undefined,
  category: undefined,
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const categoryList = ref<CategoryVO[]>([]) // 流程分类列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProcessInstanceApi.getProcessInstanceMyPage(queryParams)
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

/** 发起流程操作 **/
const handleCreate = async (row?: ProcessInstanceVO) => {
  // 如果是【业务表单】，不支持重新发起
  if (row?.id) {
    const processDefinitionDetail = await DefinitionApi.getProcessDefinition(
      row.processDefinitionId
    )
    if (processDefinitionDetail.formType === 20) {
      message.error(t('auto.views.bpm.processInstance.index.k88cb5d12'))
      return
    }
  }
  // 跳转发起流程界面
  await router.push({
    name: 'BpmProcessInstanceCreate',
    query: { processInstanceId: row?.id }
  })
}

/** 查看详情 */
const handleDetail = (row) => {
  router.push({
    name: 'BpmProcessInstanceDetail',
    query: {
      id: row.id
    }
  })
}

/** 取消按钮操作 */
const handleCancel = async (row) => {
  // 二次确认
  const { value } = await ElMessageBox.prompt(
    t('auto.views.bpm.processInstance.index.kd878c25a'),
    t('auto.views.bpm.processInstance.index.kef368da8'),
    {
      confirmButtonText: t('common.ok'),
      cancelButtonText: t('common.cancel'),
      inputPattern: /^[\s\S]*.*\S[\s\S]*$/, // 判断非空，且非空格
      inputErrorMessage: t('auto.views.bpm.processInstance.index.k6a6cf9af')
    }
  )
  // 发起取消
  await ProcessInstanceApi.cancelProcessInstanceByStartUser(row.id, value)
  message.success(t('auto.views.bpm.processInstance.index.k24a1c71e'))
  // 刷新列表
  await getList()
}

// TODO @tuituji：这个 import 是不是没用哈？
import { ClickOutside as vClickOutside } from 'element-plus'

// TODO @tuituji：onClickAdvancedSearch。方法名叫这个，会更好一些哇？打开高级搜索。
const popoverRef = ref()
const onClickOutside = () => {
  unref(popoverRef).popperRef?.delayHide?.()
}

/** 激活时 **/
onActivated(() => {
  getList()
})

/** 初始化 **/
onMounted(async () => {
  await getList()
  categoryList.value = await CategoryApi.getCategorySimpleList()
})
</script>
<style>
.bold-label .el-form-item__label {
  font-weight: bold; /* 将字体加粗 */
}
</style>
