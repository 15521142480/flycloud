"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-coupon-card",
  props: {
    list: {
      type: Array,
      default() {
        return [
          {
            title: "已领取",
            value: "0",
            icon: "/static/img/shop/order/nouse_coupon.png",
            path: "/pages/coupon/list",
            type: "geted"
          },
          {
            title: "已使用",
            value: "0",
            icon: "/static/img/shop/order/useend_coupon.png",
            path: "/pages/coupon/list",
            type: "used"
          },
          {
            title: "已失效",
            value: "0",
            icon: "/static/img/shop/order/out_coupon.png",
            path: "/pages/coupon/list",
            type: "expired"
          },
          {
            title: "领券中心",
            value: "0",
            icon: "/static/img/shop/order/all_coupon.png",
            path: "/pages/coupon/list",
            type: "all"
          }
        ];
      }
    },
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
    const props = __props;
    const bgStyle = common_vendor.computed(() => {
      const { bgType, bgImg, bgColor } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(props.list, (item, k0, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.static(item.icon),
            b: common_vendor.t(item.title),
            c: item.title,
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.path, {
              type: item.type
            }), item.title),
            e: common_vendor.n(item.type === "all" ? "menu-wallet" : "ss-flex-1")
          };
        }),
        b: common_vendor.s(bgStyle.value),
        c: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-245dca84"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-coupon-card/s-coupon-card.js.map
