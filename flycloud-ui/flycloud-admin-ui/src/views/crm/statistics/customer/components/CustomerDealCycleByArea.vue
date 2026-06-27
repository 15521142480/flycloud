<!-- 成交周期分析 -->
<template>
  <!-- Echarts图 -->
  <el-card shadow="never">
    <el-skeleton :loading="loading" animated>
      <Echart :height="500" :options="echartsOption" />
    </el-skeleton>
  </el-card>

  <!-- 统计列表 -->
  <el-card shadow="never" class="mt-16px">
    <el-table v-loading="loading" :data="list">
      <el-table-column :label="t('common.index')" align="center" type="index" width="80" />
      <el-table-column
        :label="
          t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k17fc93c9')
        "
        align="center"
        prop="areaName"
        min-width="200"
      />
      <el-table-column
        :label="
          t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k2a46ac43')
        "
        align="center"
        prop="customerDealCycle"
        min-width="200"
      />
      <el-table-column
        :label="
          t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k8e16e0d0')
        "
        align="center"
        prop="customerDealCount"
        min-width="200"
      />
    </el-table>
  </el-card>
</template>
<script setup lang="ts">
import {
  StatisticsCustomerApi,
  CrmStatisticsCustomerDealCycleByAreaRespVO
} from '@/api/crm/statistics/customer'
import { EChartsOption } from 'echarts'
const { t } = useI18n()
defineOptions({ name: 'CustomerDealCycleByArea' })

const props = defineProps<{ queryParams: any }>() // 搜索参数

const loading = ref(false) // 加载中
const list = ref<CrmStatisticsCustomerDealCycleByAreaRespVO[]>([]) // 列表的数据

/** 柱状图配置：纵向 */
const echartsOption = reactive<EChartsOption>({
  grid: {
    left: 20,
    right: 40, // 让 X 轴右侧显示完整
    bottom: 20,
    containLabel: true
  },
  legend: {},
  series: [
    {
      name: t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k2a46ac43'),
      type: 'bar',
      data: [],
      yAxisIndex: 0
    },
    {
      name: t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k8e16e0d0'),
      type: 'bar',
      data: [],
      yAxisIndex: 1
    }
  ],
  toolbox: {
    feature: {
      dataZoom: {
        xAxisIndex: false // 数据区域缩放：Y 轴不缩放
      },
      brush: {
        type: ['lineX', 'clear'] // 区域缩放按钮、还原按钮
      },
      saveAsImage: {
        show: true,
        name: t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.kd20ef81b')
      } // 保存为图片
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  yAxis: [
    {
      type: 'value',
      name: t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k2a46ac43'),
      min: 0,
      minInterval: 1 // 显示整数刻度
    },
    {
      type: 'value',
      name: t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k8e16e0d0'),
      min: 0,
      minInterval: 1, // 显示整数刻度
      splitLine: {
        lineStyle: {
          type: 'dotted', // 右侧网格线虚化, 减少混乱
          opacity: 0.7
        }
      }
    }
  ],
  xAxis: {
    type: 'category',
    name: t('auto.views.crm.statistics.customer.components.CustomerDealCycleByArea.k17fc93c9'),
    data: []
  }
}) as EChartsOption

/** 获取数据并填充图表 */
const fetchAndFill = async () => {
  // 1. 加载统计数据
  const customerDealCycleByArea = (
    await StatisticsCustomerApi.getCustomerDealCycleByArea(props.queryParams)
  ).map((s: CrmStatisticsCustomerDealCycleByAreaRespVO) => {
    return {
      areaName: s.areaName,
      customerDealCycle: s.customerDealCycle,
      customerDealCount: s.customerDealCount
    }
  })
  // 2.1 更新 Echarts 数据
  if (echartsOption.xAxis && echartsOption.xAxis['data']) {
    echartsOption.xAxis['data'] = customerDealCycleByArea.map(
      (s: CrmStatisticsCustomerDealCycleByAreaRespVO) => s.areaName
    )
  }
  if (echartsOption.series && echartsOption.series[0] && echartsOption.series[0]['data']) {
    echartsOption.series[0]['data'] = customerDealCycleByArea.map(
      (s: CrmStatisticsCustomerDealCycleByAreaRespVO) => s.customerDealCycle
    )
  }
  if (echartsOption.series && echartsOption.series[1] && echartsOption.series[1]['data']) {
    echartsOption.series[1]['data'] = customerDealCycleByArea.map(
      (s: CrmStatisticsCustomerDealCycleByAreaRespVO) => s.customerDealCount
    )
  }
  // 2.2 更新列表数据
  list.value = customerDealCycleByArea
}

/** 获取统计数据 */
const loadData = async () => {
  loading.value = true
  try {
    await fetchAndFill()
  } finally {
    loading.value = false
  }
}
defineExpose({ loadData })

/** 初始化 */
onMounted(() => {
  loadData()
})
</script>
