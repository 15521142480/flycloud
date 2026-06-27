import { dateUtil } from '@/utils/dateUtil'
import { reactive, toRefs } from 'vue'
import { tryOnMounted, tryOnUnmounted } from '@vueuse/core'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

export const useNow = (immediate = true) => {
  let timer: IntervalHandle

  const state = reactive({
    year: 0,
    month: 0,
    week: '',
    day: 0,
    hour: '',
    minute: '',
    second: 0,
    meridiem: ''
  })

  const update = () => {
    const now = dateUtil()

    const h = now.format('HH')
    const m = now.format('mm')
    const s = now.get('s')

    state.year = now.get('y')
    state.month = now.get('M') + 1
    state.week =
      t('auto.hooks.web.useNow.ke3526937') +
      [
        t('auto.hooks.web.useNow.k15917f3b'),
        t('auto.hooks.web.useNow.kd274eee8'),
        t('auto.hooks.web.useNow.k1d5639f7'),
        t('auto.hooks.web.useNow.k49ddb069'),
        t('auto.hooks.web.useNow.k4f88740b'),
        t('auto.hooks.web.useNow.k8f07f53d'),
        t('auto.hooks.web.useNow.k3d72c724')
      ][now.day()]
    state.day = now.get('date')
    state.hour = h
    state.minute = m
    state.second = s

    state.meridiem = now.format('A')
  }

  function start() {
    update()
    clearInterval(timer)
    timer = setInterval(() => update(), 1000)
  }

  function stop() {
    clearInterval(timer)
  }

  tryOnMounted(() => {
    immediate && start()
  })

  tryOnUnmounted(() => {
    stop()
  })

  return {
    ...toRefs(state),
    start,
    stop
  }
}
