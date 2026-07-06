"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../index.js");
if (!Array) {
  const _easycom_su_status_bar2 = common_vendor.resolveComponent("su-status-bar");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  (_easycom_su_status_bar2 + _easycom_su_fixed2)();
}
const _easycom_su_status_bar = () => "../../../ui/su-status-bar/su-status-bar.js";
const _easycom_su_fixed = () => "../../../ui/su-fixed/su-fixed.js";
if (!Math) {
  (_easycom_su_status_bar + _easycom_su_fixed)();
}
const _sfc_main = {
  __name: "navbar",
  props: {
    sticky: Boolean,
    zIndex: {
      type: Number,
      default: 100
    },
    back: {
      //是否返回上一页
      type: Boolean,
      default: true
    },
    backtext: {
      //返回文本
      type: String,
      default: ""
    },
    bg: {
      type: String,
      default: "bg-white"
    },
    status: {
      //状态栏颜色 可以选择light dark/其他字符串视为黑色
      type: String,
      default: ""
    },
    // 常驻
    alway: {
      type: Boolean,
      default: true
    },
    opacity: {
      //是否开启滑动渐变
      type: Boolean,
      default: false
    },
    opacityBg: {
      //开启滑动渐变后 返回按钮是否添加背景
      type: Boolean,
      default: false
    },
    noFixed: {
      //是否浮动
      type: Boolean,
      default: false
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
      default: true
    },
    bgStyles: {
      type: Object,
      default() {
      }
    }
  },
  emits: ["navback"],
  setup(__props, { emit: __emit }) {
    const state = common_vendor.reactive({
      statusCur: "",
      capsuleStyle: {},
      capsuleBack: {}
    });
    const sys_statusBar = sheep_index.sheep.$platform.device.statusBarHeight;
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const props = __props;
    common_vendor.onBeforeMount(() => {
      init();
    });
    const init = () => {
      state.capsuleStyle = {
        width: sheep_index.sheep.$platform.capsule.width + "px",
        height: sheep_index.sheep.$platform.capsule.height + "px",
        margin: "0 " + (sheep_index.sheep.$platform.device.windowWidth - sheep_index.sheep.$platform.capsule.right) + "px"
      };
      state.capsuleBack = state.capsuleStyle;
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.s(state.capsuleStyle),
        b: common_vendor.n(props.status == "" ? `text-a` : props.status == "light" ? "text-white" : "text-black"),
        c: common_vendor.s({
          height: common_vendor.unref(sys_navBar) - common_vendor.unref(sys_statusBar) + "px"
        }),
        d: common_vendor.p({
          noFixed: props.noFixed,
          alway: props.alway,
          bgStyles: props.bgStyles,
          val: 0,
          index: props.zIndex,
          noNav: true,
          bg: props.bg,
          ui: props.ui,
          opacity: props.opacity,
          placeholder: props.placeholder,
          sticky: props.sticky
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-81e1f503"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-custom-navbar/components/navbar.js.map
