<template>
  <!-- 支付信息 -->
  <el-card v-loading="loading">
    <el-descriptions :title="t('auto.views.pay.cashier.index.k84a5a94e')" :column="3" border>
      <el-descriptions-item :label="t('auto.views.pay.cashier.index.k956bb6be')">{{
        payOrder.id
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.cashier.index.kd415beac')">{{
        payOrder.subject
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.cashier.index.k84edad36')">{{
        payOrder.body
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.cashier.index.kb579703e')">
        ￥{{ (payOrder.price / 100.0).toFixed(2) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(payOrder.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.cashier.index.k27aefecf')">
        {{ formatDate(payOrder.expireTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>

  <!-- 支付选择框 -->
  <el-card
    style="margin-top: 10px"
    v-loading="submitLoading"
    :element-loading-text="t('extra.k392bd663')"
  >
    <!-- 支付宝 -->
    <el-descriptions :title="t('auto.views.pay.cashier.index.k2dbafd81')" />
    <div class="pay-channel-container">
      <div
        class="box"
        v-for="channel in channelsAlipay"
        :key="channel.code"
        @click="submit(channel.code)"
      >
        <img :src="channel.icon" />
        <div class="title">{{ channel.name }}</div>
      </div>
    </div>
    <!-- 微信支付 -->
    <el-descriptions
      :title="t('auto.views.pay.cashier.index.kc11e1c71')"
      style="margin-top: 20px"
    />
    <div class="pay-channel-container">
      <div
        class="box"
        v-for="channel in channelsWechat"
        :key="channel.code"
        @click="submit(channel.code)"
      >
        <img :src="channel.icon" />
        <div class="title">{{ channel.name }}</div>
      </div>
    </div>
    <!-- 其它支付 -->
    <el-descriptions
      :title="t('auto.views.pay.cashier.index.kc0d1cdb7')"
      style="margin-top: 20px"
    />
    <div class="pay-channel-container">
      <div
        class="box"
        v-for="channel in channelsMock"
        :key="channel.code"
        @click="submit(channel.code)"
      >
        <img :src="channel.icon" />
        <div class="title">{{ channel.name }}</div>
      </div>
    </div>
  </el-card>

  <!-- 展示形式：二维码 URL -->
  <Dialog
    :title="qrCode.title"
    v-model="qrCode.visible"
    width="350px"
    append-to-body
    :close-on-press-escape="false"
  >
    <Qrcode :text="qrCode.url" :width="310" />
  </Dialog>

  <!-- 展示形式：BarCode 条形码 -->
  <Dialog
    :title="barCode.title"
    v-model="barCode.visible"
    width="500px"
    append-to-body
    :close-on-press-escape="false"
  >
    <el-form ref="form" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('auto.views.pay.cashier.index.ka35d7201')" prop="name">
            <el-input
              v-model="barCode.value"
              :placeholder="t('auto.views.pay.cashier.index.k6674df23')"
              required
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right">
            {{ t('extra.kad404410') }}
            <el-link
              type="danger"
              target="_blank"
              href="https://baike.baidu.com/item/条码支付/10711903"
            >
              {{ t('extra.kda0b369e') }}
            </el-link>
            {{ t('extra.k209c3d7b') }}
          </div>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button
        type="primary"
        @click="submit0(barCode.channelCode)"
        :disabled="barCode.value.length === 0"
      >
        {{ t('extra.kd695ba85') }}
      </el-button>
      <el-button @click="barCode.visible = false">{{
        t('auto.views.pay.cashier.index.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { Qrcode } from '@/components/Qrcode'
import * as PayOrderApi from '@/api/pay/order'
import { PayChannelEnum, PayDisplayModeEnum, PayOrderStatusEnum } from '@/utils/constants'
import { formatDate } from '@/utils/formatTime'
import { useTagsViewStore } from '@/store/modules/tagsView'

// 导入图标
import svg_alipay_pc from '@/assets/svgs/pay/icon/alipay_pc.svg'
import svg_alipay_wap from '@/assets/svgs/pay/icon/alipay_wap.svg'
import svg_alipay_app from '@/assets/svgs/pay/icon/alipay_app.svg'
import svg_alipay_qr from '@/assets/svgs/pay/icon/alipay_qr.svg'
import svg_alipay_bar from '@/assets/svgs/pay/icon/alipay_bar.svg'
import svg_wx_pub from '@/assets/svgs/pay/icon/wx_pub.svg'
import svg_wx_lite from '@/assets/svgs/pay/icon/wx_lite.svg'
import svg_wx_app from '@/assets/svgs/pay/icon/wx_app.svg'
import svg_wx_native from '@/assets/svgs/pay/icon/wx_native.svg'
import svg_wx_bar from '@/assets/svgs/pay/icon/wx_bar.svg'
import svg_mock from '@/assets/svgs/pay/icon/mock.svg'
const { t } = useI18n()
defineOptions({ name: 'PayCashier' })

const message = useMessage() // 消息弹窗
const route = useRoute() // 路由
const { push, currentRoute } = useRouter() // 路由
const { delView } = useTagsViewStore() // 视图操作

const id = ref(undefined) // 支付单号
const returnUrl = ref<string | undefined>(undefined) // 支付完的回调地址
const loading = ref(false) // 支付信息的 loading
const payOrder = ref({}) // 支付信息
const channelsAlipay = [
  {
    name: t('auto.views.pay.cashier.index.kdff9b7f7'),
    icon: svg_alipay_pc,
    code: 'alipay_pc'
  },
  {
    name: t('auto.views.pay.cashier.index.k7fcabd95'),
    icon: svg_alipay_wap,
    code: 'alipay_wap'
  },
  {
    name: t('auto.views.pay.cashier.index.kc5dc3cd3'),
    icon: svg_alipay_app,
    code: 'alipay_app'
  },
  {
    name: t('auto.views.pay.cashier.index.ka3178821'),
    icon: svg_alipay_qr,
    code: 'alipay_qr'
  },
  {
    name: t('auto.views.pay.cashier.index.ke6558bef'),
    icon: svg_alipay_bar,
    code: 'alipay_bar'
  }
]
const channelsWechat = [
  {
    name: t('auto.views.pay.cashier.index.k1a085e6b'),
    icon: svg_wx_pub,
    code: 'wx_pub'
  },
  {
    name: t('auto.views.pay.cashier.index.k787e4e5c'),
    icon: svg_wx_lite,
    code: 'wx_lite'
  },
  {
    name: t('auto.views.pay.cashier.index.kce24cee7'),
    icon: svg_wx_app,
    code: 'wx_app'
  },
  {
    name: t('auto.views.pay.cashier.index.kc8715f38'),
    icon: svg_wx_native,
    code: 'wx_native'
  },
  {
    name: t('auto.views.pay.cashier.index.k2eb2da22'),
    icon: svg_wx_bar,
    code: 'wx_bar'
  }
]
const channelsMock = [
  {
    name: t('auto.views.pay.cashier.index.kbf4800c0'),
    icon: svg_mock,
    code: 'mock'
  }
]

const submitLoading = ref(false) // 提交支付的 loading
const interval = ref<any>(undefined) // 定时任务，轮询是否完成支付
const qrCode = ref({
  // 展示形式：二维码
  url: '',
  title: '',
  visible: false
})
const barCode = ref({
  // 展示形式：条形码
  channelCode: '',
  value: '',
  title: '',
  visible: false
})

/** 获得支付信息 */
const getDetail = async () => {
  // 1.1 未传递订单编号
  if (!id.value) {
    message.error(t('auto.views.pay.cashier.index.kda35f731'))
    goReturnUrl('cancel')
    return
  }
  const data = await PayOrderApi.getOrder(id.value, true)
  payOrder.value = data
  // 1.2 无法查询到支付信息
  if (!data) {
    message.error(t('auto.views.pay.cashier.index.k98dd3b0b'))
    goReturnUrl('cancel')
    return
  }
  // 1.3 如果已支付、或者已关闭，则直接跳转
  if (data.status === PayOrderStatusEnum.SUCCESS.status) {
    message.success(t('auto.views.pay.cashier.index.k31a7e8ae'))
    goReturnUrl('success')
    return
  } else if (data.status === PayOrderStatusEnum.CLOSED.status) {
    message.error(t('auto.views.pay.cashier.index.k607e21df'))
    goReturnUrl('close')
    return
  }
}

/** 提交支付 */
const submit = (channelCode) => {
  // 条形码支付，需要特殊处理
  if (channelCode === PayChannelEnum.ALIPAY_BAR.code) {
    barCode.value = {
      channelCode: channelCode,
      value: '',
      title: t('auto.views.pay.cashier.index.k8997fe6b'),
      visible: true
    }
    return
  }
  if (channelCode === PayChannelEnum.WX_BAR.code) {
    barCode.value = {
      channelCode: channelCode,
      value: '',
      title: t('auto.views.pay.cashier.index.kd6b8aac3'),
      visible: true
    }
    return
  }

  // 微信公众号、小程序支付，无法在 PC 网页中进行
  if (channelCode === PayChannelEnum.WX_PUB.code) {
    message.error(t('auto.views.pay.cashier.index.kcd10fb95'))
    return
  }
  if (channelCode === PayChannelEnum.WX_LITE.code) {
    message.error(t('auto.views.pay.cashier.index.k0837801c'))
    return
  }

  // 默认的提交处理
  submit0(channelCode)
}

const submit0 = async (channelCode) => {
  submitLoading.value = true
  try {
    const formData = {
      id: id.value,
      channelCode: channelCode,
      returnUrl: location.href, // 支付成功后，支付渠道跳转回当前页；再由当前页，跳转回 {@link returnUrl} 对应的地址
      ...buildSubmitParam(channelCode)
    }
    const data = await PayOrderApi.submitOrder(formData)
    // 直接返回已支付的情况，例如说扫码支付
    if (data.status === PayOrderStatusEnum.SUCCESS.status) {
      clearQueryInterval()
      message.success(t('auto.views.pay.cashier.index.kc0f333a9'))
      goReturnUrl('success')
      return
    }

    // 展示对应的界面
    if (data.displayMode === PayDisplayModeEnum.URL.mode) {
      displayUrl(channelCode, data)
    } else if (data.displayMode === PayDisplayModeEnum.QR_CODE.mode) {
      displayQrCode(channelCode, data)
    } else if (data.displayMode === PayDisplayModeEnum.APP.mode) {
      displayApp(channelCode)
    }

    // 打开轮询任务
    createQueryInterval()
  } finally {
    submitLoading.value = false
  }
}

/** 构建提交支付的额外参数 */
const buildSubmitParam = (channelCode) => {
  // ① 支付宝 BarCode 支付时，需要传递 authCode 条形码
  if (channelCode === PayChannelEnum.ALIPAY_BAR.code) {
    return {
      channelExtras: {
        auth_code: barCode.value.value
      }
    }
  }
  // ② 微信 BarCode 支付时，需要传递 authCode 条形码
  if (channelCode === PayChannelEnum.WX_BAR.code) {
    return {
      channelExtras: {
        authCode: barCode.value.value
      }
    }
  }
  return {}
}

/** 提交支付后，URL 的展示形式 */
const displayUrl = (_channelCode, data) => {
  location.href = data.displayContent
  submitLoading.value = false
}

/** 提交支付后（扫码支付） */
const displayQrCode = (channelCode, data) => {
  let title = t('auto.views.pay.cashier.index.k7514ad22')
  if (channelCode === PayChannelEnum.ALIPAY_WAP.code) {
    // 考虑到 WAP 测试，所以引导手机浏览器搞
  } else if (channelCode.indexOf('alipay_') === 0) {
    title = t('auto.views.pay.cashier.index.k205bfc82')
  } else if (channelCode.indexOf('wx_') === 0) {
    title = t('auto.views.pay.cashier.index.ke66b2bab')
  }
  qrCode.value = {
    title: title,
    url: data.displayContent,
    visible: true
  }
  submitLoading.value = false
}

/** 提交支付后（App） */
const displayApp = (channelCode) => {
  if (channelCode === PayChannelEnum.ALIPAY_APP.code) {
    message.error(t('auto.views.pay.cashier.index.k0de4d29c'))
  }
  if (channelCode === PayChannelEnum.WX_APP.code) {
    message.error(t('auto.views.pay.cashier.index.ka3f5306e'))
  }
  submitLoading.value = false
}

/** 轮询查询任务 */
const createQueryInterval = () => {
  if (interval.value) {
    return
  }
  interval.value = setInterval(async () => {
    const data = await PayOrderApi.getOrder(id.value)
    // 已支付
    if (data.status === PayOrderStatusEnum.SUCCESS.status) {
      clearQueryInterval()
      message.success(t('auto.views.pay.cashier.index.kc0f333a9'))
      goReturnUrl('success')
    }
    // 已取消
    if (data.status === PayOrderStatusEnum.CLOSED.status) {
      clearQueryInterval()
      message.error(t('auto.views.pay.cashier.index.k7480667e'))
      goReturnUrl('close')
    }
  }, 1000 * 2)
}

/** 清空查询任务 */
const clearQueryInterval = () => {
  // 清空各种弹窗
  qrCode.value = {
    title: '',
    url: '',
    visible: false
  }
  // 清空任务
  clearInterval(interval.value)
  interval.value = undefined
}

/**
 * 回到业务的 URL
 *
 * @param payResult 支付结果
 *                  ① success：支付成功
 *                  ② cancel：取消支付
 *                  ③ close：支付已关闭
 */
const goReturnUrl = (payResult) => {
  // 清理任务
  clearQueryInterval()

  // 未配置的情况下，只能关闭
  if (!returnUrl.value) {
    delView(unref(currentRoute))
    return
  }

  const url =
    returnUrl.value.indexOf('?') >= 0
      ? returnUrl.value + '&payResult=' + payResult
      : returnUrl.value + '?payResult=' + payResult
  // 如果有配置，且是 http 开头，则浏览器跳转
  if (returnUrl.value.indexOf('http') === 0) {
    location.href = url
  } else {
    delView(unref(currentRoute))
    push({
      path: url
    })
  }
}

/** 初始化 */
onMounted(() => {
  id.value = route.query.id
  if (route.query.returnUrl) {
    returnUrl.value = decodeURIComponent(route.query.returnUrl)
  }
  getDetail()
})
</script>

<style lang="scss" scoped>
.pay-channel-container {
  display: flex;
  margin-top: -10px;

  .box {
    width: 160px;
    padding-top: 10px;
    padding-bottom: 5px;
    margin-right: 10px;
    text-align: center;
    cursor: pointer;
    border: 1px solid #e6ebf5;

    img {
      width: 40px;
      height: 40px;
    }

    .title {
      padding-top: 5px;
    }
  }
}
</style>
