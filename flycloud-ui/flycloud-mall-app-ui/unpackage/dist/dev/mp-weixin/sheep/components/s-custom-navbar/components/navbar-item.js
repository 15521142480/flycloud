"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../index.js");
if (!Array) {
  const _easycom_s_search_block2 = common_vendor.resolveComponent("s-search-block");
  _easycom_s_search_block2();
}
const _easycom_s_search_block = () => "../../s-search-block/s-search-block.js";
if (!Math) {
  _easycom_s_search_block();
}
const _sfc_main = {
  __name: "navbar-item",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    width: {
      type: String,
      default: "1px"
    }
  },
  setup(__props) {
    const height = common_vendor.computed(() => sheep_index.sheep.$platform.capsule.height);
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.data.type === "text"
      }, __props.data.type === "text" ? {
        b: common_vendor.t(__props.data.text),
        c: common_vendor.s({
          color: __props.data.textColor,
          width: __props.width
        }),
        d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(__props.data.url), "ad")
      } : {}, {
        e: __props.data.type === "image"
      }, __props.data.type === "image" ? {
        f: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.imgUrl),
        g: common_vendor.s({
          width: __props.width
        }),
        h: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(__props.data.url), "68")
      } : {}, {
        i: __props.data.type === "search"
      }, __props.data.type === "search" ? {
        j: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/index/search"), "4c"),
        k: common_vendor.p({
          placeholder: __props.data.placeholder || "搜索关键字",
          ["placeholder-position"]: __props.data.placeholderPosition,
          radius: __props.data.borderRadius,
          ["el-background"]: __props.data.backgroundColor,
          ["font-color"]: __props.data.textColor,
          height: height.value,
          width: __props.width,
          ["show-scan"]: __props.data.showScan
        }),
        l: common_vendor.s({
          width: __props.width
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c063d35d"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/components/s-custom-navbar/components/navbar-item.js.map
