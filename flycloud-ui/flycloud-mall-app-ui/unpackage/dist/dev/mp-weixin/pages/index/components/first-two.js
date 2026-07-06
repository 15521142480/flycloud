"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const _sfc_main = {
  __name: "first-two",
  props: {
    pagination: Object
  },
  setup(__props) {
    return (_ctx, _cache) => {
      var _a;
      return {
        a: common_vendor.f((_a = __props.pagination) == null ? void 0 : _a.list, (item, k0, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.picUrl),
            b: common_vendor.t(item.name),
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price)),
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            e: item.id
          };
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-80c21380"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/index/components/first-two.js.map
