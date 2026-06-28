<template>
  <!--  <doc-alert :title="t('extra.k49b1160b')" url="https://doc.iocoder.cn/mall/promotion-coupon/" />-->

  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="82px"
    >
      <el-form-item
        :label="t('auto.views.mall.promotion.coupon.template.index.k040f6dcf')"
        prop="name"
      >
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.promotion.coupon.template.index.k70a14fb5')"
          @keyup="handleQuery"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.coupon.template.index.k1b05dd75')"
        prop="discountType"
      >
        <el-select
          v-model="queryParams.discountType"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.promotion.coupon.template.index.ka363f12a')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.PROMOTION_DISCOUNT_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.coupon.template.index.k0043186e')"
        prop="status"
      >
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.mall.promotion.coupon.template.index.k83e404c0')"
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
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.mall.promotion.coupon.template.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.mall.promotion.coupon.template.index.k1f291968')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k3fa60718') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.kfd2922cf') }}
        </el-button>
        <el-button
          v-hasPermi="['promotion:coupon-template:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k1f1a9904') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.mall.promotion.coupon.template.index.k040f6dcf')"
        min-width="140"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.mall.promotion.coupon.template.index.ke4e46c72')"
        min-width="130"
        prop="productScope"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_PRODUCT_SCOPE" :value="scope.row.productScope" />
        </template>
      </el-table-column>
      <el-table-column :label="t('extra.kf06ebf87')" min-width="110" prop="discount">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_DISCOUNT_TYPE" :value="scope.row.discountType" />
          <div>{{ discountFormat(scope.row) }}</div>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mall.promotion.coupon.template.CouponTemplateForm.k1527d9df')"
        min-width="100"
        prop="takeType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_COUPON_TAKE_TYPE" :value="scope.row.takeType" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="validityTypeFormat"
        align="center"
        :label="t('extra.k01100972')"
        prop="validityType"
        width="185"
      />
      <el-table-column
        :formatter="totalCountFormat"
        align="center"
        :label="t('auto.views.mall.promotion.coupon.template.CouponTemplateForm.k54d6b5f2')"
        prop="totalCount"
      />
      <el-table-column
        :formatter="remainedCountFormat"
        align="center"
        :label="t('auto.views.mall.promotion.coupon.components.CouponSendForm.k0ca28093')"
        prop="totalCount"
      />
      <el-table-column
        :formatter="takeLimitCountFormat"
        align="center"
        :label="t('extra.k666e6fea')"
        prop="takeLimitCount"
      />
      <el-table-column align="center" :label="t('common.status')" prop="status">
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
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="180"
      />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        :label="t('common.operation')"
        width="120"
      >
        <template #default="scope">
          <el-button
            v-hasPermi="['promotion:coupon-template:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('extra.k4c512392') }}
          </el-button>
          <el-button
            v-hasPermi="['promotion:coupon-template:delete']"
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
  <CouponTemplateForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import * as CouponTemplateApi from '@/api/mall/promotion/coupon/couponTemplate'
import { CommonStatusEnum } from '@/utils/constants'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import CouponTemplateForm from './CouponTemplateForm.vue'
import {
  discountFormat,
  remainedCountFormat,
  takeLimitCountFormat,
  totalCountFormat,
  validityTypeFormat
} from '@/views/mall/promotion/coupon/formatter'
const { t } = useI18n()
defineOptions({ name: 'PromotionCouponTemplate' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 字典表格数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  status: null,
  discountType: null,
  type: null,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    // 执行查询
    const data = await CouponTemplateApi.getCouponTemplatePage(queryParams)
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
  queryFormRef?.value?.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 优惠劵模板状态修改 */
const handleStatusChange = async (row: any) => {
  // 此时，row 已经变成目标状态了，所以可以直接提交请求和提示
  let text = row.status === CommonStatusEnum.ENABLE ? t('common.enabled') : t('common.disabled')

  try {
    await message.confirm(
      t('auto.views.mall.promotion.coupon.template.index.ka4db1fb1') +
        text +
        '""' +
        row.name +
        t('auto.views.mall.promotion.coupon.template.index.k540d5cd4')
    )
    await CouponTemplateApi.updateCouponTemplateStatus(row.id, row.status)
    message.success(text + t('common.success'))
  } catch {
    // 异常时，需要将 row.status 状态重置回之前的
    row.status =
      row.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
  }
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.confirm(
      t('auto.views.mall.promotion.coupon.template.index.k8feee452') +
        id +
        t('auto.views.mall.promotion.coupon.template.index.kc87f4c07')
    )
    // 发起删除
    await CouponTemplateApi.deleteCouponTemplate(id)
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
