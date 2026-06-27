<template>
  <doc-alert
    :title="t('auto.views.mall.promotion.seckill.config.index.kb4b3d450')"
    url="https://doc.iocoder.cn/mall/promotion-seckill/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="108px"
    >
      <el-form-item
        :label="t('auto.views.mall.promotion.seckill.config.index.kb4a36dd0')"
        prop="name"
      >
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.mall.promotion.seckill.config.index.kc30b664c')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.seckill.config.index.k65a972d7')"
        prop="status"
      >
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.mall.promotion.seckill.config.index.k4b6989d1')"
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
          v-hasPermi="['promotion:seckill-config:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k199f0d70') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.mall.promotion.seckill.config.index.kb4a36dd0')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.seckill.config.index.k871bfb38')"
        align="center"
        prop="startTime"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.seckill.config.index.k52b5e70f')"
        align="center"
        prop="endTime"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.seckill.config.index.kc00cb496')"
        align="center"
        prop="sliderPicUrls"
      >
        <template #default="scope">
          <el-image
            class="h-40px max-w-40px"
            v-for="(url, index) in scope?.row.sliderPicUrls"
            :key="index"
            :src="url"
            :preview-src-list="scope?.row.sliderPicUrls"
            :initial-index="index"
            preview-teleported
          />
        </template>
      </el-table-column>
      <el-table-column label="活动状态" align="center" prop="status">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="0"
            :inactive-value="1"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['promotion:seckill-config:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['promotion:seckill-config:delete']"
          >
            删除
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
  <SeckillConfigForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { SeckillConfigApi, SeckillConfigVO } from '@/api/mall/promotion/seckill/seckillConfig.ts'
import SeckillConfigForm from './SeckillConfigForm.vue'
import { CommonStatusEnum } from '@/utils/constants'

/** 秒杀时段 列表 */
const { t } = useI18n()
defineOptions({ name: 'SeckillConfig' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<SeckillConfigVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SeckillConfigApi.getSeckillConfigPage(queryParams)
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
    await SeckillConfigApi.deleteSeckillConfig(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 修改用户状态 */
const handleStatusChange = async (row: SeckillConfigVO) => {
  try {
    // 修改状态的二次确认
    const text = row.status === CommonStatusEnum.ENABLE ? t('common.enabled') : t('common.disabled')
    await message.confirm(
      t('auto.views.mall.promotion.seckill.config.index.k2be38185') +
        text +
        '"' +
        row.name +
        t('auto.views.mall.promotion.seckill.config.index.k090c9105')
    )
    // 发起修改状态
    await SeckillConfigApi.updateSeckillConfigStatus(row.id, row.status)
    // 刷新列表
    await getList()
  } catch {
    // 取消后，进行恢复按钮
    row.status =
      row.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
