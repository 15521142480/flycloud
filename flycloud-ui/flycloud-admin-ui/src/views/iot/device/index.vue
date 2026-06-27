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
      <el-form-item :label="t('auto.views.iot.device.index.k6cc98552')" prop="productId">
        <el-select
          v-model="queryParams.productId"
          :placeholder="t('auto.views.iot.device.index.k59a0d3d1')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="product in products"
            :key="product.id"
            :label="product.name"
            :value="product.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="DeviceName" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          :placeholder="t('auto.views.iot.device.index.k58560dae')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.iot.device.index.k90f8a65c')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.iot.device.index.k8167b2a5')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.iot.device.index.k47a991d1')" prop="deviceType">
        <el-select
          v-model="queryParams.deviceType"
          :placeholder="t('auto.views.iot.device.index.kd3646054')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_PRODUCT_DEVICE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.iot.device.index.k567a4c84')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.iot.device.index.k60e6fb6a')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_DEVICE_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />
          {{ t('extra.ka73f5111') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />
          {{ t('extra.kfb222501') }}
        </el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['iot:device:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" />
          {{ t('extra.kbd17ec6a') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="DeviceName" align="center" prop="deviceName">
        <template #default="scope">
          <el-link @click="openDetail(scope.row.id)">{{ scope.row.deviceName }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="备注名称" align="center" prop="name" />
      <el-table-column label="设备所属产品" align="center" prop="productId">
        <template #default="scope">
          {{ productMap[scope.row.productId] }}
        </template>
      </el-table-column>
      <el-table-column label="设备类型" align="center" prop="deviceType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.IOT_PRODUCT_DEVICE_TYPE" :value="scope.row.deviceType" />
        </template>
      </el-table-column>
      <el-table-column label="设备状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.IOT_DEVICE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        label="最后上线时间"
        align="center"
        prop="lastOnlineTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row.id)"
            v-hasPermi="['iot:product:query']"
          >
            查看
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['iot:device:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['iot:device:delete']"
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
  <DeviceForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { DeviceApi, DeviceVO } from '@/api/iot/device'
import DeviceForm from './DeviceForm.vue'
import { ProductApi } from '@/api/iot/product'

/** IoT 设备 列表 */
const { t } = useI18n()
defineOptions({ name: 'IoTDevice' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<DeviceVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  deviceName: undefined,
  productId: undefined,
  deviceType: undefined,
  name: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单

/** 产品标号和名称的映射 */
const productMap = reactive({})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DeviceApi.getDevicePage(queryParams)
    list.value = data.list
    total.value = data.total
    // 获取产品ID列表
    const productIds = [...new Set(data.list.map((device) => device.productId))]
    // 获取产品名称
    // TODO @haohao：最好后端拼接哈
    const products = await Promise.all(productIds.map((id) => ProductApi.getProduct(id)))
    products.forEach((product) => {
      productMap[product.id] = product.name
    })
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

/** 打开详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'IoTDeviceDetail', params: { id } })
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await DeviceApi.deleteDevice(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 查询字典下拉列表 */
const products = ref()
const getProducts = async () => {
  products.value = await ProductApi.getSimpleProductList()
}

/** 初始化 **/
onMounted(() => {
  getList()
  getProducts()
})
</script>
