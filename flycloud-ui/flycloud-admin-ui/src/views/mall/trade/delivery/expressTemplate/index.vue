<template>
  <doc-alert
    :title="t('auto.views.mall.trade.delivery.expressTemplate.index.k13435b1a')"
    url="https://doc.iocoder.cn/mall/trade-delivery-express/"
  />

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item
        :label="t('auto.views.mall.trade.delivery.expressTemplate.index.kbbc511d0')"
        prop="name"
      >
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.mall.trade.delivery.expressTemplate.index.k86bd4450')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.trade.delivery.expressTemplate.index.k459460d6')"
        prop="chargeMode"
      >
        <el-select
          v-model="queryParams.chargeMode"
          :placeholder="t('auto.views.mall.trade.delivery.expressTemplate.index.k459460d6')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.EXPRESS_CHARGE_MODE)"
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
          v-hasPermi="['trade:delivery:express-template:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" />
          {{ t('extra.kf9542690') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.expressTemplate.index.k9f42dac6')"
        min-width="60"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.expressTemplate.index.kbbc511d0')"
        min-width="100"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.mall.trade.delivery.expressTemplate.index.k459460d6')"
        prop="chargeMode"
        min-width="100"
        align="center"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.EXPRESS_CHARGE_MODE" :value="scope.row.chargeMode" />
        </template>
      </el-table-column>
      <el-table-column label="排序" min-width="100" prop="sort" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['trade:delivery:express-template:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['trade:delivery:express-template:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <ExpressTemplateForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as DeliveryExpressTemplateApi from '@/api/mall/trade/delivery/expressTemplate'
import ExpressTemplateForm from './ExpressTemplateForm.vue'
const { t } = useI18n()
defineOptions({ name: 'DeliveryExpressTemplate' })

const message = useMessage() // 消息弹窗
const total = ref(0) // 列表的总页数
const loading = ref(true) // 列表的加载中
const list = ref<any[]>([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  chargeMode: undefined
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DeliveryExpressTemplateApi.getDeliveryExpressTemplatePage(queryParams)
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
    await DeliveryExpressTemplateApi.deleteDeliveryExpressTemplate(id)
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
