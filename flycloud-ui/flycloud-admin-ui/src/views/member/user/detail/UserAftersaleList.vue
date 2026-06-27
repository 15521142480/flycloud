<template>
  <!-- 搜索 -->
  <ContentWrap>
    <el-form ref="queryFormRef" :inline="true" :model="queryParams" label-width="68px">
      <el-form-item
        :label="t('auto.views.member.user.detail.UserAftersaleList.k47b74133')"
        prop="spuName"
      >
        <el-input
          v-model="queryParams.spuName"
          class="!w-280px"
          clearable
          :placeholder="t('auto.views.member.user.detail.UserAftersaleList.k46c7a7ea')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserAftersaleList.kaa0737e3')"
        prop="no"
      >
        <el-input
          v-model="queryParams.no"
          class="!w-280px"
          clearable
          :placeholder="t('auto.views.member.user.detail.UserAftersaleList.k6e9e0e31')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserAftersaleList.k8c60a237')"
        prop="orderNo"
      >
        <el-input
          v-model="queryParams.orderNo"
          class="!w-280px"
          clearable
          :placeholder="t('auto.views.member.user.detail.UserAftersaleList.k703bbdd7')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserAftersaleList.k1357a380')"
        prop="status"
      >
        <el-select
          v-model="queryParams.status"
          class="!w-280px"
          clearable
          :placeholder="t('auto.views.member.user.detail.UserAftersaleList.kaafac0f5')"
        >
          <el-option
            :label="t('auto.views.member.user.detail.UserAftersaleList.k778fc8f9')"
            value="0"
          />
          <el-option
            v-for="dict in getDictOptions(DICT_TYPE.TRADE_AFTER_SALE_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserAftersaleList.k21718f30')"
        prop="way"
      >
        <el-select
          v-model="queryParams.way"
          class="!w-280px"
          clearable
          :placeholder="t('auto.views.member.user.detail.UserAftersaleList.kf7e2ae13')"
        >
          <el-option
            v-for="dict in getDictOptions(DICT_TYPE.TRADE_AFTER_SALE_WAY)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.detail.UserAftersaleList.k3bf5ee94')"
        prop="type"
      >
        <el-select
          v-model="queryParams.type"
          class="!w-280px"
          clearable
          :placeholder="t('auto.views.member.user.detail.UserAftersaleList.k2a7ecc3c')"
        >
          <el-option
            v-for="dict in getDictOptions(DICT_TYPE.TRADE_AFTER_SALE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-280px"
          :end-placeholder="t('auto.views.member.user.detail.UserAftersaleList.k935f547a')"
          :start-placeholder="t('auto.views.member.user.detail.UserAftersaleList.k935f547a')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k887fa182') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k28219ac1') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-tabs v-model="queryParams.status" @tab-click="tabClick">
      <el-tab-pane
        v-for="item in statusTabs"
        :key="item.label"
        :label="item.label"
        :name="item.value"
      />
    </el-tabs>
    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column
        align="center"
        :label="t('auto.views.member.user.detail.UserAftersaleList.kaa0737e3')"
        min-width="200"
        prop="no"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.member.user.detail.UserAftersaleList.k8c60a237')"
        min-width="200"
        prop="orderNo"
      >
        <template #default="{ row }">
          <el-button link type="primary" @click="openOrderDetail(row.orderId)">
            {{ row.orderNo }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="商品信息" min-width="600" prop="spuName">
        <template #default="{ row }">
          <div class="flex items-center">
            <el-image
              :src="row.picUrl"
              class="mr-10px h-30px w-30px"
              @click="imagePreview(row.picUrl)"
            />
            <span class="mr-10px">{{ row.spuName }}</span>
            <el-tag v-for="property in row.properties" :key="property.propertyId" class="mr-10px">
              {{ property.propertyName }}: {{ property.valueName }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="订单金额" min-width="120" prop="refundPrice">
        <template #default="scope">
          <span>{{ fenToYuan(scope.row.refundPrice) }} 元</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="申请时间" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDate(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="售后状态" width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.TRADE_AFTER_SALE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="售后方式">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.TRADE_AFTER_SALE_WAY" :value="scope.row.way" />
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="120">
        <template #default="{ row }">
          <el-button link type="primary" @click="openAfterSaleDetail(row.id)">处理退款</el-button>
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
</template>
<script lang="ts" setup>
import * as AfterSaleApi from '@/api/mall/trade/afterSale/index'
import { DICT_TYPE, getDictOptions } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { createImageViewer } from '@/components/ImageViewer'
import { TabsPaneContext } from 'element-plus'
import { cloneDeep } from 'lodash-es'
import { fenToYuan } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'UserAfterSaleList' })

const { push } = useRouter() // 路由跳转
const props = defineProps<{
  userId: number
}>()
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref<AfterSaleApi.TradeAfterSaleVO[]>([]) // 列表的数据
const statusTabs = ref([
  {
    label: t('auto.views.member.user.detail.UserAftersaleList.k778fc8f9'),
    value: '0'
  }
])
const queryFormRef = ref() // 搜索的表单
// 查询参数
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  no: null,
  status: '0',
  orderNo: null,
  spuName: null,
  createTime: [],
  way: null,
  type: null,
  userId: null
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = cloneDeep(queryParams.value)
    // 处理掉全部的状态，不传就是全部
    if (data.status === '0') {
      delete data.status
    }
    // 执行查询
    if (props.userId) {
      data.userId = props.userId as any
    }
    const res = await AfterSaleApi.getAfterSalePage(data)
    list.value = res.list as AfterSaleApi.TradeAfterSaleVO[]
    total.value = res.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = async () => {
  queryParams.value.pageNum = 1
  await getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** tab 切换 */
const tabClick = async (tab: TabsPaneContext) => {
  queryParams.value.status = tab.paneName as any
  await getList()
}

/** 处理退款 */
const openAfterSaleDetail = (id: number) => {
  push({ name: 'TradeAfterSaleDetail', params: { id } })
}

/** 查看订单详情 */
const openOrderDetail = (id: number) => {
  push({ name: 'TradeOrderDetail', params: { id } })
}

/** 商品图预览 */
const imagePreview = (imgUrl: string) => {
  createImageViewer({
    urlList: [imgUrl]
  })
}

onMounted(async () => {
  await getList()
  // 设置 statuses 过滤
  for (const dict of getDictOptions(DICT_TYPE.TRADE_AFTER_SALE_STATUS)) {
    statusTabs.value.push({
      label: dict.label,
      value: dict.value
    })
  }
})
</script>
