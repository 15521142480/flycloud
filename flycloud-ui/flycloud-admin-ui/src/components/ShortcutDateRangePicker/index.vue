<template>
  <div class="flex flex-row items-center gap-2">
    <el-radio-group v-model="shortcutDays" @change="handleShortcutDaysChange">
      <el-radio-button :value="1">{{
        t('auto.components.ShortcutDateRangePicker.index.k59c4fcb0')
      }}</el-radio-button>
      <el-radio-button :value="7">{{
        t('auto.components.ShortcutDateRangePicker.index.kfc11847b')
      }}</el-radio-button>
      <el-radio-button :value="30">{{
        t('auto.components.ShortcutDateRangePicker.index.kcb892bd4')
      }}</el-radio-button>
    </el-radio-group>
    <el-date-picker
      v-model="times"
      value-format="YYYY-MM-DD HH:mm:ss"
      type="daterange"
      :start-placeholder="t('auto.components.ShortcutDateRangePicker.index.k1f291968')"
      :end-placeholder="t('auto.components.ShortcutDateRangePicker.index.kf4b9b2b5')"
      :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
      :shortcuts="shortcuts"
      class="!w-240px"
      @change="emitDateRangePicker"
    />
    <slot></slot>
  </div>
</template>
<script lang="ts" setup>
import dayjs from 'dayjs'
import * as DateUtil from '@/utils/formatTime'

/** 快捷日期范围选择组件 */
const { t } = useI18n()
defineOptions({ name: 'ShortcutDateRangePicker' })

const shortcutDays = ref(7) // 日期快捷天数（单选按钮组）, 默认7天
const times = ref<[string, string]>(['', '']) // 时间范围参数
defineExpose({ times }) // 暴露时间范围参数
/** 日期快捷选择 */
const shortcuts = [
  {
    text: t('auto.components.ShortcutDateRangePicker.index.k59c4fcb0'),
    value: () => DateUtil.getDayRange(new Date(), -1)
  },
  {
    text: t('auto.components.ShortcutDateRangePicker.index.kfc11847b'),
    value: () => DateUtil.getLast7Days()
  },
  {
    text: t('auto.components.ShortcutDateRangePicker.index.k389f0007'),
    value: () => [dayjs().startOf('M'), dayjs().subtract(1, 'd')]
  },
  {
    text: t('auto.components.ShortcutDateRangePicker.index.kcb892bd4'),
    value: () => DateUtil.getLast30Days()
  },
  {
    text: t('auto.components.ShortcutDateRangePicker.index.k1d46b23b'),
    value: () => DateUtil.getLast1Year()
  }
]

/** 设置时间范围 */
function setTimes() {
  const beginDate = dayjs().subtract(shortcutDays.value, 'd')
  const yesterday = dayjs().subtract(1, 'd')
  times.value = DateUtil.getDateRange(beginDate, yesterday)
}

/** 快捷日期单选按钮选中 */
const handleShortcutDaysChange = async () => {
  // 设置时间范围
  setTimes()
  // 发送时间范围选中事件
  await emitDateRangePicker()
}

/** 触发事件：时间范围选中 */
const emits = defineEmits<{
  (e: 'change', times: [dayjs.ConfigType, dayjs.ConfigType]): void
}>()
/** 触发时间范围选中事件 */
const emitDateRangePicker = async () => {
  emits('change', times.value)
}

/** 初始化 **/
onMounted(() => {
  handleShortcutDaysChange()
})
</script>
