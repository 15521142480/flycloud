<!-- 销售漏斗分析 -->
<template>
  <!-- Echarts图 -->
  <el-card shadow="never">
    <el-row>
      <el-col :span="24">
        <el-button-group class="mb-10px">
          <el-button type="primary" @click="handleActive(true)">{{
            t('auto.views.crm.statistics.funnel.components.FunnelBusiness.kda459ee5')
          }}</el-button>
          <el-button type="primary" @click="handleActive(false)">{{
            t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k752bd7c6')
          }}</el-button>
        </el-button-group>
        <el-skeleton :loading="loading" animated>
          <Echart :height="500" :options="echartsOption" />
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
        :label="t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k4ca39faa')"
        prop="endStatus"
        width="200"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_BUSINESS_END_STATUS_TYPE" :value="scope.row.endStatus" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="
          t('auto.views.crm.statistics.funnel.components.BusinessInversionRateSummary.kf6b4649f')
        "
        min-width="200"
        prop="businessCount"
      />
      <el-table-column
        align="center"
        :label="t('extra.ke03f7ae4')"
        min-width="200"
        prop="totalPrice"
      />
    </el-table>
  </el-card>
</template>
<script lang="ts" setup>
import { CrmStatisticFunnelRespVO, StatisticFunnelApi } from '@/api/crm/statistics/funnel'
import { EChartsOption } from 'echarts'
import { DICT_TYPE } from '@/utils/dict'
const { t } = useI18n()
defineOptions({ name: 'FunnelBusiness' })
const props = defineProps<{ queryParams: any }>() // 搜索参数

const active = ref(true)
const loading = ref(false) // 加载中
const list = ref<CrmStatisticFunnelRespVO[]>([]) // 列表的数据

/** 销售漏斗 */
const echartsOption = reactive<EChartsOption>({
  title: {
    text: t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k15d3fb6e')
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}'
  },
  toolbox: {
    feature: {
      dataView: { readOnly: false },
      restore: {},
      saveAsImage: {}
    }
  },
  legend: {
    data: [
      t('auto.views.crm.statistics.funnel.components.FunnelBusiness.kf2068706'),
      t('auto.views.crm.statistics.funnel.components.FunnelBusiness.kc4beee85'),
      t('auto.views.crm.statistics.funnel.components.FunnelBusiness.ke65d23dd')
    ]
  },
  series: [
    {
      name: t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k15d3fb6e'),
      type: 'funnel',
      left: '10%',
      top: 60,
      bottom: 60,
      width: '80%',
      min: 0,
      max: 100,
      minSize: '0%',
      maxSize: '100%',
      sort: 'descending',
      gap: 2,
      label: {
        show: true,
        position: 'inside'
      },
      labelLine: {
        length: 10,
        lineStyle: {
          width: 1,
          type: 'solid'
        }
      },
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 1
      },
      emphasis: {
        label: {
          fontSize: 20
        }
      },
      data: [
        {
          value: 60,
          name: t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k4ebf343d')
        },
        {
          value: 40,
          name: t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k2c7531d7')
        },
        {
          value: 20,
          name: t('auto.views.crm.statistics.funnel.components.FunnelBusiness.k5bd12c22')
        }
      ]
    }
  ]
}) as EChartsOption

const handleActive = async (val: boolean) => {
  active.value = val
  await loadData()
}

/** 获取统计数据 */
const loadData = async () => {
  loading.value = true
  // 1. 加载漏斗数据
  const data = (await StatisticFunnelApi.getFunnelSummary(
    props.queryParams
  )) as CrmStatisticFunnelRespVO
  // 2.1 更新 Echarts 数据
  if (
    !!data &&
    echartsOption.series &&
    echartsOption.series[0] &&
    echartsOption.series[0]['data']
  ) {
    // tips：写死 value 值是为了保持漏斗顺序不变
    const list: { value: number; name: string }[] = []
    if (active.value) {
      list.push({ value: 60, name: t('extra.k170f1fff', { p0: data.customerCount || 0 }) })
      list.push({ value: 40, name: t('extra.k8519c32d', { p0: data.businessCount || 0 }) })
      list.push({ value: 20, name: t('extra.k1f31e95f', { p0: data.businessWinCount || 0 }) })
    } else {
      list.push({
        value: data.customerCount || 0,
        name: t('extra.k170f1fff', { p0: data.customerCount || 0 })
      })
      list.push({
        value: data.businessCount || 0,
        name: t('extra.k8519c32d', { p0: data.businessCount || 0 })
      })
      list.push({
        value: data.businessWinCount || 0,
        name: t('extra.k1f31e95f', { p0: data.businessWinCount || 0 })
      })
    }

    echartsOption.series[0]['data'] = list
  }
  // 2.2 获取商机结束状态统计
  list.value = await StatisticFunnelApi.getBusinessSummaryByEndStatus(props.queryParams)
  loading.value = false
}
defineExpose({ loadData })

/** 初始化 */
onMounted(() => {
  loadData()
})
</script>
