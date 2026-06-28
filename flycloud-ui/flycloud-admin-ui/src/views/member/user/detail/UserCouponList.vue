<template>
  <!-- 搜索工作栏 -->
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.member.user.detail.UserCouponList.k1f291968')"
          :end-placeholder="t('auto.views.member.user.detail.UserCouponList.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" />{{ t('common.search') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" />{{ t('common.reset') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <!-- Tab 选项：真正的内容在 Lab -->
    <el-tabs v-model="activeTab" type="card" @tab-change="onTabChange">
      <el-tab-pane
        v-for="tab in statusTabs"
        :key="tab.value"
        :label="tab.label"
        :name="tab.value"
      />
    </el-tabs>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.member.user.detail.UserCouponList.k338d0ddd')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.member.user.detail.UserCouponList.kc8d290b1')"
        align="center"
        prop="discountType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_DISCOUNT_TYPE" :value="scope.row.discountType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mall.promotion.coupon.template.CouponTemplateForm.k1527d9df')"
        align="center"
        prop="takeType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_COUPON_TAKE_TYPE" :value="scope.row.takeType" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PROMOTION_COUPON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.mall.promotion.coupon.index.k05fea3f9')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column
        :label="t('extra.k01100972')"
        align="center"
        prop="useTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column
        :label="t('common.operation')"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            v-hasPermi="['promotion:coupon:delete']"
            type="danger"
            link
            @click="handleDelete(scope.row.id)"
          >
            {{ t('extra.k7f9298c2') }}
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
</template>

<script setup lang="ts" name="UserCouponList">
import { deleteCoupon, getCouponPage } from '@/api/mall/promotion/coupon/coupon'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'UserCouponList' })

const { userId }: { userId: number } = defineProps({
  userId: {
    type: Number,
    required: true
  }
}) //用户编号

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 字典表格数据
// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  createTime: [],
  status: undefined,
  userIds: undefined
})
const queryFormRef = ref() // 搜索的表单

const activeTab = ref('all') // Tab 筛选
const statusTabs = reactive([
  {
    label: t('auto.views.member.user.detail.UserCouponList.k778fc8f9'),
    value: 'all'
  }
])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  // 执行查询
  try {
    queryParams.userIds = userId
    const data = await getCouponPage(queryParams)
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
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 二次确认
    await message.confirm(t('auto.views.member.user.detail.UserCouponList.k62865160'))
    // 发起删除
    await deleteCoupon(id)
    message.notifySuccess(t('auto.views.member.user.detail.UserCouponList.kc6670066'))
    // 重新加载列表
    await getList()
  } catch {}
}

/** tab 切换 */
const onTabChange = (tabName) => {
  queryParams.status = tabName === 'all' ? undefined : tabName
  getList()
}

/** 初始化 **/
onMounted(() => {
  getList()
  // 设置 statuses 过滤
  for (const dict of getIntDictOptions(DICT_TYPE.PROMOTION_COUPON_STATUS)) {
    statusTabs.push({
      label: dict.label,
      value: dict.value as string
    })
  }
})
</script>
