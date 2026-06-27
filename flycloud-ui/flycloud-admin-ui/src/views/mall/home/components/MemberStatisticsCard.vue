<template>
  <el-card shadow="never">
    <template #header>
      <CardTitle :title="t('auto.views.mall.home.components.MemberStatisticsCard.k30e1b70f')" />
    </template>
    <!-- 折线图 -->
    <Echart :height="300" :options="lineChartOptions" />
  </el-card>
</template>
<script lang="ts" setup>
import dayjs from 'dayjs'
import { EChartsOption } from 'echarts'
import * as MemberStatisticsApi from '@/api/mall/statistics/member'
import { formatDate } from '@/utils/formatTime'
import { CardTitle } from '@/components/Card'

/** 会员用户统计卡片 */
const { t } = useI18n()
defineOptions({ name: 'MemberStatisticsCard' })

const loading = ref(true) // 加载中
/** 折线图配置 */
const lineChartOptions = reactive<EChartsOption>({
  dataset: {
    dimensions: ['date', 'count'],
    source: []
  },
  grid: {
    left: 20,
    right: 20,
    bottom: 20,
    top: 80,
    containLabel: true
  },
  legend: {
    top: 50
  },
  series: [
    {
      name: t('auto.views.mall.home.components.MemberStatisticsCard.k1e5df8e7'),
      type: 'line',
      smooth: true,
      areaStyle: {}
    }
  ],
  toolbox: {
    feature: {
      // 数据区域缩放
      dataZoom: {
        yAxisIndex: false // Y轴不缩放
      },
      brush: {
        type: ['lineX', 'clear'] // 区域缩放按钮、还原按钮
      },
      saveAsImage: {
        show: true,
        name: t('auto.views.mall.home.components.MemberStatisticsCard.k3634d02b')
      } // 保存为图片
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    },
    padding: [5, 10]
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    axisTick: {
      show: false
    },
    axisLabel: {
      formatter: (date: string) => formatDate(date, 'MM-DD')
    }
  },
  yAxis: {
    axisTick: {
      show: false
    }
  }
}) as EChartsOption

const getMemberRegisterCountList = async () => {
  loading.value = true
  // 查询最近一月数据
  const beginTime = dayjs().subtract(30, 'd').startOf('d')
  const endTime = dayjs().endOf('d')
  const list = await MemberStatisticsApi.getMemberRegisterCountList(beginTime, endTime)
  // 更新 Echarts 数据
  if (lineChartOptions.dataset && lineChartOptions.dataset['source']) {
    lineChartOptions.dataset['source'] = list
  }
  loading.value = false
}

/** 初始化 **/
onMounted(() => {
  getMemberRegisterCountList()
})
</script>
