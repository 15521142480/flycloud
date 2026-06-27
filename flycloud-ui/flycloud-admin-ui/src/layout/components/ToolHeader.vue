<script lang="tsx">
import { defineComponent, computed } from 'vue'
import { Message } from '@/layout/components//Message'
import { Collapse } from '@/layout/components/Collapse'
import { UserInfo } from '@/layout/components/UserInfo'
import { Screenfull } from '@/layout/components/Screenfull'
import { Breadcrumb } from '@/layout/components/Breadcrumb'
import { PreferenceDropdown } from '@/layout/components/PreferenceDropdown'
import RouterSearch from '@/components/RouterSearch/index.vue'
import { useAppStore } from '@/store/modules/app'
import { useDesign } from '@/hooks/web/useDesign'

const { getPrefixCls, variables } = useDesign()

const prefixCls = getPrefixCls('tool-header')

const appStore = useAppStore()

// 面包屑
const breadcrumb = computed(() => appStore.getBreadcrumb)

// 折叠图标
const hamburger = computed(() => appStore.getHamburger)

// 全屏图标
const screenfull = computed(() => appStore.getScreenfull)

// 搜索图片
const search = computed(() => appStore.search)

// 布局
const layout = computed(() => appStore.getLayout)

// 消息图标
const message = computed(() => appStore.getMessage)

export default defineComponent({
  name: 'ToolHeader',
  setup() {
    return () => (
      <div
        id={`${variables.namespace}-tool-header`}
        class={[
          prefixCls,
          'h-[var(--top-tool-height)] relative px-[var(--top-tool-p-x)] flex items-center justify-between',
          'dark:bg-[var(--el-bg-color)]'
        ]}
      >
        {layout.value !== 'top' ? (
          <div class="h-full flex items-center">
            {hamburger.value && layout.value !== 'cutMenu' ? (
              <Collapse class="custom-hover" color="var(--top-header-text-color)"></Collapse>
            ) : undefined}
            {breadcrumb.value ? <Breadcrumb class="lt-md:hidden"></Breadcrumb> : undefined}
          </div>
        ) : undefined}
        <div class="h-full flex items-center">
          {screenfull.value ? (
            <Screenfull
              class="custom-hover top-icon-action top-icon-action--screenfull"
              color="var(--top-header-text-color)"
            ></Screenfull>
          ) : undefined}
          {search.value ? (
            <RouterSearch class="top-icon-action top-icon-action--wide" isModal={false} />
          ) : undefined}
          <span class="tool-divider"></span>
          <PreferenceDropdown color="var(--top-header-text-color)"></PreferenceDropdown>
          <span class="tool-divider"></span>
          {message.value ? (
            <Message
              class="custom-hover top-icon-action top-icon-action--wide"
              color="var(--top-header-text-color)"
            ></Message>
          ) : undefined}
          <UserInfo></UserInfo>
        </div>
      </div>
    )
  }
})
</script>

<style lang="scss" scoped>
$prefix-cls: #{$namespace}-tool-header;

.#{$prefix-cls} {
  transition: left var(--transition-time-02);
}

:deep(.top-icon-action) {
  min-width: 42px;
  height: 100%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

:deep(.top-icon-action--wide) {
  min-width: 63px;
}

:deep(.top-icon-action--screenfull) {
  min-width: 70px;
}

.tool-divider {
  width: 1px;
  height: 24px;
  margin: 0 8px;
  background: var(--el-border-color);
}
</style>
