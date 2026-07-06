"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
const _sfc_main = {
  __name: "s-wallet-card",
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
    const props = __props;
    const bgStyle = common_vendor.computed(() => {
      const { bgType, bgImg, bgColor } = props.styles;
      return {
        background: bgType === "img" ? `url(${bgImg}) no-repeat top center / 100% 100%` : bgColor
      };
    });
    const userWallet = common_vendor.computed(() => sheep_index.sheep.$store("user").userWallet);
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const numData = common_vendor.computed(() => sheep_index.sheep.$store("user").numData);
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(userWallet.value.balance) || "0.00"),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/wallet/money"), "f8"),
        c: common_vendor.t(userInfo.value.point || 0),
        d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/wallet/score"), "d1"),
        e: common_vendor.t(numData.value.unusedCouponCount),
        f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/list", {
          type: "geted"
        }), "dd"),
        g: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/user/wallet_icon.png"),
        h: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/user/wallet/money"), "5f"),
        i: common_vendor.s(bgStyle.value),
        j: common_vendor.s({
          marginLeft: `${__props.data.space}px`
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0aa1f5b2"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-wallet-card/s-wallet-card.js.map
