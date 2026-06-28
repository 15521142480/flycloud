<template>
  <ContentWrap>
    <!-- 订单信息 -->
    <el-descriptions :title="t('auto.views.mall.trade.order.detail.index.k43b06de2')">
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.k9820edd2')">{{
        formData.no
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.k2a120ba7')">{{
        formData?.user?.name
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.kccc1043a')">
        <dict-tag :type="DICT_TYPE.TRADE_ORDER_TYPE" :value="formData.type!" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.k6c8104c1')">
        <dict-tag :type="DICT_TYPE.TERMINAL" :value="formData.terminal!" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.ke3fabec0')">{{
        formData.userRemark
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.k6718a4d1')">{{
        formData.remark
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.kfa7bd390')">{{
        formData.payOrderId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.k835299d9')">
        <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="formData.payChannelCode!" />
      </el-descriptions-item>
      <el-descriptions-item
        v-if="formData.brokerageUser"
        :label="t('auto.views.mall.trade.order.detail.index.kb0521549')"
      >
        {{ formData.brokerageUser?.name }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 订单状态 -->
    <el-descriptions :column="1" :title="t('auto.views.mall.trade.order.detail.index.k4e4ca9ca')">
      <el-descriptions-item :label="t('auto.views.mall.trade.order.detail.index.k9d97842b')">
        <dict-tag :type="DICT_TYPE.TRADE_ORDER_STATUS" :value="formData.status!" />
      </el-descriptions-item>
      <el-descriptions-item v-hasPermi="['trade:order:update']" label-class-name="no-colon">
        <el-button
          v-if="formData.status! === TradeOrderStatusEnum.UNPAID.status"
          type="primary"
          @click="updatePrice"
        >
          {{ t('extra.k8ca82085') }}
        </el-button>
        <el-button type="primary" @click="remark">{{ t('common.remark') }}</el-button>
        <!-- 待发货 -->
        <template v-if="formData.status! === TradeOrderStatusEnum.UNDELIVERED.status">
          <!-- 快递发货 -->
          <el-button
            v-if="formData.deliveryType === DeliveryTypeEnum.EXPRESS.type"
            type="primary"
            @click="delivery"
          >
            {{ t('extra.k3ddeb39f') }}
          </el-button>
          <el-button
            v-if="formData.deliveryType === DeliveryTypeEnum.EXPRESS.type"
            type="primary"
            @click="updateAddress"
          >
            {{ t('extra.k1dfdf0dc') }}
          </el-button>
          <!-- 到店自提 -->
          <el-button
            v-if="formData.deliveryType === DeliveryTypeEnum.PICK_UP.type && showPickUp"
            type="primary"
            @click="handlePickUp"
          >
            {{ t('extra.k1f9adbed') }}
          </el-button>
        </template>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label
          ><span style="color: red"
            >{{ t('auto.views.mall.trade.afterSale.detail.index.ka2af2599') }}
          </span></template
        >
        {{ t('extra.kec41fc89') }}<br />
        {{ t('extra.k32ca1b49') }} <br />
        {{ t('extra.k8de769ef') }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 商品信息 -->
    <el-descriptions :title="t('extra.k7519f060')">
      <el-descriptions-item labelClassName="no-colon">
        <el-row :gutter="20">
          <el-col :span="15">
            <el-table :data="formData.items" border>
              <el-table-column :label="t('extra.kf8353994')" prop="spuName" width="auto">
                <template #default="{ row }">
                  {{ row.spuName }}
                  <el-tag v-for="property in row.properties" :key="property.propertyId">
                    {{ property.propertyName }}: {{ property.valueName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column :label="t('extra.ke34fd103')" prop="price" width="150">
                <template #default="{ row }"
                  >{{ fenToYuan(row.price) }}{{ t('extra.k2d94e2e7') }}</template
                >
              </el-table-column>
              <el-table-column
                :label="t('auto.views.erp.stock.check.index.kb9ae8931')"
                prop="count"
                width="100"
              />
              <el-table-column
                :label="
                  t('auto.views.erp.finance.payment.components.FinancePaymentItemForm.k92bcbf71')
                "
                prop="payPrice"
                width="150"
              >
                <template #default="{ row }"
                  >{{ fenToYuan(row.payPrice) }}{{ t('extra.k2d94e2e7') }}</template
                >
              </el-table-column>
              <el-table-column :label="t('extra.k81df0241')" prop="afterSaleStatus" width="120">
                <template #default="{ row }">
                  <dict-tag
                    :type="DICT_TYPE.TRADE_ORDER_ITEM_AFTER_SALE_STATUS"
                    :value="row.afterSaleStatus"
                  />
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="10" />
        </el-row>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="4">
      <!-- 第一层 -->
      <el-descriptions-item :label="t('extra.k6eb0c208')">
        {{ fenToYuan(formData.totalPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('extra.kd0e9e5f9')">
        {{ fenToYuan(formData.deliveryPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('extra.kbf367808')">
        {{ fenToYuan(formData.adjustPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <el-descriptions-item v-for="item in 1" :key="item" label-class-name="no-colon" />
      <!-- 第二层 -->
      <el-descriptions-item>
        <template #label
          ><span style="color: red">{{ t('extra.kc467084a') }} </span></template
        >
        {{ fenToYuan(formData.couponPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label
          ><span style="color: red">{{ t('extra.k2ce8b041') }} </span></template
        >
        {{ fenToYuan(formData.vipPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label
          ><span style="color: red">{{ t('extra.k46f1bb8f') }} </span></template
        >
        {{ fenToYuan(formData.discountPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label
          ><span style="color: red">{{ t('extra.k87c5d5ef') }} </span></template
        >
        {{ fenToYuan(formData.pointPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
      <!-- 第三层 -->
      <el-descriptions-item v-for="item in 3" :key="item" label-class-name="no-colon" />
      <el-descriptions-item :label="t('extra.k864f6636')">
        {{ fenToYuan(formData.payPrice!) }} {{ t('extra.k2d94e2e7') }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 物流信息 -->
    <el-descriptions :column="4" :title="t('extra.kcf5122d3')">
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k8e7a820e')">
        <dict-tag :type="DICT_TYPE.TRADE_DELIVERY_TYPE" :value="formData.deliveryType!" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k022b027f')">{{
        formData.receiverName
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.ka90ca165')">{{
        formData.receiverMobile
      }}</el-descriptions-item>
      <!-- 快递配送 -->
      <div v-if="formData.deliveryType === DeliveryTypeEnum.EXPRESS.type">
        <el-descriptions-item v-if="formData.receiverDetailAddress" :label="t('extra.k371a3b91')">
          {{ formData.receiverAreaName }} {{ formData.receiverDetailAddress }}
          <el-link
            v-clipboard:copy="formData.receiverAreaName + ' ' + formData.receiverDetailAddress"
            v-clipboard:success="clipboardSuccess"
            icon="ep:document-copy"
            type="primary"
          />
        </el-descriptions-item>
        <el-descriptions-item v-if="formData.logisticsId" :label="t('extra.k8d274e36')">
          {{ deliveryExpressList.find((item) => item.id === formData.logisticsId)?.name }}
        </el-descriptions-item>
        <el-descriptions-item v-if="formData.logisticsId" :label="t('extra.ke3479963')">
          {{ formData.logisticsNo }}
        </el-descriptions-item>
        <el-descriptions-item v-if="formatDate.deliveryTime" :label="t('extra.k21524c74')">
          {{ formatDate(formData.deliveryTime) }}
        </el-descriptions-item>
        <el-descriptions-item v-for="item in 2" :key="item" label-class-name="no-colon" />
        <el-descriptions-item v-if="expressTrackList.length > 0" :label="t('extra.kbb77c761')">
          <el-timeline>
            <el-timeline-item
              v-for="(express, index) in expressTrackList"
              :key="index"
              :timestamp="formatDate(express.time)"
            >
              {{ express.content }}
            </el-timeline-item>
          </el-timeline>
        </el-descriptions-item>
      </div>
      <!-- 自提门店 -->
      <div v-if="formData.deliveryType === DeliveryTypeEnum.PICK_UP.type">
        <el-descriptions-item v-if="formData.pickUpStoreId" :label="t('extra.ke01ab7e6')">
          {{ pickUpStore?.name }}
        </el-descriptions-item>
      </div>
    </el-descriptions>

    <!-- 订单日志 -->
    <el-descriptions :title="t('extra.k03bc5ae7')">
      <el-descriptions-item labelClassName="no-colon">
        <el-timeline>
          <el-timeline-item
            v-for="(log, index) in formData.logs"
            :key="index"
            :timestamp="formatDate(log.createTime!)"
            placement="top"
          >
            <div class="el-timeline-right-content">
              {{ log.content }}
            </div>
            <template #dot>
              <span
                :style="{ backgroundColor: getUserTypeColor(log.userType!) }"
                class="dot-node-style"
              >
                {{ getDictLabel(DICT_TYPE.USER_TYPE, log.userType)[0] }}
              </span>
            </template>
          </el-timeline-item>
        </el-timeline>
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>

  <!-- 各种操作的弹窗 -->
  <OrderDeliveryForm ref="deliveryFormRef" @success="getDetail" />
  <OrderUpdateRemarkForm ref="updateRemarkForm" @success="getDetail" />
  <OrderUpdateAddressForm ref="updateAddressFormRef" @success="getDetail" />
  <OrderUpdatePriceForm ref="updatePriceFormRef" @success="getDetail" />
</template>
<script lang="ts" setup>
import * as TradeOrderApi from '@/api/mall/trade/order'
import { fenToYuan } from '@/utils'
import { formatDate } from '@/utils/formatTime'
import { DICT_TYPE, getDictLabel, getDictObj } from '@/utils/dict'
import OrderUpdateRemarkForm from '@/views/mall/trade/order/form/OrderUpdateRemarkForm.vue'
import OrderDeliveryForm from '@/views/mall/trade/order/form/OrderDeliveryForm.vue'
import OrderUpdateAddressForm from '@/views/mall/trade/order/form/OrderUpdateAddressForm.vue'
import OrderUpdatePriceForm from '@/views/mall/trade/order/form/OrderUpdatePriceForm.vue'
import * as DeliveryExpressApi from '@/api/mall/trade/delivery/express'
import { useTagsViewStore } from '@/store/modules/tagsView'
import { DeliveryTypeEnum, TradeOrderStatusEnum } from '@/utils/constants'
import * as DeliveryPickUpStoreApi from '@/api/mall/trade/delivery/pickUpStore'
import { propTypes } from '@/utils/propTypes'
const { t } = useI18n()
defineOptions({ name: 'TradeOrderDetail' })

const message = useMessage() // 消息弹窗

/** 获得 userType 颜色 */
const getUserTypeColor = (type: number) => {
  const dict = getDictObj(DICT_TYPE.USER_TYPE, type)
  switch (dict?.colorType) {
    case 'success':
      return '#67C23A'
    case 'info':
      return '#909399'
    case 'warning':
      return '#E6A23C'
    case 'danger':
      return '#F56C6C'
  }
  return '#409EFF'
}

// 订单详情
const formData = ref<TradeOrderApi.OrderVO>({
  logs: []
})

/** 各种操作 */
const updateRemarkForm = ref() // 订单备注表单 Ref
const remark = () => {
  updateRemarkForm.value?.open(formData.value)
}
const deliveryFormRef = ref() // 发货表单 Ref
const delivery = () => {
  deliveryFormRef.value?.open(formData.value)
}
const updateAddressFormRef = ref() // 收货地址表单 Ref
const updateAddress = () => {
  updateAddressFormRef.value?.open(formData.value)
}
const updatePriceFormRef = ref() // 订单调价表单 Ref
const updatePrice = () => {
  updatePriceFormRef.value?.open(formData.value)
}

/** 核销 */
const handlePickUp = async () => {
  try {
    // 二次确认
    await message.confirm(t('auto.views.mall.trade.order.detail.index.k4fcfc596'))
    // 提交
    await TradeOrderApi.pickUpOrder(formData.value.id!)
    message.success(t('auto.views.mall.trade.order.detail.index.k6a980a8c'))
    // 刷新列表
    await getDetail()
  } catch {}
}

/** 获得详情 */
const { params } = useRoute() // 查询参数
const props = defineProps({
  id: propTypes.number.def(undefined), // 订单ID
  showPickUp: propTypes.bool.def(true) // 显示核销按钮
})
const id = (params.id || props.id) as unknown as number
const getDetail = async () => {
  if (id) {
    const res = (await TradeOrderApi.getOrder(id)) as TradeOrderApi.OrderVO
    // 没有表单信息则关闭页面返回
    if (!res) {
      message.error(t('auto.views.mall.trade.order.detail.index.kd639df2e'))
      close()
    }
    formData.value = res
  }
}

/** 关闭 tag */
const { delView } = useTagsViewStore() // 视图操作
const { push, currentRoute } = useRouter() // 路由
const close = () => {
  delView(unref(currentRoute))
  push({ name: 'TradeOrder' })
}

/** 复制 */
const clipboardSuccess = () => {
  message.success(t('auto.views.mall.trade.order.detail.index.kc1ef062e'))
}

/** 初始化 **/
const deliveryExpressList = ref([]) // 物流公司
const expressTrackList = ref([]) // 物流详情
const pickUpStore = ref({}) // 自提门店
onMounted(async () => {
  await getDetail()
  // 如果配送方式为快递，则查询物流公司
  if (formData.value.deliveryType === DeliveryTypeEnum.EXPRESS.type) {
    deliveryExpressList.value = await DeliveryExpressApi.getSimpleDeliveryExpressList()
    if (form.value.logisticsId) {
      expressTrackList.value = await TradeOrderApi.getExpressTrackList(formData.value.id!)
    }
  } else if (formData.value.deliveryType === DeliveryTypeEnum.PICK_UP.type) {
    pickUpStore.value = await DeliveryPickUpStoreApi.getDeliveryPickUpStore(
      formData.value.pickUpStoreId
    )
  }
})
</script>
<style lang="scss" scoped>
:deep(.el-descriptions) {
  &:not(:nth-child(1)) {
    margin-top: 20px;
  }

  .el-descriptions__title {
    display: flex;
    align-items: center;

    &::before {
      display: inline-block;
      width: 3px;
      height: 20px;
      margin-right: 10px;
      background-color: #409eff;
      content: '';
    }
  }

  .el-descriptions-item__container {
    margin: 0 10px;

    .no-colon {
      margin: 0;

      &::after {
        content: '';
      }
    }
  }
}

// 时间线样式调整
:deep(.el-timeline) {
  margin: 10px 0 0 160px;

  .el-timeline-item__wrapper {
    position: relative;
    top: -20px;

    .el-timeline-item__timestamp {
      position: absolute !important;
      top: 10px;
      left: -150px;
    }
  }

  .el-timeline-right-content {
    display: flex;
    align-items: center;
    min-height: 30px;
    padding: 10px;
    border-radius: var(--el-card-border-radius);
    background-color: var(--app-content-bg-color);

    &::before {
      position: absolute;
      top: 10px;
      left: 13px; /* 将伪元素水平居中 */
      border-color: transparent var(--app-content-bg-color) transparent transparent; /* 尖角颜色，左侧朝向 */
      border-style: solid;
      border-width: 8px; /* 调整尖角大小 */
      content: ''; /* 必须设置 content 属性 */
    }
  }

  .dot-node-style {
    position: absolute;
    left: -5px;
    display: flex;
    width: 20px;
    height: 20px;
    font-size: 10px;
    color: #fff;
    border-radius: 50%;
    justify-content: center;
    align-items: center;
  }
}
</style>
