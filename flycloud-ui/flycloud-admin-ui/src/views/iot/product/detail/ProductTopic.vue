<template>
  <ContentWrap>
    <el-tabs>
      <el-tab-pane :label="t('auto.views.iot.product.detail.ProductTopic.k12040114')">
        <Table
          :columns="columns1"
          :data="data1"
          :span-method="createSpanMethod(data1)"
          align="left"
          headerAlign="left"
          border="true"
        />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.iot.product.detail.ProductTopic.k81b3a222')">
        <Table
          :columns="columns2"
          :data="data2"
          :span-method="createSpanMethod(data2)"
          align="left"
          headerAlign="left"
          border="true"
        />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>
</template>
<script setup lang="ts">
import { ProductVO } from '@/api/iot/product'
const { t } = useI18n()
const props = defineProps<{ product: ProductVO }>()

// 定义列
const columns1 = reactive([
  {
    label: t('auto.views.iot.product.detail.ProductTopic.k4e1ff3eb'),
    field: 'function',
    width: 150
  },
  {
    label: t('auto.views.iot.product.detail.ProductTopic.k81bf1cb1'),
    field: 'topicClass',
    width: 800
  },
  {
    label: t('auto.views.iot.product.detail.ProductTopic.k11899ced'),
    field: 'operationPermission',
    width: 100
  },
  { label: t('auto.views.iot.product.detail.ProductTopic.k412f54dc'), field: 'description' }
])

const columns2 = reactive([
  {
    label: t('auto.views.iot.product.detail.ProductTopic.k4e1ff3eb'),
    field: 'function',
    width: 150
  },
  {
    label: t('auto.views.iot.product.detail.ProductTopic.k81bf1cb1'),
    field: 'topicClass',
    width: 800
  },
  {
    label: t('auto.views.iot.product.detail.ProductTopic.k11899ced'),
    field: 'operationPermission',
    width: 100
  },
  { label: t('auto.views.iot.product.detail.ProductTopic.k412f54dc'), field: 'description' }
])

// TODO @haohao：这个，有没可能写到一个枚举里，方便后续维护？ /Users/yunai/Java/yudao-ui-admin-vue3/src/views/ai/utils/constants.ts
const data1 = computed(() => {
  if (!props.product || !props.product.productKey) return []
  return [
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kbdb9a2fa'),
      topicClass: `/ota/device/inform/${props.product.productKey}/\${deviceName}`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.kb4f7a33a')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kbdb9a2fa'),
      topicClass: `/ota/device/upgrade/${props.product.productKey}/\${deviceName}`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k199f974b')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kbdb9a2fa'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/ota/firmware/get`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.kffe9395a')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kbdb9a2fa'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/ota/firmware/get`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k5a4a4847')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kf2a614a5'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/deviceinfo/update`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.kba9c4b08')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kf2a614a5'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/deviceinfo/update_reply`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k41fd0907')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kf2a614a5'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/deviceinfo/delete`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k4082767a')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kf2a614a5'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/deviceinfo/delete_reply`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.ke3ea3a2c')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k8eda19c2'),
      topicClass: `/ext/ntp/${props.product.productKey}/\${deviceName}/request`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k92d1d54f')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k8eda19c2'),
      topicClass: `/ext/ntp/${props.product.productKey}/\${deviceName}/response`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k0321aa1f')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kad225e47'),
      topicClass: `/shadow/update/${props.product.productKey}/\${deviceName}`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k6b517c32')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kad225e47'),
      topicClass: `/shadow/get/${props.product.productKey}/\${deviceName}`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.ke45a45b1')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k0a8d80c2'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/config/push`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.kf90d6c1e')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k0a8d80c2'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/config/get`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k9e2babee')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k0a8d80c2'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/config/get_reply`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k2184bb8f')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k8b24aab4'),
      topicClass: `/broadcast/${props.product.productKey}/\${identifier}`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k463e697f')
    }
  ]
})

const data2 = computed(() => {
  if (!props.product || !props.product.productKey) return []
  return [
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k4f8940cf'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/event/property/post`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k15d1cb9f')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k4f8940cf'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/event/property/post_reply`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k60659b25')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k7289c9f0'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/service/property/set`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.k8d241a26')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k73f76a73'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/event/\${tsl.event.identifier}/post`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k1fed85fa')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.k73f76a73'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/event/\${tsl.event.identifier}/post_reply`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.kcb205e2d')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kb798b2c1'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/service/\${tsl.service.identifier}`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k5319af76'),
      description: t('auto.views.iot.product.detail.ProductTopic.kc9f36aa4')
    },
    {
      function: t('auto.views.iot.product.detail.ProductTopic.kb798b2c1'),
      topicClass: `/sys/${props.product.productKey}/\${deviceName}/thing/service/\${tsl.service.identifier}_reply`,
      operationPermission: t('auto.views.iot.product.detail.ProductTopic.k94f172d0'),
      description: t('auto.views.iot.product.detail.ProductTopic.k87d133be')
    }
  ]
})

// 通用的单元格合并方法生成器
const createSpanMethod = (data: any[]) => {
  // 预处理，计算每个功能的合并行数
  const rowspanMap: Record<number, number> = {}
  let currentFunction = ''
  let startIndex = 0
  let count = 0

  data.forEach((item, index) => {
    if (item.function !== currentFunction) {
      if (count > 0) {
        rowspanMap[startIndex] = count
      }
      currentFunction = item.function
      startIndex = index
      count = 1
    } else {
      count++
    }
  })

  // 处理最后一组
  if (count > 0) {
    rowspanMap[startIndex] = count
  }

  // 返回 span 方法
  return ({ row, column, rowIndex, columnIndex }: SpanMethodProps) => {
    if (columnIndex === 0) {
      // 仅对“功能”列进行合并
      const rowspan = rowspanMap[rowIndex] || 0
      if (rowspan > 0) {
        return {
          rowspan,
          colspan: 1
        }
      } else {
        return {
          rowspan: 0,
          colspan: 0
        }
      }
    }
  }
}
</script>
