<!-- 客户来源分析 -->
<template>
  <!-- Echarts图 -->
  <el-card shadow="never">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-skeleton :loading="loading" animated>
          <Echart :height="500" :options="echartsOption" />
        </el-skeleton>
      </el-col>
      <el-col :span="12">
        <el-skeleton :loading="loading" animated>
          <Echart :height="500" :options="echartsOption2" />
        </el-skeleton>
      </el-col>
    </el-row>
  </el-card>

  <!-- 统计列表 -->
  <el-card class="mt-16px" shadow="never">
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" :label="t('common.index')" type="index" width="80" />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.kbb7208b8')"
        prop="level"
        width="200"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="scope.row.level" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('extra.k465fe4d6')"
        min-width="200"
        prop="customerCount"
      />
      <el-table-column
        align="center"
        :label="t('extra.kf96f9eab')"
        min-width="200"
        prop="dealCount"
      />
      <el-table-column
        align="center"
        :label="t('extra.k81b67ddf')"
        min-width="200"
        prop="levelPortion"
      />
      <el-table-column
        align="center"
        :label="t('extra.k7acde532')"
        min-width="200"
        prop="dealPortion"
      />
    </el-table>
  </el-card>
</template>
<script lang="ts" setup>
import {
  CrmStatisticCustomerLevelRespVO,
  StatisticsPortraitApi
} from '@/api/crm/statistics/portrait'
import { EChartsOption } from 'echarts'
import { DICT_TYPE, getDictLabel } from '@/utils/dict'
import { erpCalculatePercentage, getSumValue } from '@/utils'
import { isEmpty } from '@/utils/is'
const { t } = useI18n()
defineOptions({ name: 'PortraitCustomerLevel' })
const props = defineProps<{ queryParams: any }>() // 搜索参数

const loading = ref(false) // 加载中
const list = ref<CrmStatisticCustomerLevelRespVO[]>([]) // 列表的数据

/** 饼图配置（全部客户） */
const echartsOption = reactive<EChartsOption>({
  title: {
    text: t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.kc83c7ab1'),
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  toolbox: {
    feature: {
      saveAsImage: {
        show: true,
        name: t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.kc83c7ab1')
      } // 保存为图片
    }
  },
  series: [
    {
      name: t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.kc83c7ab1'),
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: []
    }
  ]
}) as EChartsOption

/** 饼图配置（成交客户） */
const echartsOption2 = reactive<EChartsOption>({
  title: {
    text: t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.k486a5a89'),
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  toolbox: {
    feature: {
      saveAsImage: {
        show: true,
        name: t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.k486a5a89')
      } // 保存为图片
    }
  },
  series: [
    {
      name: t('auto.views.crm.statistics.portrait.components.PortraitCustomerLevel.k486a5a89'),
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: []
    }
  ]
}) as EChartsOption

/** 获取统计数据 */
const loadData = async () => {
  // 1. 加载统计数据
  loading.value = true
  const levelList = await StatisticsPortraitApi.getCustomerLevel(props.queryParams)
  // 2.1 更新 Echarts 数据
  if (echartsOption.series && echartsOption.series[0] && echartsOption.series[0]['data']) {
    echartsOption.series[0]['data'] = levelList.map((r: CrmStatisticCustomerLevelRespVO) => {
      return {
        name: getDictLabel(DICT_TYPE.CRM_CUSTOMER_LEVEL, r.level),
        value: r.customerCount
      }
    })
  }
  // 2.2 更新 Echarts2 数据
  if (echartsOption2.series && echartsOption2.series[0] && echartsOption2.series[0]['data']) {
    echartsOption2.series[0]['data'] = levelList.map((r: CrmStatisticCustomerLevelRespVO) => {
      return {
        name: getDictLabel(DICT_TYPE.CRM_CUSTOMER_LEVEL, r.level),
        value: r.dealCount
      }
    })
  }
  // 3. 计算比例
  calculateProportion(levelList)
  list.value = levelList
  loading.value = false
}
defineExpose({ loadData })

/** 计算比例 */
const calculateProportion = (levelList: CrmStatisticCustomerLevelRespVO[]) => {
  if (isEmpty(levelList)) {
    return
  }
  // 这里类型丢失了所以重新搞个变量
  const list = levelList as unknown as CrmStatisticCustomerLevelRespVO[]
  const sumCustomerCount = getSumValue(list.map((item) => item.customerCount))
  const sumDealCount = getSumValue(list.map((item) => item.dealCount))
  list.forEach((item) => {
    item.levelPortion =
      item.customerCount === 0 ? 0 : erpCalculatePercentage(item.customerCount, sumCustomerCount)
    item.dealPortion =
      item.dealCount === 0 ? 0 : erpCalculatePercentage(item.dealCount, sumDealCount)
  })
}

/** 初始化 */
onMounted(() => {
  loadData()
})
</script>
