<template>
  <doc-alert
    :title="t('auto.views.mall.promotion.rewardActivity.index.k8c9d26ac')"
    url="https://doc.iocoder.cn/mall/promotion-record/"
  />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item
        :label="t('auto.views.mall.promotion.rewardActivity.index.k2b020286')"
        prop="name"
      >
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.promotion.rewardActivity.index.ka90d22e9')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.rewardActivity.index.k65a972d7')"
        prop="status"
      >
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.promotion.rewardActivity.index.k4b6989d1')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.rewardActivity.index.kabe0ecdb')"
        prop="createTime"
      >
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.mall.promotion.rewardActivity.index.kfb582984')"
          :start-placeholder="t('auto.views.mall.promotion.rewardActivity.index.kd8e8ee4c')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k29055e95') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k1710c1e1') }}
        </el-button>
        <el-button
          v-hasPermi="['promotion:reward-activity:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k58bff045') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" default-expand-all row-key="id">
      <el-table-column
        :label="t('auto.views.mall.promotion.rewardActivity.index.k2b020286')"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.rewardActivity.index.k0e49ed99')"
        prop="productScope"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_PRODUCT_SCOPE" :value="scope.row.productScope" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.k658b1a2e')"
        prop="startTime"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.mall.promotion.bargain.activity.bargainActivity_data.kcdd4e446')"
        prop="endTime"
      />
      <el-table-column align="center" :label="t('common.status')" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="180"
      />
      <el-table-column align="center" :label="t('common.operation')">
        <template #default="scope">
          <el-button
            v-hasPermi="['promotion:reward-activity:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['promotion:reward-activity:close']"
            link
            type="danger"
            @click="handleClose(scope.row.id)"
          >
            {{ t('common.close') }}
          </el-button>
          <el-button
            v-hasPermi="['promotion:reward-activity:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <RewardForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as RewardActivityApi from '@/api/mall/promotion/reward/rewardActivity'
import RewardForm from './RewardForm.vue'
const { t } = useI18n()
defineOptions({ name: 'PromotionRewardActivity' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref<any[]>([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await RewardActivityApi.getRewardActivityPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref<InstanceType<typeof RewardForm>>()
const openForm = (type: string, id?: number) => {
  formRef.value?.open(type, id)
}

/** 关闭按钮操作 */
const handleClose = async (id: number) => {
  try {
    // 关闭的二次确认
    await message.confirm(t('auto.views.mall.promotion.rewardActivity.index.k613b3a06'))
    // 发起关闭
    await RewardActivityApi.closeRewardActivity(id)
    message.success(t('auto.views.mall.promotion.rewardActivity.index.kf3cf9e86'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await RewardActivityApi.deleteRewardActivity(id)
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
