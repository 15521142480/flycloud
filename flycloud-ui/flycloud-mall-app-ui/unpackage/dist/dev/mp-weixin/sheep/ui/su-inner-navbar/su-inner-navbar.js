"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Array) {
  const _easycom_su_status_bar2 = common_vendor.resolveComponent("su-status-bar");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  (_easycom_su_status_bar2 + _easycom_su_fixed2)();
}
const _easycom_su_status_bar = () => "../su-status-bar/su-status-bar.js";
const _easycom_su_fixed = () => "../su-fixed/su-fixed.js";
if (!Math) {
  (_easycom_su_status_bar + _easycom_su_fixed)();
}
const _sfc_main = {
  __name: "su-inner-navbar",
  props: {
    zIndex: {
      type: Number,
      default: 100
    },
    title: {
      //返回文本
      type: String,
      default: ""
    },
    bg: {
      type: String,
      default: "bg-white"
    },
    // 常驻
    alway: {
      type: Boolean,
      default: true
    },
    opacity: {
      //是否开启滑动渐变
      type: Boolean,
      default: true
    },
    noFixed: {
      //是否浮动
      type: Boolean,
      default: true
    },
    ui: {
      type: String,
      default: ""
    },
    capsule: {
      //是否开启胶囊返回
      type: Boolean,
      default: false
    },
    stopBack: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: [Boolean],
      default: false
    },
    bgStyles: {
      type: Object,
      default() {
      }
    }
  },
  emits: ["navback", "clickLeft"],
  setup(__props, { emit: __emit }) {
    const state = common_vendor.reactive({
      statusCur: "",
      capsuleStyle: {},
      capsuleBack: {},
      isDark: true
    });
    const sys_statusBar = sheep_index.sheep.$platform.device.statusBarHeight;
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const props = __props;
    const emits = __emit;
    const hasHistory = sheep_index.sheep.$router.hasHistory();
    common_vendor.onBeforeMount(() => {
      init();
    });
    common_vendor.onPageScroll((e) => {
      let top = e.scrollTop;
      state.isDark = top < sheep_index.sheep.$platform.navbar;
    });
    function onClickLeft() {
      if (hasHistory) {
        sheep_index.sheep.$router.back();
      } else {
        sheep_index.sheep.$router.go("/pages/index/index");
      }
      emits("clickLeft");
    }
    function onClickRight() {
      sheep_hooks_useModal.showMenuTools();
    }
    const init = () => {
      state.capsuleStyle = {
        width: sheep_index.sheep.$platform.capsule.width + "px",
        height: sheep_index.sheep.$platform.capsule.height + "px"
      };
      state.capsuleBack = state.capsuleStyle;
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(hasHistory)
      }, common_vendor.unref(hasHistory) ? {} : {}, {
        b: common_vendor.o(onClickLeft, "13"),
        c: common_vendor.o(onClickRight, "a5"),
        d: common_vendor.t(__props.title),
        e: common_vendor.s(state.capsuleStyle),
        f: common_vendor.n({
          "text-white": state.isDark,
          "text-black": !state.isDark,
          "ss-p-x-20": common_vendor.unref(sheep_index.sheep).$platform.provider !== "alipay"
        }),
        g: common_vendor.s({
          height: common_vendor.unref(sys_navBar) - common_vendor.unref(sys_statusBar) + "px"
        }),
        h: common_vendor.p({
          noFixed: props.noFixed,
          alway: props.alway,
          bgStyles: props.bgStyles,
          val: 0,
          index: props.zIndex,
          noNav: true,
          bg: props.bg,
          ui: props.ui,
          opacity: props.opacity,
          placeholder: props.placeholder
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-374b2fee"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-inner-navbar/su-inner-navbar.js.map
