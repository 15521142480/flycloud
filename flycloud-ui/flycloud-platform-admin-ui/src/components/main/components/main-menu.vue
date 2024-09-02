<template>
  <div class="main-menu">
    <Menu ref="menu" :active-name="activeName"  theme="dark" width="auto" @on-select="handleSelectMenu">
      <template v-for="item in menuDataList">
        <template v-if="item.children && item.children.length > 0">
          <!-- 此处如果需要递归菜单，写一个字菜单组件即可（本系统只做二级菜单） -->
          <Submenu :name="item.path" :key="item.id">
            <template slot="title">
              <Icon :type="item.icon" :size="getIconSize" />
              {{ item.title }}
            </template>
            <template v-for="itemTwo in item.children">
              <Menu-item :name="itemTwo.path" :key="itemTwo.id">
                <Icon :type="itemTwo.icon" :size="getIconSize"></Icon>
                <span class="layout-text">{{ itemTwo.title }}</span>
              </Menu-item>
            </template>
          </Submenu>
        </template>
        <template v-else>
          <Menu-item :name="item.path" :key="item.id">
            <Icon :type="item.icon" :size="getIconSize"></Icon>
            <span class="layout-text">{{ item.title }}</span>
          </Menu-item>
        </template>
      </template>
    </Menu>
  </div>
</template>

<script>
import { getUserMenu } from '../../../util/cacheUtils'
export default {
  components: {},
  props: {
    spanLeft: {
      type: Number,
      default () {
        return 5
      }
    }
  },
  data () {
    return {
      activeName: '', // 当前菜单所选的位置
      menuDataList: getUserMenu(),
      name: ''
    }
  },

  computed: {
    getIconSize () {
      return this.spanLeft === 5 ? 14 : 24
    }
  },
  methods: {
    /**
     * 选择菜单
     */
    handleSelectMenu (menuRouterPath) {
      this.$emit('on-menu-select', menuRouterPath)
    }
  },
  watch: {},
  created () {

  },
  mounted () {
  }
}
</script>

<style scoped lang="scss">
</style>
