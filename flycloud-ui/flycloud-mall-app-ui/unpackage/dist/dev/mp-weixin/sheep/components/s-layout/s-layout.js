"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_su_navbar2 = common_vendor.resolveComponent("su-navbar");
  const _easycom_s_custom_navbar2 = common_vendor.resolveComponent("s-custom-navbar");
  const _easycom_su_inner_navbar2 = common_vendor.resolveComponent("su-inner-navbar");
  const _easycom_s_tabbar2 = common_vendor.resolveComponent("s-tabbar");
  const _easycom_s_auth_modal2 = common_vendor.resolveComponent("s-auth-modal");
  const _easycom_s_share_modal2 = common_vendor.resolveComponent("s-share-modal");
  const _easycom_s_menu_tools2 = common_vendor.resolveComponent("s-menu-tools");
  (_easycom_su_navbar2 + _easycom_s_custom_navbar2 + _easycom_su_inner_navbar2 + _easycom_s_tabbar2 + _easycom_s_auth_modal2 + _easycom_s_share_modal2 + _easycom_s_menu_tools2)();
}
const _easycom_su_navbar = () => "../../ui/su-navbar/su-navbar.js";
const _easycom_s_custom_navbar = () => "../s-custom-navbar/s-custom-navbar.js";
const _easycom_su_inner_navbar = () => "../../ui/su-inner-navbar/su-inner-navbar.js";
const _easycom_s_tabbar = () => "../s-tabbar/s-tabbar.js";
const _easycom_s_auth_modal = () => "../s-auth-modal/s-auth-modal.js";
const _easycom_s_share_modal = () => "../s-share-modal/s-share-modal.js";
const _easycom_s_menu_tools = () => "../s-menu-tools/s-menu-tools.js";
if (!Math) {
  (_easycom_su_navbar + _easycom_s_custom_navbar + _easycom_su_inner_navbar + _easycom_s_tabbar + _easycom_s_auth_modal + _easycom_s_share_modal + _easycom_s_menu_tools)();
}
const _sfc_main = {
  __name: "s-layout",
  props: {
    title: {
      type: String,
      default: ""
    },
    navbar: {
      type: String,
      default: "normal"
    },
    opacityBgUi: {
      type: String,
      default: "bg-white"
    },
    // 顶部导航栏背景颜色（仅在 navbar === 'normal' 时生效）
    navbarBackgroundColor: {
      type: String,
      default: ""
    },
    color: {
      type: String,
      default: ""
    },
    tools: {
      type: String,
      default: "title"
    },
    keyword: {
      type: String,
      default: ""
    },
    navbarStyle: {
      type: Object,
      default: () => ({
        styleType: "",
        type: "",
        color: "",
        src: "",
        list: [],
        alwaysShow: 0
      })
    },
    bgStyle: {
      type: Object,
      default: () => ({
        src: "",
        color: "var(--ui-BG-1)"
      })
    },
    tabbar: {
      type: [String, Boolean],
      default: ""
    },
    onShareAppMessage: {
      type: [Boolean, Object],
      default: true
    },
    leftWidth: {
      type: [Number, String],
      default: 100
    },
    rightWidth: {
      type: [Number, String],
      default: 100
    },
    defaultSearch: {
      type: String,
      default: ""
    },
    //展示返回按钮
    showLeftButton: {
      type: Boolean,
      default: false
    }
  },
  emits: ["search"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const sysStore = sheep_index.sheep.$store("sys");
    sheep_index.sheep.$store("user");
    sheep_index.sheep.$store("app");
    sheep_index.sheep.$store("modal");
    const sys = common_vendor.computed(() => sysStore);
    const navbarMode = common_vendor.computed(() => {
      if (props.navbar === "normal" || props.navbarStyle.styleType === "normal") {
        return "normal";
      }
      return "inner";
    });
    const bgMain = common_vendor.computed(() => {
      if (navbarMode.value === "inner") {
        return {
          background: `${props.bgStyle.backgroundColor || props.bgStyle.color} url(${sheep_index.sheep.$url.cdn(
            props.bgStyle.backgroundImage
          )}) no-repeat top center / 100% auto`
        };
      }
      return {};
    });
    const bgBody = common_vendor.computed(() => {
      if (navbarMode.value === "normal") {
        return {
          background: `${props.bgStyle.backgroundColor || props.bgStyle.color} url(${sheep_index.sheep.$url.cdn(
            props.bgStyle.backgroundImage
          )}) no-repeat top center / 100% auto`
        };
      }
      return {};
    });
    const shareInfo = common_vendor.computed(() => {
      if (props.onShareAppMessage === true) {
        return sheep_index.sheep.$platform.share.getShareInfo();
      } else {
        if (!common_vendor.isEmpty(props.onShareAppMessage)) {
          sheep_index.sheep.$platform.share.updateShareInfo(props.onShareAppMessage);
          return props.onShareAppMessage;
        }
      }
      return {};
    });
    common_vendor.index.showShareMenu({
      withShareTicket: true,
      menus: ["shareAppMessage", "shareTimeline"]
    });
    common_vendor.onShareAppMessage(() => {
      return {
        title: shareInfo.value.title,
        path: shareInfo.value.forward.path,
        imageUrl: shareInfo.value.image
      };
    });
    common_vendor.onShareTimeline(() => {
      return {
        title: shareInfo.value.title,
        query: shareInfo.value.forward.path,
        imageUrl: shareInfo.value.image
      };
    });
    common_vendor.onMounted(() => {
      if (!common_vendor.isEmpty(shareInfo.value)) {
        sheep_index.sheep.$platform.share.updateShareInfo(shareInfo.value);
      }
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e;
      return common_vendor.e({
        a: __props.navbar === "normal"
      }, __props.navbar === "normal" ? {
        b: common_vendor.o((e) => emits("search", e), "a0"),
        c: common_vendor.p({
          title: __props.title,
          statusBar: true,
          color: __props.color,
          tools: __props.tools,
          opacityBgUi: __props.opacityBgUi,
          backgroundColor: __props.navbarBackgroundColor,
          defaultSearch: __props.defaultSearch
        })
      } : __props.navbar === "custom" && navbarMode.value === "normal" ? {
        e: common_vendor.p({
          data: __props.navbarStyle,
          showLeftButton: __props.showLeftButton
        })
      } : {}, {
        d: __props.navbar === "custom" && navbarMode.value === "normal",
        f: __props.navbar === "inner"
      }, __props.navbar === "inner" ? {
        g: common_vendor.p({
          title: __props.title
        })
      } : {}, {
        h: __props.navbar === "inner"
      }, __props.navbar === "inner" ? {
        i: common_vendor.s({
          paddingTop: ((_b = (_a = common_vendor.unref(sheep_index.sheep)) == null ? void 0 : _a.$platform) == null ? void 0 : _b.navbar) + "px"
        })
      } : {}, {
        j: __props.navbar === "custom" && navbarMode.value === "inner"
      }, __props.navbar === "custom" && navbarMode.value === "inner" ? {
        k: common_vendor.p({
          data: __props.navbarStyle,
          showLeftButton: __props.showLeftButton
        })
      } : {}, {
        l: __props.tabbar !== ""
      }, __props.tabbar !== "" ? {
        m: common_vendor.p({
          path: __props.tabbar
        })
      } : {}, {
        n: common_vendor.s(bgBody.value),
        o: common_vendor.s(bgMain.value),
        p: common_vendor.p({
          shareInfo: shareInfo.value
        }),
        q: common_vendor.n("theme-" + ((_c = sys.value) == null ? void 0 : _c.mode)),
        r: common_vendor.n("main-" + ((_d = sys.value) == null ? void 0 : _d.theme)),
        s: common_vendor.n("font-" + ((_e = sys.value) == null ? void 0 : _e.fontSize))
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-32d0115a"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-layout/s-layout.js.map
