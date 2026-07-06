"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_helper_index = require("../../helper/index.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  name: "su-tabbar",
  props: {
    customStyle: {
      type: [Object, String],
      default: () => ({})
    },
    customClass: {
      type: String,
      default: ""
    },
    // 跳转的页面路径
    url: {
      type: String,
      default: ""
    },
    // 页面跳转的类型
    linkType: {
      type: String,
      default: "navigateTo"
    },
    // 当前匹配项的name
    value: {
      type: [String, Number, null],
      default: ""
    },
    // 是否为iPhoneX留出底部安全距离
    safeAreaInsetBottom: {
      type: Boolean,
      default: true
    },
    // 是否显示上方边框
    border: {
      type: Boolean,
      default: true
    },
    // 元素层级z-index
    zIndex: {
      type: [String, Number],
      default: 10
    },
    // 选中标签的颜色
    activeColor: {
      type: String,
      default: "#1989fa"
    },
    // 未选中标签的颜色
    inactiveColor: {
      type: String,
      default: "#7d7e80"
    },
    // 是否固定在底部
    fixed: {
      type: Boolean,
      default: true
    },
    // fixed定位固定在底部时，是否生成一个等高元素防止塌陷
    placeholder: {
      type: Boolean,
      default: true
    },
    midTabBar: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      placeholderHeight: 0,
      safeBottomHeight: sheep_index.sheep.$platform.device.safeAreaInsets.bottom
    };
  },
  computed: {
    tabbarStyle() {
      const style = {
        zIndex: this.zIndex
      };
      return sheep_helper_index.deepMerge(style, sheep_helper_index.addStyle(this.customStyle));
    },
    // 监听多个参数的变化，通过在computed执行对应的操作
    updateChild() {
      return [this.value, this.activeColor, this.inactiveColor];
    },
    updatePlaceholder() {
      return [this.fixed, this.placeholder];
    }
  },
  watch: {
    updateChild() {
      this.updateChildren();
    },
    updatePlaceholder() {
      this.setPlaceholderHeight();
    }
  },
  created() {
    this.children = [];
  },
  mounted() {
    this.setPlaceholderHeight();
  },
  methods: {
    updateChildren() {
      this.children.length && this.children.map((child) => child.updateFromParent());
    },
    getRect(selector, all) {
      return new Promise((resolve) => {
        common_vendor.index.createSelectorQuery().in(this)[all ? "selectAll" : "select"](selector).boundingClientRect((rect) => {
          if (all && Array.isArray(rect) && rect.length) {
            resolve(rect);
          }
          if (!all && rect) {
            resolve(rect);
          }
        }).exec();
      });
    },
    // 设置用于防止塌陷元素的高度
    async setPlaceholderHeight() {
      if (!this.fixed || !this.placeholder)
        return;
      await sheep_helper_index.sleep(20);
      this.getRect(".u-tabbar__content").then(({ height = 50 }) => {
        this.placeholderHeight = height;
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $props.safeAreaInsetBottom
  }, $props.safeAreaInsetBottom ? {
    b: common_vendor.s({
      height: $data.safeBottomHeight + "px"
    })
  } : {}, {
    c: common_vendor.o(() => {
    }, "ec"),
    d: common_vendor.n($props.border && "u-border-top"),
    e: common_vendor.n($props.fixed && "u-tabbar--fixed"),
    f: common_vendor.n({
      "mid-tabbar": $props.midTabBar
    }),
    g: common_vendor.s($options.tabbarStyle),
    h: $props.placeholder
  }, $props.placeholder ? {
    i: $data.placeholderHeight + "px"
  } : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-b5af0eb7"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-tabbar/su-tabbar.js.map
