"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  name: "SuPopup",
  components: {},
  emits: ["change", "maskClick", "close"],
  props: {
    // 开启状态
    show: {
      type: Boolean,
      default: false
    },
    // 顶部，底部时有效
    space: {
      type: Number,
      default: 0
    },
    // 默认圆角
    round: {
      type: [String, Number],
      default: 0
    },
    // 是否显示关闭
    showClose: {
      type: Boolean,
      default: false
    },
    // 开启动画
    animation: {
      type: Boolean,
      default: true
    },
    // 弹出层类型，可选值，top: 顶部弹出层；bottom：底部弹出层；center：全屏弹出层
    // message: 消息提示 ; dialog : 对话框
    type: {
      type: String,
      default: "bottom"
    },
    // maskClick
    isMaskClick: {
      type: Boolean,
      default: null
    },
    // TODO 2 个版本后废弃属性 ，使用 isMaskClick
    maskClick: {
      type: Boolean,
      default: null
    },
    // 可设置none
    backgroundColor: {
      type: String,
      default: "#ffffff"
    },
    backgroundImage: {
      type: String,
      default: ""
    },
    safeArea: {
      type: Boolean,
      default: true
    },
    maskBackgroundColor: {
      type: String,
      default: "rgba(0, 0, 0, 0.4)"
    },
    zIndex: {
      type: [String, Number],
      default: 10075
    }
  },
  watch: {
    show: {
      handler: function(newValue, oldValue) {
        if (typeof oldValue === "undefined" && !newValue) {
          return;
        }
        if (newValue) {
          this.open();
        } else {
          this.close();
        }
      },
      immediate: true
    },
    /**
     * 监听type类型
     */
    type: {
      handler: function(type) {
        if (!this.config[type])
          return;
        this[this.config[type]](true);
      },
      immediate: true
    },
    isDesktop: {
      handler: function(newVal) {
        if (!this.config[newVal])
          return;
        this[this.config[this.type]](true);
      },
      immediate: true
    },
    /**
     * 监听遮罩是否可点击
     * @param {Object} val
     */
    maskClick: {
      handler: function(val) {
        this.mkclick = val;
      },
      immediate: true
    },
    isMaskClick: {
      handler: function(val) {
        this.mkclick = val;
      },
      immediate: true
    },
    // H5 下禁止底部滚动
    showPopup(show) {
    }
  },
  data() {
    return {
      sheep: sheep_index.sheep,
      duration: 300,
      ani: [],
      showPopup: false,
      showTrans: false,
      popupWidth: 0,
      popupHeight: 0,
      config: {
        top: "top",
        bottom: "bottom",
        center: "center",
        left: "left",
        right: "right",
        message: "top",
        dialog: "center",
        share: "bottom"
      },
      maskClass: {
        position: "fixed",
        bottom: 0,
        top: 0,
        left: 0,
        right: 0,
        backgroundColor: "rgba(0, 0, 0, 0.4)"
      },
      transClass: {
        position: "fixed",
        left: 0,
        right: 0
      },
      maskShow: true,
      mkclick: true,
      popupstyle: this.isDesktop ? "fixforpc-top" : "top"
    };
  },
  computed: {
    isDesktop() {
      return this.popupWidth >= 500 && this.popupHeight >= 500;
    },
    bg() {
      if (this.backgroundColor === "" || this.backgroundColor === "none") {
        return "transparent";
      }
      return this.backgroundColor;
    },
    borderRadius() {
      if (this.round) {
        if (this.type === "bottom") {
          return {
            "border-top-left-radius": parseFloat(this.round) + "px",
            "border-top-right-radius": parseFloat(this.round) + "px"
          };
        }
        if (this.type === "center") {
          return {
            "border-top-left-radius": parseFloat(this.round) + "px",
            "border-top-right-radius": parseFloat(this.round) + "px",
            "border-bottom-left-radius": parseFloat(this.round) + "px",
            "border-bottom-right-radius": parseFloat(this.round) + "px"
          };
        }
        if (this.type === "top") {
          return {
            "border-bottom-left-radius": parseFloat(this.round) + "px",
            "border-bottom-right-radius": parseFloat(this.round) + "px"
          };
        }
      }
    }
  },
  mounted() {
    const fixSize = () => {
      const { windowWidth, windowHeight, windowTop, safeArea, screenHeight, safeAreaInsets } = sheep_index.sheep.$platform.device;
      this.popupWidth = windowWidth;
      this.popupHeight = windowHeight + (windowTop || 0);
      if (safeArea && this.safeArea) {
        this.safeAreaInsets = screenHeight - safeArea.bottom;
      } else {
        this.safeAreaInsets = 0;
      }
    };
    fixSize();
  },
  // TODO vue3
  unmounted() {
    this.setH5Visible();
  },
  created() {
    if (this.isMaskClick === null && this.maskClick === null) {
      this.mkclick = true;
    } else {
      this.mkclick = this.isMaskClick !== null ? this.isMaskClick : this.maskClick;
    }
    if (this.animation) {
      this.duration = 300;
    } else {
      this.duration = 0;
    }
    this.messageChild = null;
    this.clearPropagation = false;
    this.maskClass.backgroundColor = this.maskBackgroundColor;
  },
  methods: {
    setH5Visible() {
    },
    /**
     * 公用方法，不显示遮罩层
     */
    closeMask() {
      this.maskShow = false;
    },
    /**
     * 公用方法，遮罩层禁止点击
     */
    disableMask() {
      this.mkclick = false;
    },
    // TODO nvue 取消冒泡
    clear(e) {
      e.stopPropagation();
      this.clearPropagation = true;
    },
    open(direction) {
      if (this.showPopup) {
        clearTimeout(this.timer);
        this.showPopup = false;
      }
      let innerType = ["top", "center", "bottom", "left", "right", "message", "dialog", "share"];
      if (!(direction && innerType.indexOf(direction) !== -1)) {
        direction = this.type;
      }
      if (!this.config[direction]) {
        common_vendor.index.__f__("error", "at sheep/ui/su-popup/su-popup.vue:382", "缺少类型：", direction);
        return;
      }
      this[this.config[direction]]();
      this.$emit("change", {
        show: true,
        type: direction
      });
    },
    close(type) {
      this.showTrans = false;
      this.$emit("change", {
        show: false,
        type: this.type
      });
      this.$emit("close");
      clearTimeout(this.timer);
      this.timer = setTimeout(() => {
        this.showPopup = false;
      }, 300);
    },
    // TODO 处理冒泡事件，头条的冒泡事件有问题 ，先这样兼容
    touchstart() {
      this.clearPropagation = false;
    },
    onTap() {
      if (this.clearPropagation) {
        this.clearPropagation = false;
        return;
      }
      this.$emit("maskClick");
      if (!this.mkclick)
        return;
      this.close();
    },
    /**
     * 顶部弹出样式处理
     */
    top(type) {
      this.popupstyle = this.isDesktop ? "fixforpc-top" : "top";
      this.ani = ["slide-top"];
      this.transClass = {
        position: "fixed",
        left: 0,
        right: 0,
        top: this.space + "px",
        backgroundColor: this.bg
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
      this.$nextTick(() => {
        if (this.messageChild && this.type === "message") {
          this.messageChild.timerClose();
        }
      });
    },
    /**
     * 底部弹出样式处理
     */
    bottom(type) {
      this.popupstyle = "bottom";
      this.ani = ["slide-bottom"];
      this.transClass = {
        position: "fixed",
        left: 0,
        right: 0,
        bottom: 0,
        paddingBottom: this.safeAreaInsets + this.space + "px",
        backgroundColor: this.bg
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    },
    /**
     * 中间弹出样式处理
     */
    center(type) {
      this.popupstyle = "center";
      this.ani = ["zoom-out", "fade"];
      this.transClass = {
        position: "fixed",
        display: "flex",
        flexDirection: "column",
        bottom: 0,
        left: 0,
        right: 0,
        top: 0,
        justifyContent: "center",
        alignItems: "center"
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    },
    left(type) {
      this.popupstyle = "left";
      this.ani = ["slide-left"];
      this.transClass = {
        position: "fixed",
        left: 0,
        bottom: 0,
        top: 0,
        backgroundColor: this.bg,
        display: "flex",
        flexDirection: "column"
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    },
    right(type) {
      this.popupstyle = "right";
      this.ani = ["slide-right"];
      this.transClass = {
        position: "fixed",
        bottom: 0,
        right: 0,
        top: 0,
        backgroundColor: this.bg,
        display: "flex",
        flexDirection: "column"
      };
      if (type)
        return;
      this.showPopup = true;
      this.showTrans = true;
    }
  }
};
const __injectCSSVars__ = () => {
  common_vendor.useCssVars((_ctx) => ({
    "2741299f": _ctx.backgroundImage
  }));
};
const __setup__ = _sfc_main.setup;
_sfc_main.setup = __setup__ ? (props, ctx) => {
  __injectCSSVars__();
  return __setup__(props, ctx);
} : __injectCSSVars__;
if (!Array) {
  const _easycom_uni_transition2 = common_vendor.resolveComponent("uni-transition");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  (_easycom_uni_transition2 + _easycom_uni_icons2)();
}
const _easycom_uni_transition = () => "../../../uni_modules/uni-transition/components/uni-transition/uni-transition.js";
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  (_easycom_uni_transition + _easycom_uni_icons)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.showPopup
  }, $data.showPopup ? common_vendor.e({
    b: $data.maskShow
  }, $data.maskShow ? {
    c: common_vendor.o($options.onTap, "6c"),
    d: common_vendor.p({
      name: "mask",
      ["mode-class"]: "fade",
      styles: $data.maskClass,
      duration: $data.duration,
      show: $data.showTrans
    })
  } : {}, {
    e: $data.showPopup
  }, $data.showPopup ? common_vendor.e({
    f: $props.showClose
  }, $props.showClose ? {
    g: common_vendor.o($options.close, "96"),
    h: common_vendor.p({
      color: "#F6F6F6",
      type: "closeempty",
      size: "32"
    })
  } : {}, {
    i: common_vendor.s({
      backgroundColor: $options.bg
    }),
    j: common_vendor.s($options.borderRadius),
    k: common_vendor.n($data.popupstyle),
    l: common_vendor.o((...args) => $options.clear && $options.clear(...args), "ec")
  }) : {}, {
    m: common_vendor.o($options.onTap, "28"),
    n: common_vendor.p({
      ["mode-class"]: $data.ani,
      name: "content",
      styles: {
        ...$data.transClass,
        ...$options.borderRadius
      },
      duration: $data.duration,
      show: $data.showTrans
    }),
    o: common_vendor.o((...args) => $options.touchstart && $options.touchstart(...args), "a3"),
    p: common_vendor.n($data.popupstyle),
    q: common_vendor.n($options.isDesktop ? "fixforpc-z-index" : ""),
    r: common_vendor.s({
      zIndex: $props.zIndex
    }),
    s: common_vendor.s(_ctx.__cssVars()),
    t: common_vendor.o((...args) => $options.clear && $options.clear(...args), "9f")
  }) : {
    v: common_vendor.s(_ctx.__cssVars())
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-popup/su-popup.js.map
