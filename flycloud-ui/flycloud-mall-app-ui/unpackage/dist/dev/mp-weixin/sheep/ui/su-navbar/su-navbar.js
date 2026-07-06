"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Array) {
  const _easycom_su_status_bar2 = common_vendor.resolveComponent("su-status-bar");
  const _easycom_uni_search_bar2 = common_vendor.resolveComponent("uni-search-bar");
  (_easycom_su_status_bar2 + _easycom_uni_search_bar2)();
}
const _easycom_su_status_bar = () => "../su-status-bar/su-status-bar.js";
const _easycom_uni_search_bar = () => "../../../uni_modules/uni-search-bar/components/uni-search-bar/uni-search-bar.js";
if (!Math) {
  (_easycom_su_status_bar + _easycom_uni_search_bar)();
}
const _sfc_main = {
  __name: "su-navbar",
  props: {
    dark: {
      type: Boolean,
      default: false
    },
    modelValue: {
      type: String,
      default: ""
    },
    title: {
      type: String,
      default: ""
    },
    titleAlign: {
      type: String,
      default: "center"
      // left | center
    },
    rightText: {
      type: String,
      default: ""
    },
    leftIcon: {
      type: String,
      default: "left"
    },
    rightIcon: {
      type: String,
      default: ""
    },
    fixed: {
      type: [Boolean, String],
      default: true
    },
    placeholder: {
      type: [Boolean, String],
      default: true
    },
    color: {
      type: String,
      default: ""
    },
    backgroundColor: {
      type: String,
      default: ""
    },
    opacity: {
      type: [Boolean, String],
      default: false
    },
    opacityBgUi: {
      type: String,
      default: "bg-white"
    },
    statusBar: {
      type: [Boolean, String],
      default: false
    },
    shadow: {
      type: [Boolean, String],
      default: false
    },
    border: {
      type: [Boolean, String],
      default: false
    },
    height: {
      type: [Number, String],
      default: 44
    },
    leftWidth: {
      type: [Number, String],
      default: 80
    },
    rightWidth: {
      type: [Number, String],
      default: 0
    },
    tools: {
      type: String,
      default: "title"
    },
    defaultSearch: {
      type: String,
      default: ""
    }
  },
  emits: ["clickLeft", "clickRight", "clickTitle", "search"],
  setup(__props, { emit: __emit }) {
    const getVal = (val) => typeof val === "number" ? val + "px" : val;
    const emits = __emit;
    const props = __props;
    common_vendor.computed(() => {
      return {
        width: sheep_index.sheep.$platform.capsule.width + "px",
        height: sheep_index.sheep.$platform.capsule.height + "px",
        margin: "0 " + (sheep_index.sheep.$platform.device.windowWidth - sheep_index.sheep.$platform.capsule.right) + "px"
      };
    });
    const searchModel = common_vendor.computed(() => {
      return props.defaultSearch;
    });
    common_vendor.computed(() => {
      if (props.dark) {
        if (props.backgroundColor) {
          return props.backgroundColor;
        } else {
          return props.dark ? "#333" : "#FFF";
        }
      }
      return props.backgroundColor || "#FFF";
    });
    const themeColor = common_vendor.computed(() => {
      if (props.dark) {
        if (props.color) {
          return props.color;
        } else {
          return props.dark ? "#fff" : "#333";
        }
      }
      return props.color || "#333";
    });
    const navbarHeight = common_vendor.computed(() => {
      return getVal(props.height);
    });
    const leftIconWidth = common_vendor.computed(() => {
      return getVal(props.leftWidth);
    });
    common_vendor.computed(() => {
      return getVal(props.rightWidth);
    });
    function onSearch(e) {
      emits("search", e.value);
    }
    common_vendor.onLoad(() => {
      if (common_vendor.index.report && props.title !== "") {
        common_vendor.index.report("title", props.title);
      }
    });
    const hasHistory = sheep_index.sheep.$router.hasHistory();
    function onClickLeft() {
      if (hasHistory) {
        sheep_index.sheep.$router.back();
      } else {
        sheep_index.sheep.$router.go("/pages/index/index");
      }
      emits("clickLeft");
    }
    function onClickTitle() {
      emits("clickTitle");
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.n(__props.opacity ? "" : __props.opacityBgUi),
        b: __props.statusBar
      }, __props.statusBar ? {} : {}, {
        c: __props.leftIcon.length > 0
      }, __props.leftIcon.length > 0 ? common_vendor.e({
        d: common_vendor.unref(hasHistory)
      }, common_vendor.unref(hasHistory) ? {} : {}, {
        e: common_vendor.o(onClickLeft, "91"),
        f: common_vendor.o((...args) => common_vendor.unref(sheep_hooks_useModal.showMenuTools) && common_vendor.unref(sheep_hooks_useModal.showMenuTools)(...args), "fe")
      }) : {}, {
        g: __props.titleAlign === "left" && __props.title.length && common_vendor.unref(sheep_index.sheep).$platform.name !== "WechatOfficialAccount"
      }, __props.titleAlign === "left" && __props.title.length && common_vendor.unref(sheep_index.sheep).$platform.name !== "WechatOfficialAccount" ? {
        h: common_vendor.t(__props.title),
        i: themeColor.value,
        j: !__props.leftIcon.length > 0 ? 1 : ""
      } : {}, {
        k: leftIconWidth.value,
        l: __props.tools === "search"
      }, __props.tools === "search" ? {
        m: common_vendor.o(onSearch, "3f"),
        n: common_vendor.o(($event) => searchModel.value = $event, "15"),
        o: common_vendor.p({
          radius: 20,
          placeholder: "请输入关键词",
          cancelButton: "none",
          modelValue: searchModel.value
        })
      } : common_vendor.e({
        p: __props.tools === "title" && __props.titleAlign === "center" && __props.title.length
      }, __props.tools === "title" && __props.titleAlign === "center" && __props.title.length ? {
        q: common_vendor.t(__props.title),
        r: themeColor.value
      } : {}, {
        s: common_vendor.o(onClickTitle, "f6")
      }), {
        t: themeColor.value,
        v: navbarHeight.value,
        w: __props.backgroundColor,
        x: __props.fixed ? 1 : "",
        y: __props.shadow ? 1 : "",
        z: __props.border ? 1 : "",
        A: __props.placeholder
      }, __props.placeholder ? common_vendor.e({
        B: __props.statusBar
      }, __props.statusBar ? {} : {}, {
        C: navbarHeight.value
      }) : {}, {
        D: __props.dark ? 1 : ""
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-305eaf98"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-navbar/su-navbar.js.map
