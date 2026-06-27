<!-- 合同金额排行 -->
<template>
  <!-- 柱状图 -->
  <el-card shadow="never">
    <el-skeleton :loading="loading" animated>
      <Echart :height="500" :options="echartsOption" />
    </el-skeleton>
  </el-card>

  <!-- 排行列表 -->
  <el-card shadow="never" class="mt-16px">
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.crm.statistics.rank.components.ContractPriceRank.k92040fc1')"
        align="center"
        type="index"
        width="80"
      />
      <el-table-column
        :label="t('auto.views.crm.statistics.rank.components.ContractPriceRank.ka2f30c0a')"
        align="center"
        prop="name"
        min-width="200"
      />
      <el-table-column
        :label="t('auto.views.crm.statistics.rank.components.ContractPriceRank.k91061a56')"
        align="center"
        prop="deptName"
        min-width="200"
      />
      <el-table-column
        :label="t('auto.views.crm.statistics.rank.components.ContractPriceRank.k4687c195')"
        align="center"
        prop="count"
        min-width="200"
        :formatter="erpPriceTableColumnFormatter"
      />
    </el-table>
  </el-card>
</template>
<script setup lang="ts">
import { StatisticsRankApi, StatisticsRankRespVO } from '@/api/crm/statistics/rank'
import { EChartsOption } from 'echarts'
import { clone } from 'lodash-es'
import { erpPriceTableColumnFormatter } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'ContractPriceRank' })
const props = defineProps<{ queryParams: any }>() // 搜索参数

const loading = ref(false) // 加载中
const list = ref<StatisticsRankRespVO[]>([]) // 列表的数据

/** 柱状图配置：横向 */
const echartsOption = reactive<EChartsOption>({
  dataset: {
    dimensions: ['name', 'count'],
    source: []
  },
  grid: {
    left: 20,
    right: 20,
    bottom: 20,
    containLabel: true
  },
  legend: {
    top: 50
  },
  series: [
    {
      name: t('auto.views.crm.statistics.rank.components.ContractPriceRank.k5cf4954f'),
      type: 'bar'
    }
  ],
  toolbox: {
    feature: {
      dataZoom: {
        yAxisIndex: false // 数据区域缩放：Y 轴不缩放
      },
      brush: {
        type: ['lineX', 'clear'] // 区域缩放按钮、还原按钮
      },
      saveAsImage: {
        show: true,
        name: t('auto.views.crm.statistics.rank.components.ContractPriceRank.k5cf4954f')
      } // 保存为图片
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  xAxis: {
    type: 'value',
    name: t('auto.views.crm.statistics.rank.components.ContractPriceRank.k4687c195')
  },
  yAxis: {
    type: 'category',
    name: t('auto.views.crm.statistics.rank.components.ContractPriceRank.ka2f30c0a')
  }
}) as EChartsOption

/** 获取合同金额排行 */
const loadData = async () => {
  // 1. 加载排行数据
  loading.value = true
  const rankingList = await StatisticsRankApi.getContractPriceRank(props.queryParams)
  // 2.1 更新 Echarts 数据
  if (echartsOption.dataset && echartsOption.dataset['source']) {
    echartsOption.dataset['source'] = clone(rankingList).reverse()
  }
  // 2.2 更新列表数据
  list.value = rankingList
  loading.value = false
}
defineExpose({ loadData })

/** 初始化 */
onMounted(() => {
  loadData()
})
</script>
