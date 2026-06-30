<script lang="ts" setup>
import { ArrowDown, CollectionTag, Moon, Sunny } from '@element-plus/icons-vue'
import { useAppStore } from '@/store/modules/app'
import { useLocaleStore } from '@/store/modules/locale'
import { useLocale } from '@/hooks/web/useLocale'
import { useDesign } from '@/hooks/web/useDesign'
import { ElementPlusSize } from '@/types/elementPlus'
import { propTypes } from '@/utils/propTypes'
const { t } = useI18n()
defineOptions({ name: 'PreferenceDropdown' })

const props = defineProps({
  color: propTypes.string.def(''),
  showSize: propTypes.bool.def(true)
})
const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('preference-dropdown')
const appStore = useAppStore()
const localeStore = useLocaleStore()
const { changeLocale } = useLocale()

const sizeMap = computed(() => appStore.getSizeMap)
const langMap = computed(() => localeStore.getLocaleMap)
const currentLang = computed(() => localeStore.getCurrentLocale)
const currentSize = computed(() => appStore.getCurrentSize)
const isDark = computed(() => appStore.getIsDark)
const themeLabel = computed(() => t(isDark.value ? 'preference.dark' : 'preference.light'))
const localeLabel = computed(
  () => langMap.value.find((item) => item.lang === currentLang.value.lang)?.name || ''
)

const setThemeMode = (mode: 'light' | 'dark') => {
  appStore.setIsDark(mode === 'dark')
}

const setCurrentSize = (size: ElementPlusSize) => {
  appStore.setCurrentSize(size)
}

const setLang = async (lang: LocaleType) => {
  if (lang === unref(currentLang).lang) return
  await changeLocale(lang)
}
</script>

<template>
  <div :class="prefixCls" class="preference-actions">
    <ElDropdown trigger="click" @command="setThemeMode">
      <button class="preference-switcher" type="button" :aria-label="t('preference.theme')">
        <component :is="isDark ? Moon : Sunny" />
        <span>{{ themeLabel }}</span>
        <ArrowDown />
      </button>
      <template #dropdown>
        <ElDropdownMenu>
          <ElDropdownItem command="light" :disabled="!isDark">
            {{ t('preference.light') }}
          </ElDropdownItem>
          <ElDropdownItem command="dark" :disabled="isDark">
            {{ t('preference.dark') }}
          </ElDropdownItem>
        </ElDropdownMenu>
      </template>
    </ElDropdown>

    <ElDropdown v-if="props.showSize" trigger="click" @command="setCurrentSize">
      <button class="preference-switcher" type="button" :aria-label="t('preference.size')">
        <Icon :color="props.color" :size="16" icon="mdi:format-size" />
        <span>{{ t(`size.${currentSize}`) }}</span>
        <ArrowDown />
      </button>
      <template #dropdown>
        <ElDropdownMenu>
          <ElDropdownItem
            v-for="item in sizeMap"
            :key="item"
            :command="item"
            :disabled="item === currentSize"
          >
            {{ t(`size.${item}`) }}
          </ElDropdownItem>
        </ElDropdownMenu>
      </template>
    </ElDropdown>

    <ElDropdown trigger="click" @command="setLang">
      <button class="preference-switcher" type="button" :aria-label="t('preference.language')">
        <CollectionTag />
        <span>{{ localeLabel }}</span>
        <ArrowDown />
      </button>
      <template #dropdown>
        <ElDropdownMenu>
          <ElDropdownItem
            v-for="item in langMap"
            :key="item.lang"
            :command="item.lang"
            :disabled="item.lang === currentLang.lang"
          >
            {{ item.name }}
          </ElDropdownItem>
        </ElDropdownMenu>
      </template>
    </ElDropdown>
  </div>
</template>

<style lang="scss" scoped>
$prefix-cls: #{$namespace}-preference-dropdown;

.#{$prefix-cls} {
  display: inline-flex;
  align-items: center;
  height: 100%;
  gap: 6px;
}

.preference-switcher {
  height: 28px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 0 7px;
  border: 1px solid var(--app-border, var(--el-border-color));
  border-radius: 6px;
  background: var(--app-chip-bg, var(--el-bg-color));
  color: var(--app-text, var(--top-header-text-color));
  font-size: 12px;
  line-height: 1;
  cursor: pointer;

  svg {
    width: 13px;
    height: 13px;
  }
}

@media (max-width: 520px) {
  .preference-switcher span {
    display: none;
  }
}
</style>
