"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_uni_badge2 = common_vendor.resolveComponent("uni-badge");
  _easycom_uni_badge2();
}
const _easycom_uni_badge = () => "../../../uni_modules/uni-badge/components/uni-badge/uni-badge.js";
if (!Math) {
  _easycom_uni_badge();
}
const _sfc_main = {
  __name: "s-order-card",
  props: {
    // 装修数据
    data: {
      type: Object,
      default: () => ({})
    },
    // 装修样式
    styles: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props) {
    const orderMap = [
      {
        title: "待付款",
        value: "1",
        icon: "/static/img/shop/order/no_pay.png",
        path: "/pages/order/list",
        type: "unpaid",
        count: "unpaidCount"
      },
      {
        title: "待收货",
        value: "3",
        icon: "/static/img/shop/order/no_take.png",
        path: "/pages/order/list",
        type: "noget",
        count: "deliveredCount"
      },
      {
        title: "待评价",
        value: "4",
        icon: "/static/img/shop/order/no_comment.png",
        path: "/pages/order/list",
        type: "nocomment",
        count: "uncommentedCount"
      },
      {
        title: "售后单",
        value: "0",
        icon: "/static/img/shop/order/change_order.png",
        path: "/pages/order/aftersale/list",
        type: "aftersale",
        count: "afterSaleCount"
      },
      {
        title: "全部订单",
        value: "0",
        icon: "/static/img/shop/order/all_order.png",
        path: "/pages/order/list"
      }
    ];
    const props = __props;
    const numData = common_vendor.computed(() => sheep_index.sheep.$store("user").numData);
    const style = common_vendor.computed(() => {
      const { bgType, bgImg, bgColor } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(orderMap, (item, k0, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.static(item.icon),
            b: "85ba17ee-0-" + i0,
            c: common_vendor.p({
              text: numData.value.orderCount[item.count],
              absolute: "rightTop",
              size: "small"
            }),
            d: common_vendor.t(item.title),
            e: item.title,
            f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.path, {
              type: item.value
            }), item.title)
          };
        }),
        b: common_vendor.s(style.value),
        c: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-85ba17ee"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-order-card/s-order-card.js.map
