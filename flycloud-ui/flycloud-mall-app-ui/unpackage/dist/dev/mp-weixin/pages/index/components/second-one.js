"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const _sfc_main = {
  __name: "second-one",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    activeMenu: [Number, String]
  },
  setup(__props) {
    const props = __props;
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(props.data[__props.activeMenu].name),
        b: common_vendor.f(props.data[__props.activeMenu].children, (item, k0, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.picUrl),
            b: common_vendor.t(item.name),
            c: item.id,
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/list", {
              categoryId: item.id
            }), item.id)
          };
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c7b30c59"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/index/components/second-one.js.map
