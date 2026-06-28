<template>
  <doc-alert
    :title="t('auto.views.mall.trade.delivery.pickUpStore.index.k13435b1a')"
    url="https://doc.iocoder.cn/mall/trade-delivery-express/"
  />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form ref="queryFormRef" :inline="true" :model="queryParams" class="-mb-15px">
      <el-form-item
        :label="t('auto.views.mall.trade.delivery.pickUpStore.index.kd25150b2')"
        prop="phone"
      >
        <el-input
          v-model="queryParams.phone"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.trade.delivery.pickUpStore.index.k96651c34')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.delivery.pickUpStore.index.k7bfb547b')"
        prop="name"
      >
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.trade.delivery.pickUpStore.index.k33434707')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.delivery.pickUpStore.index.k6016f100')"
        prop="status"
      >
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.trade.delivery.pickUpStore.index.k6016f100')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          class="!w-240px"
          :end-placeholder="t('auto.views.mall.trade.delivery.pickUpStore.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.mall.trade.delivery.pickUpStore.index.k1f291968')"
          type="datetimerange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k44a85076') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k07371200') }}
        </el-button>
        <el-button
          v-hasPermi="['trade:delivery:pick-up-store:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.kca61db51') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.pickUpStore.index.k9f42dac6')"
        min-width="80"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.pickUpStore.index.k92ac903c')"
        min-width="100"
        prop="logo"
      >
        <template #default="scope">
          <img
            v-if="scope.row.logo"
            :src="scope.row.logo"
            :alt="t('auto.views.mall.trade.delivery.pickUpStore.index.k92ac903c')"
            class="h-50px"
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.pickUpStore.PickUpStoreForm.k7bfb547b')"
        min-width="150"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.pickUpStore.PickUpStoreForm.kd25150b2')"
        min-width="100"
        prop="phone"
      />
      <el-table-column
        :label="t('auto.views.crm.clue.ClueForm.k67d2d797')"
        min-width="100"
        prop="detailAddress"
      />
      <el-table-column :label="t('extra.kcc3307c8')" min-width="180">
        <template #default="scope">
          {{ scope.row.openingTime }} ~ {{ scope.row.closingTime }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k6bbda1b1')"
        min-width="100"
        prop="status"
      >
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
            v-hasPermi="['trade:delivery:pick-up-store:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['trade:delivery:pick-up-store:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>
  <!-- 表单弹窗：添加/修改 -->
  <DeliveryPickUpStoreForm ref="formRef" @success="getList" />
</template>
<script lang="ts" name="DeliveryPickUpStore" setup>
import * as DeliveryPickUpStoreApi from '@/api/mall/trade/delivery/pickUpStore'
import DeliveryPickUpStoreForm from './PickUpStoreForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const total = ref(0) // 列表的总页数
const loading = ref(true) // 列表的加载中
const list = ref<any[]>([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: undefined,
  phone: undefined,
  name: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

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
    await DeliveryPickUpStoreApi.deleteDeliveryPickUpStore(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DeliveryPickUpStoreApi.getDeliveryPickUpStorePage(queryParams)
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
