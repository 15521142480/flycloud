<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="65%">
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
          :label="t('auto.views.mall.promotion.coupon.components.CouponSelect.k040f6dcf')"
          prop="name"
        >
          <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            :placeholder="t('auto.views.mall.promotion.coupon.components.CouponSelect.k70a14fb5')"
            @keyup="handleQuery"
          />
        </el-form-item>
        <el-form-item
          :label="t('auto.views.mall.promotion.coupon.components.CouponSelect.k1b05dd75')"
          prop="discountType"
        >
          <el-select
            v-model="queryParams.discountType"
            class="!w-240px"
            clearable
            :placeholder="t('auto.views.mall.promotion.coupon.components.CouponSelect.ka363f12a')"
          >
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.PROMOTION_DISCOUNT_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">
            <Icon class="mr-5px" icon="ep:search" />
            {{ t('extra.kcfbac39f') }}
          </el-button>
          <el-button @click="resetQuery">
            <Icon class="mr-5px" icon="ep:refresh" />
            {{ t('extra.ked89c360') }}
          </el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column
          :label="t('auto.views.mall.promotion.coupon.components.CouponSelect.k040f6dcf')"
          min-width="140"
          prop="name"
        />
        <el-table-column
          :label="t('auto.views.mall.promotion.coupon.components.CouponSelect.ke4e46c72')"
          min-width="80"
          prop="productScope"
        >
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.PROMOTION_PRODUCT_SCOPE" :value="scope.row.productScope" />
          </template>
        </el-table-column>
        <el-table-column :label="t('extra.kf06ebf87')" min-width="100" prop="discount">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.PROMOTION_DISCOUNT_TYPE" :value="scope.row.discountType" />
            {{ discountFormat(scope.row) }}
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
            <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
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
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('extra.k008b8fcb')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import {
  discountFormat,
  remainedCountFormat,
  takeLimitCountFormat,
  validityTypeFormat
} from '@/views/mall/promotion/coupon/formatter'
import * as CouponTemplateApi from '@/api/mall/promotion/coupon/couponTemplate'
import { CouponTemplateTakeTypeEnum } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'CouponSelect' })

const props = defineProps<{
  multipleSelection?: CouponTemplateApi.CouponTemplateVO[]
  takeType: number // 领取方式
}>()
const emit = defineEmits<{
  (e: 'update:multipleSelection', v: CouponTemplateApi.CouponTemplateVO[]): void
  (e: 'change', v: CouponTemplateApi.CouponTemplateVO[]): void
}>()
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref(t('auto.views.mall.promotion.coupon.components.CouponSelect.k3acbce42')) // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 字典表格数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  discountType: null,
  canTakeTypes: [CouponTemplateTakeTypeEnum.USER.type] // 只获得直接领取的券
})
const queryFormRef = ref() // 搜索的表单
const selectedCouponList = ref<CouponTemplateApi.CouponTemplateVO[]>([]) // 选择的数据

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    // 执行查询
    queryParams.canTakeTypes = [props.takeType] as any
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

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  resetQuery()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

const handleSelectionChange = (val: CouponTemplateApi.CouponTemplateVO[]) => {
  if (props.multipleSelection) {
    emit('update:multipleSelection', val)
    return
  }
  selectedCouponList.value = val
}

const submitForm = () => {
  dialogVisible.value = false
  emit('change', selectedCouponList.value)
}
</script>
