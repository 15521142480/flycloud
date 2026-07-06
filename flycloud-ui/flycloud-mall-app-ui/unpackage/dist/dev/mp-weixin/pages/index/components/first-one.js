"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
if (!Array) {
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  _easycom_s_goods_column2();
}
const _easycom_s_goods_column = () => "../../../sheep/components/s-goods-column/s-goods-column.js";
if (!Math) {
  _easycom_s_goods_column();
}
const _sfc_main = {
  __name: "first-one",
  props: {
    pagination: Object
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(__props.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "f8bc43e1-0-" + i0,
            c: common_vendor.p({
              size: "sl",
              data: item
            }),
            d: item.id
          };
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-f8bc43e1"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/index/components/first-one.js.map
