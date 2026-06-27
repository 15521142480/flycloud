<template>
  <ContentWrap>
    <!-- 订单信息 -->
    <el-descriptions :title="t('auto.views.mall.trade.afterSale.detail.index.k43b06de2')">
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k9820edd2')">{{
        formData.orderNo
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k8e7a820e')">
        <dict-tag :type="DICT_TYPE.TRADE_DELIVERY_TYPE" :value="formData.order.deliveryType" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kccc1043a')">
        <dict-tag :type="DICT_TYPE.TRADE_ORDER_TYPE" :value="formData.order.type" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k022b027f')">
        {{ formData.order.receiverName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.ke3fabec0')">
        {{ formData.order.userRemark }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k6c8104c1')">
        <dict-tag :type="DICT_TYPE.TERMINAL" :value="formData.order.terminal" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.ka90ca165')">
        {{ formData.order.receiverMobile }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k6718a4d1')">{{
        formData.order.remark
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kfa7bd390')">
        {{ formData.order.payOrderId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k835299d9')">
        <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="formData.order.payChannelCode" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k2a120ba7')">{{
        formData?.user?.name
      }}</el-descriptions-item>
    </el-descriptions>

    <!-- 售后信息 -->
    <el-descriptions :title="t('auto.views.mall.trade.afterSale.detail.index.k18e5909d')">
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kbe0b3129')">{{
        formData.no
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kba6b1f3f')">
        {{ formatDate(formData.auditTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kecc3b8d1')">
        <dict-tag :type="DICT_TYPE.TRADE_AFTER_SALE_TYPE" :value="formData.type" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kdda7dde9')">
        <dict-tag :type="DICT_TYPE.TRADE_AFTER_SALE_WAY" :value="formData.way" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k14fd98b9')">
        {{ fenToYuan(formData.refundPrice) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kdb1dc7b8')">{{
        formData.applyReason
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.k6994971e')">
        {{ formData.applyDescription }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.ke683f045')">
        <el-image
          v-for="(item, index) in formData.applyPicUrls"
          :key="index"
          :src="item.url"
          class="mr-10px h-60px w-60px"
          @click="imagePreview(formData.applyPicUrls)"
        />
      </el-descriptions-item>
    </el-descriptions>

    <!-- 退款状态 -->
    <el-descriptions
      :column="1"
      :title="t('auto.views.mall.trade.afterSale.detail.index.k7d1f4b47')"
    >
      <el-descriptions-item :label="t('auto.views.mall.trade.afterSale.detail.index.kbef630bd')">
        <dict-tag :type="DICT_TYPE.TRADE_AFTER_SALE_STATUS" :value="formData.status" />
      </el-descriptions-item>
      <el-descriptions-item label-class-name="no-colon">
        <el-button v-if="formData.status === 10" type="primary" @click="agree">{{
          t('auto.views.mall.trade.afterSale.detail.index.ka068fe4d')
        }}</el-button>
        <el-button v-if="formData.status === 10" type="primary" @click="disagree">
          {{ t('extra.k168e8c06') }}
        </el-button>
        <el-button v-if="formData.status === 30" type="primary" @click="receive">
          {{ t('extra.k1a177309') }}
        </el-button>
        <el-button v-if="formData.status === 30" type="primary" @click="refuse">{{
          t('auto.views.mall.trade.afterSale.detail.index.k45189b49')
        }}</el-button>
        <el-button v-if="formData.status === 40" type="primary" @click="refund">{{
          t('auto.views.mall.trade.afterSale.detail.index.k75452d6d')
        }}</el-button>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label
          ><span style="color: red"
            >{{ t('auto.views.mall.trade.afterSale.detail.index.ka2af2599') }}
          </span></template
        >
        {{ t('extra.k60167873') }}<br />
        {{ t('extra.k678cfc90') }}<br />
        {{ t('extra.k4b72e03e') }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 商品信息 -->
    <el-descriptions :title="t('extra.k9df39a91')">
      <el-descriptions-item labelClassName="no-colon">
        <el-row :gutter="20">
          <el-col :span="15">
            <el-table :data="[formData.orderItem]" border>
              <el-table-column :label="t('extra.kf8353994')" prop="spuName" width="auto">
                <template #default="{ row }">
                  {{ row.spuName }}
                  <el-tag v-for="property in row.properties" :key="property.propertyId">
                    {{ property.propertyName }}: {{ property.valueName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="商品原价" prop="price" width="150">
                <template #default="{ row }">{{ fenToYuan(row.price) }} 元</template>
              </el-table-column>
              <el-table-column label="数量" prop="count" width="100" />
              <el-table-column label="合计" prop="payPrice" width="150">
                <template #default="{ row }">{{ fenToYuan(row.payPrice) }} 元</template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="10" />
        </el-row>
      </el-descriptions-item>
    </el-descriptions>

    <!-- 操作日志 -->
    <el-descriptions title="售后日志">
      <el-descriptions-item labelClassName="no-colon">
        <el-timeline>
          <el-timeline-item
            v-for="saleLog in formData.logs"
            :key="saleLog.id"
            :timestamp="formatDate(saleLog.createTime)"
            placement="top"
          >
            <div class="el-timeline-right-content">
              <span>{{ saleLog.content }}</span>
            </div>
            <template #dot>
              <span
                :style="{ backgroundColor: getUserTypeColor(saleLog.userType) }"
                class="dot-node-style"
              >
                {{ getDictLabel(DICT_TYPE.USER_TYPE, saleLog.userType)[0] || '系' }}
              </span>
            </template>
          </el-timeline-item>
        </el-timeline>
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>

  <!-- 各种操作的弹窗 -->
  <UpdateAuditReasonForm ref="updateAuditReasonFormRef" @success="getDetail" />
</template>
<script lang="ts" setup>
import * as AfterSaleApi from '@/api/mall/trade/afterSale/index'
import { fenToYuan } from '@/utils'
import { DICT_TYPE, getDictLabel, getDictObj } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import UpdateAuditReasonForm from '@/views/mall/trade/afterSale/form/AfterSaleDisagreeForm.vue'
import { createImageViewer } from '@/components/ImageViewer'
import { isArray } from '@/utils/is'
import { useTagsViewStore } from '@/store/modules/tagsView'
const { t } = useI18n()
defineOptions({ name: 'TradeAfterSaleDetail' })
const message = useMessage() // 消息弹窗
const { params } = useRoute() // 查询参数
const { push, currentRoute } = useRouter() // 路由
const formData = ref({
  order: {},
  logs: []
})
const updateAuditReasonFormRef = ref() // 拒绝售后表单 Ref

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

/** 获得详情 */
const getDetail = async () => {
  const id = params.id as unknown as number
  if (id) {
    const res = await AfterSaleApi.getAfterSale(id)
    // 没有表单信息则关闭页面返回
    if (res == null) {
      message.notifyError(t('auto.views.mall.trade.afterSale.detail.index.k442c0159'))
      close()
    }
    formData.value = res
  }
}

/** 同意售后 */
const agree = async () => {
  try {
    // 二次确认
    await message.confirm(t('auto.views.mall.trade.afterSale.detail.index.kcfaa5627'))
    await AfterSaleApi.agree(formData.value.id)
    // 提示成功
    message.success(t('common.success'))
    await getDetail()
  } catch {}
}

/** 拒绝售后 */
const disagree = async () => {
  updateAuditReasonFormRef.value?.open(formData.value)
}

/** 确认收货 */
const receive = async () => {
  try {
    // 二次确认
    await message.confirm(t('auto.views.mall.trade.afterSale.detail.index.k9718c06a'))
    await AfterSaleApi.receive(formData.value.id)
    // 提示成功
    message.success(t('common.success'))
    await getDetail()
  } catch {}
}

/** 拒绝收货 */
const refuse = async () => {
  try {
    // 二次确认
    await message.confirm(t('auto.views.mall.trade.afterSale.detail.index.ke36db97e'))
    await AfterSaleApi.refuse(formData.value.id)
    // 提示成功
    message.success(t('common.success'))
    await getDetail()
  } catch {}
}

/** 确认退款 */
const refund = async () => {
  try {
    // 二次确认
    await message.confirm(t('auto.views.mall.trade.afterSale.detail.index.k97798334'))
    await AfterSaleApi.refund(formData.value.id)
    // 提示成功
    message.success(t('common.success'))
    await getDetail()
  } catch {}
}

/** 图片预览 */
const imagePreview = (args) => {
  const urlList = []
  if (isArray(args)) {
    args.forEach((item) => {
      urlList.push(item.url)
    })
  } else {
    urlList.push(args)
  }
  createImageViewer({
    urlList
  })
}
const { delView } = useTagsViewStore() // 视图操作
/** 关闭 tag */
const close = () => {
  delView(unref(currentRoute))
  push({ name: 'TradeAfterSale' })
}
onMounted(async () => {
  await getDetail()
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
    background-color: var(--app-content-bg-color);

    &::before {
      position: absolute;
      top: 10px;
      left: 13px;
      border-color: transparent var(--app-content-bg-color) transparent transparent; /* 尖角颜色，左侧朝向 */
      border-style: solid;
      border-width: 8px; /* 调整尖角大小 */
      content: '';
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
