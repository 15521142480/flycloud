"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  name: "AiProgress",
  components: {},
  props: {
    // 进度条的值
    percentage: {
      type: [Number, String],
      required: true
    },
    // 是否内联显示数据
    textInside: {
      type: Boolean,
      default: false
    },
    // 进度条高度
    strokeWidth: {
      type: [Number, String],
      default: 6
    },
    // 默认动画时长
    duration: {
      type: [Number, String],
      default: 2e3
    },
    // 是否有动画
    isAnimate: {
      type: Boolean,
      default: false
    },
    // 背景颜色
    bgColor: {
      type: String,
      default: "linear-gradient(90deg, var(--ui-BG-Main) 0%, var(--ui-BG-Main-gradient) 100%)"
    },
    // 是否不显示数据
    noData: {
      type: Boolean,
      default: false
    },
    // 是否自定义显示内容
    lineData: {
      type: Boolean,
      default: false
    },
    // 自定义底色
    inBgColor: {
      type: String,
      default: "#ebeef5"
    }
  },
  data() {
    return {
      width: 0,
      timer: null,
      containerWidth: 0,
      contentWidth: 0
    };
  },
  methods: {
    start() {
      if (this.isAnimate) {
        const container = common_vendor.index.createSelectorQuery().in(this).selectAll("#container");
        common_vendor.index.createSelectorQuery().in(this).selectAll("#content");
        container.boundingClientRect().exec((res1) => {
          this.contentWidth = res1[0][0].width * 1 * (this.percentage * 1 / 100).toFixed(2) + "px";
        });
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.start();
    });
  },
  created() {
  },
  filters: {},
  computed: {},
  watch: {},
  directives: {}
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $props.lineData
  }, $props.lineData ? {} : {}, {
    b: $props.isAnimate
  }, $props.isAnimate ? common_vendor.e({
    c: $props.textInside && !$props.noData
  }, $props.textInside && !$props.noData ? {
    d: common_vendor.t($props.percentage)
  } : {}, {
    e: $props.strokeWidth + "px",
    f: $props.bgColor,
    g: $data.contentWidth,
    h: `width ${$props.duration / 1e3}s ease`
  }) : {}, {
    i: !$props.isAnimate
  }, !$props.isAnimate ? common_vendor.e({
    j: $props.textInside && !$props.noData
  }, $props.textInside && !$props.noData ? {
    k: common_vendor.t($props.percentage)
  } : {}, {
    l: $props.percentage + "%",
    m: $props.strokeWidth + "px",
    n: $props.bgColor
  }) : {}, {
    o: $props.inBgColor,
    p: !$props.textInside && !$props.lineData && !$props.noData && !$props.isAnimate
  }, !$props.textInside && !$props.lineData && !$props.noData && !$props.isAnimate ? {
    q: common_vendor.t($props.percentage)
  } : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-e717a0ca"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-progress/su-progress.js.map
