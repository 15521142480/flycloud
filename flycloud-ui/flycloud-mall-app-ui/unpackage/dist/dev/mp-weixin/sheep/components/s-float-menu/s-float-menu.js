"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_uni_fab2 = common_vendor.resolveComponent("uni-fab");
  _easycom_uni_fab2();
}
const _easycom_uni_fab = () => "../../../uni_modules/uni-fab/components/uni-fab/uni-fab.js";
if (!Math) {
  _easycom_uni_fab();
}
const _sfc_main = {
  __name: "s-float-menu",
  props: {
    data: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    var _a;
    const props = __props;
    const state = common_vendor.reactive({
      // 可选样式配置项
      pattern: [],
      // 展开菜单内容配置项
      content: [],
      // 展开菜单显示方式：horizontal-水平显示，vertical-垂直显示
      direction: ""
    });
    const fabRef = common_vendor.ref(null);
    state.direction = props.data.direction;
    (_a = props.data) == null ? void 0 : _a.list.forEach((item) => {
      var _a2;
      const text = ((_a2 = props.data) == null ? void 0 : _a2.showText) ? item.text : "";
      state.content.push({ iconPath: sheep_index.sheep.$url.cdn(item.imgUrl), url: item.url, text });
      state.pattern.push({ color: item.textColor });
    });
    function handleOpenLink(e) {
      sheep_index.sheep.$router.go(e.item.url);
    }
    function handleCollapseFab() {
      var _a2, _b;
      if ((_a2 = common_vendor.unref(fabRef)) == null ? void 0 : _a2.isShow) {
        (_b = common_vendor.unref(fabRef)) == null ? void 0 : _b.close();
      }
    }
    common_vendor.onBackPress(() => {
      var _a2, _b;
      if ((_a2 = common_vendor.unref(fabRef)) == null ? void 0 : _a2.isShow) {
        (_b = common_vendor.unref(fabRef)) == null ? void 0 : _b.close();
        return true;
      }
      return false;
    });
    return (_ctx, _cache) => {
      var _a2, _b;
      return common_vendor.e({
        a: (_a2 = fabRef.value) == null ? void 0 : _a2.isShow
      }, ((_b = fabRef.value) == null ? void 0 : _b.isShow) ? {
        b: common_vendor.o(handleCollapseFab, "92")
      } : {}, {
        c: common_vendor.sr(fabRef, "4cc25f94-0", {
          "k": "fabRef"
        }),
        d: common_vendor.o(handleOpenLink, "27"),
        e: common_vendor.p({
          horizontal: "right",
          vertical: "bottom",
          direction: state.direction,
          pattern: state.pattern,
          content: state.content
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-4cc25f94"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-float-menu/s-float-menu.js.map
