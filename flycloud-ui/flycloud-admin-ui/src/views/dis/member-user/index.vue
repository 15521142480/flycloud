<template>
  <ContentWrap>
    <div class="mb-15px flex flex-wrap items-center justify-between gap-10px">
      <div class="flex flex-wrap gap-10px">
        <el-button :loading="indexOperating" type="primary" @click="handleSynchronize">
          <Icon class="mr-5px" icon="ep:refresh" />
          初始化/全量同步索引
        </el-button>
        <el-button :loading="indexOperating" type="warning" @click="handleUpgradeIndex">
          <Icon class="mr-5px" icon="ep:upload" />
          升级索引
        </el-button>
        <el-button :loading="indexOperating" type="danger" plain @click="handleRollbackIndex">
          <Icon class="mr-5px" icon="ep:refresh-left" />
          回滚索引
        </el-button>
      </div>
      <div class="flex items-center gap-10px">
        <el-select
          v-model="selectedHistoricalIndex"
          class="!w-260px"
          clearable
          placeholder="选择会员用户历史索引"
        >
          <el-option-group
            v-for="group in indexAliasGroups"
            :key="group.alias"
            :label="`${group.alias}（当前：${group.currentIndex}）`"
          >
            <el-option
              v-for="index in group.versionIndexes"
              :key="index"
              :disabled="!isHistoricalMemberUserIndex(group, index)"
              :label="index === group.currentIndex ? `${index}（当前运行中）` : index"
              :value="index"
            />
          </el-option-group>
        </el-select>
        <el-button
          :disabled="!selectedHistoricalIndex"
          :loading="indexOperating"
          type="danger"
          plain
          @click="handleDeleteHistoricalIndex"
        >
          <Icon class="mr-5px" icon="ep:delete" />
          删除旧版本
        </el-button>
      </div>
    </div>

    <el-alert
      :closable="false"
      show-icon
      title="会员数据以 MySQL 为准；新增、修改先写本地消息表，再由 RocketMQ 异步更新 Elasticsearch。"
      type="info"
    />
  </ContentWrap>

  <ContentWrap>
    <el-form ref="queryFormRef" :inline="true" :model="queryParams" class="-mb-15px" label-width="72px">
      <el-form-item label="关键词" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          class="!w-220px"
          clearable
          placeholder="昵称、姓名或备注"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="queryParams.mobile" class="!w-180px" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="queryParams.email" class="!w-220px" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="!w-120px" clearable placeholder="全部">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排序字段" prop="sortField">
        <el-select v-model="queryParams.sortField" class="!w-130px">
          <el-option label="创建时间" value="createTime" />
          <el-option label="更新时间" value="updateTime" />
          <el-option label="会员编号" value="id" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序方式" prop="sortOrder">
        <el-select v-model="queryParams.sortOrder" class="!w-110px">
          <el-option label="倒序" value="DESC" />
          <el-option label="正序" value="ASC" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          查询
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          重置
        </el-button>
        <el-button @click="openForm('create')">
          <Icon class="mr-5px" icon="ep:plus" />
          新增会员
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <div class="mb-12px">
      <el-button>
        Elasticsearch搜索引擎的会员用户信息
      </el-button>
    </div>

    <el-table v-loading="loading" :data="list" stripe>
      <el-table-column align="center" label="会员编号" min-width="120" prop="id" />
      <el-table-column align="center" label="手机号" min-width="130" prop="mobile" />
      <el-table-column align="center" label="邮箱" min-width="180" prop="email" show-overflow-tooltip />
      <el-table-column align="center" label="昵称" min-width="130" prop="nickname" show-overflow-tooltip />
      <el-table-column align="center" label="真实姓名" min-width="110" prop="name" show-overflow-tooltip />
      <el-table-column align="center" label="状态" min-width="80" prop="status">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="岗位编号" min-width="130" show-overflow-tooltip>
        <template #default="{ row }">{{ row.postIds?.join(', ') || '-' }}</template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" min-width="170" prop="createTime">
        <template #default="{ row }">{{ formatDate(row.createTime) || '-' }}</template>
      </el-table-column>
      <el-table-column align="center" label="更新时间" min-width="170" prop="updateTime">
        <template #default="{ row }">{{ formatDate(row.updateTime) || '-' }}</template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" min-width="130">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDetail(row.id)">详情</el-button>
          <el-button link type="primary" @click="openForm('update', row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <MemberUserSearchForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as MemberUserSearchApi from '@/api/dis/member-user'
import MemberUserSearchForm from './MemberUserSearchForm.vue'

/** RocketMQ 与 Elasticsearch 会员用户集成测试页面。 */
defineOptions({ name: 'DistributedMemberUserSearch' })

const message = useMessage()
const { push } = useRouter()

const loading = ref(false)
const indexOperating = ref(false)
const selectedHistoricalIndex = ref<string>()
const indexAliasGroups = ref<MemberUserSearchApi.ElasticsearchAliasIndexGroup[]>([])
const total = ref(0)
const list = ref<MemberUserSearchApi.MemberUserSearchVO[]>([])
const queryFormRef = ref()
const formRef = ref()
const queryParams = reactive<MemberUserSearchApi.MemberUserSearchPageParams>({
  pageNum: 1,
  pageSize: 10,
  keyword: undefined,
  mobile: undefined,
  email: undefined,
  status: undefined,
  sortField: 'createTime',
  sortOrder: 'DESC'
})

/** 查询 Elasticsearch 中的会员用户分页列表。 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MemberUserSearchApi.getPage(queryParams)
    list.value = data.list || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

/** 查询 Elasticsearch 中受版本治理的业务别名与真实索引。 */
const getIndexAliases = async () => {
  indexAliasGroups.value = await MemberUserSearchApi.getIndexAliases()
}

/** 判断选项是否为可从会员用户页面安全删除的历史版本。 */
const isHistoricalMemberUserIndex = (group: MemberUserSearchApi.ElasticsearchAliasIndexGroup, index: string) => {
  return group.alias === 'member_user' && index !== group.currentIndex
}

/** 按当前筛选条件重新查询第一页。 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置筛选条件并重新查询。 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.sortField = 'createTime'
  queryParams.sortOrder = 'DESC'
  handleQuery()
}

/** 打开新增或修改会员表单。 */
const openForm = (
  type: 'create' | 'update',
  row?: MemberUserSearchApi.MemberUserSearchVO
) => {
  formRef.value.open(type, row)
}

/** 跳转至现有会员详情页，详情仍以 MySQL 权威数据为准。 */
const openDetail = (id: string) => {
  push({ name: 'MemberUserDetail', params: { id } })
}

/** 初始化真实版本索引并执行全量同步。 */
const handleSynchronize = async () => {
  try {
    await message.confirm('将以 MySQL 权威数据全量覆盖当前会员 ES 索引，是否继续？')
    indexOperating.value = true
    const index = await MemberUserSearchApi.synchronize()
    message.success(`会员索引同步完成，当前真实索引：${index}`)
    await Promise.all([getList(), getIndexAliases()])
  } catch {
    // 用户取消时不提示错误。
  } finally {
    indexOperating.value = false
  }
}

/** 创建新版本索引、同步数据并原子切换 Alias。 */
const handleUpgradeIndex = async () => {
  try {
    await message.confirm('将创建新版本索引并切换 Alias，旧索引会保留至观察期结束，是否继续？')
    indexOperating.value = true
    const index = await MemberUserSearchApi.upgradeIndex()
    message.success(`会员索引已升级并切换至：${index}`)
    await Promise.all([getList(), getIndexAliases()])
  } catch {
    // 用户取消或接口异常时由请求拦截器提示。
  } finally {
    indexOperating.value = false
  }
}

/** 输入升级审计记录编号，在观察期内回滚 Alias。 */
const handleRollbackIndex = async () => {
  try {
    const { value } = await message.prompt('请输入已切换成功的索引升级审计记录编号', '回滚会员索引')
    if (!value?.trim()) {
      message.warning('索引升级审计记录编号不能为空')
      return
    }
    await message.confirm(`将回滚升级记录 ${value} 对应的 Alias，是否继续？`)
    indexOperating.value = true
    await MemberUserSearchApi.rollbackIndex(value.trim())
    message.success('会员索引 Alias 已回滚')
    await Promise.all([getList(), getIndexAliases()])
  } catch {
    // 用户取消或接口异常时由请求拦截器提示。
  } finally {
    indexOperating.value = false
  }
}

/** 删除已脱离 Alias 的会员用户旧版本索引。 */
const handleDeleteHistoricalIndex = async () => {
  const index = selectedHistoricalIndex.value
  if (!index) {
    return
  }
  try {
    await message.confirm(`将永久删除 Elasticsearch 索引 ${index}，删除后不可恢复且不能再回滚至该版本，是否继续？`)
    indexOperating.value = true
    await MemberUserSearchApi.deleteHistoricalIndex(index)
    selectedHistoricalIndex.value = undefined
    message.success(`历史索引 ${index} 已删除`)
    await getIndexAliases()
  } catch {
    // 用户取消或接口异常时由请求拦截器提示。
  } finally {
    indexOperating.value = false
  }
}

onMounted(() => {
  getList()
  getIndexAliases()
})
</script>
