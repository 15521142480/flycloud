"use strict";
const common_vendor = require("../../../common/vendor.js");
require("../../index.js");
const _sfc_main = {
  name: "UniNoticeBar",
  emits: ["click", "getmore", "close"],
  props: {
    text: {
      type: String,
      default: ""
    },
    moreText: {
      type: String,
      default: ""
    },
    backgroundColor: {
      type: String,
      default: ""
    },
    speed: {
      // 默认1s滚动100px
      type: Number,
      default: 100
    },
    color: {
      type: String,
      default: "var(--ui-BG-Main)"
    },
    moreColor: {
      type: String,
      default: "#FF9A43"
    },
    single: {
      // 是否单行
      type: [Boolean, String],
      default: false
    },
    scrollable: {
      // 是否滚动，添加后控制单行效果取消
      type: [Boolean, String],
      default: false
    },
    showIcon: {
      // 是否显示左侧icon
      type: [Boolean, String],
      default: false
    },
    showGetMore: {
      // 是否显示右侧查看更多
      type: [Boolean, String],
      default: false
    },
    showClose: {
      // 是否显示左侧关闭按钮
      type: [Boolean, String],
      default: false
    }
  },
  data() {
    const elId = `Uni_${Math.ceil(Math.random() * 1e6).toString(36)}`;
    const elIdBox = `Uni_${Math.ceil(Math.random() * 1e6).toString(36)}`;
    return {
      textWidth: 0,
      boxWidth: 0,
      wrapWidth: "",
      webviewHide: false,
      elId,
      elIdBox,
      show: true,
      animationDuration: "none",
      animationPlayState: "paused",
      animationDelay: "0s"
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initSize();
    });
  },
  methods: {
    initSize() {
      if (this.scrollable) {
        let query = [];
        let textQuery = new Promise((resolve, reject) => {
          common_vendor.index.createSelectorQuery().in(this).select(`#${this.elId}`).boundingClientRect().exec((ret) => {
            this.textWidth = ret[0].width;
            resolve();
          });
        });
        let boxQuery = new Promise((resolve, reject) => {
          common_vendor.index.createSelectorQuery().in(this).select(`#${this.elIdBox}`).boundingClientRect().exec((ret) => {
            this.boxWidth = ret[0].width;
            resolve();
          });
        });
        query.push(textQuery);
        query.push(boxQuery);
        Promise.all(query).then(() => {
          this.animationDuration = `${this.textWidth / this.speed}s`;
          this.animationDelay = `-${this.boxWidth / this.speed}s`;
          setTimeout(() => {
            this.animationPlayState = "running";
          }, 1e3);
        });
      }
    },
    loopAnimation() {
    },
    clickMore() {
      this.$emit("getmore");
    },
    close() {
      this.show = false;
      this.$emit("close");
    },
    onClick() {
      this.$emit("click");
    }
  }
};
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  _easycom_uni_icons2();
}
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  _easycom_uni_icons();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.show
  }, $data.show ? common_vendor.e({
    b: $props.showIcon === true || $props.showIcon === "true"
  }, $props.showIcon === true || $props.showIcon === "true" ? {
    c: common_vendor.p({
      type: "sound",
      color: $props.color,
      size: "22"
    })
  } : {}, {
    d: common_vendor.t($props.text),
    e: $data.elId,
    f: $props.scrollable ? 1 : "",
    g: !$props.scrollable && ($props.single || $props.showGetMore) ? 1 : "",
    h: $props.color,
    i: $data.wrapWidth + "px",
    j: $data.animationDuration,
    k: $data.animationDuration,
    l: $data.webviewHide ? "paused" : $data.animationPlayState,
    m: $data.webviewHide ? "paused" : $data.animationPlayState,
    n: $data.animationDelay,
    o: $data.animationDelay,
    p: $data.elIdBox,
    q: $props.scrollable ? 1 : "",
    r: !$props.scrollable && ($props.single || $props.moreText) ? 1 : "",
    s: $props.scrollable ? 1 : "",
    t: !$props.scrollable && ($props.single || $props.moreText) ? 1 : "",
    v: $props.showGetMore === true || $props.showGetMore === "true"
  }, $props.showGetMore === true || $props.showGetMore === "true" ? common_vendor.e({
    w: $props.moreText.length > 0
  }, $props.moreText.length > 0 ? {
    x: common_vendor.t($props.moreText),
    y: $props.moreColor
  } : {
    z: common_vendor.p({
      type: "right",
      color: $props.moreColor,
      size: "16"
    })
  }, {
    A: common_vendor.o((...args) => $options.clickMore && $options.clickMore(...args), "35")
  }) : {}, {
    B: ($props.showClose === true || $props.showClose === "true") && ($props.showGetMore === false || $props.showGetMore === "false")
  }, ($props.showClose === true || $props.showClose === "true") && ($props.showGetMore === false || $props.showGetMore === "false") ? {
    C: common_vendor.p({
      type: "closeempty",
      color: $props.color,
      size: "16"
    }),
    D: common_vendor.o((...args) => $options.close && $options.close(...args), "5e")
  } : {}, {
    E: $props.backgroundColor,
    F: common_vendor.o((...args) => $options.onClick && $options.onClick(...args), "07")
  }) : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-8eee8640"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-notice-bar/su-notice-bar.js.map
