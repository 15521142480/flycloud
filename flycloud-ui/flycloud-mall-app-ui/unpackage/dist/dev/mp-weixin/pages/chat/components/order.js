"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  _easycom_s_goods_item2();
}
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
if (!Math) {
  _easycom_s_goods_item();
}
const _sfc_main = {
  __name: "order",
  props: {
    orderData: {
      type: Object,
      default: {}
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(__props.orderData.no),
        b: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatOrderStatus)(__props.orderData)),
        c: common_vendor.n(common_vendor.unref(sheep_hooks_useGoods.formatOrderColor)(__props.orderData)),
        d: common_vendor.f(__props.orderData.items, (item, k0, i0) => {
          return {
            a: "a458419c-0-" + i0,
            b: common_vendor.p({
              img: item.picUrl,
              title: item.spuName,
              skuText: item.properties.map((property) => property.valueName).join(" "),
              price: item.price,
              num: item.count
            }),
            c: item.id
          };
        }),
        e: common_vendor.t(__props.orderData.productCount),
        f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.orderData.payPrice)),
        g: __props.orderData.id
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-a458419c"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/order.js.map
