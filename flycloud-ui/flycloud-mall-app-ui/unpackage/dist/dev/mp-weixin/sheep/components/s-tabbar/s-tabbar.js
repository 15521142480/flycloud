"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_su_tabbar_item2 = common_vendor.resolveComponent("su-tabbar-item");
  _easycom_su_tabbar_item2();
}
const _easycom_su_tabbar_item = () => "../../ui/su-tabbar-item/su-tabbar-item.js";
if (!Math) {
  (_easycom_su_tabbar_item + SuTabbar)();
}
const SuTabbar = () => "../../ui/su-tabbar/su-tabbar.js";
const _sfc_main = {
  __name: "s-tabbar",
  props: {
    path: String,
    default: ""
  },
  setup(__props) {
    const tabbar = common_vendor.computed(() => {
      var _a;
      return (_a = sheep_index.sheep.$store("app").template.basic) == null ? void 0 : _a.tabbar;
    });
    const tabbarStyle = common_vendor.computed(() => {
      const backgroundStyle = tabbar.value.style;
      if (backgroundStyle.bgType === "color") {
        return { background: backgroundStyle.bgColor };
      }
      if (backgroundStyle.bgType === "img")
        return {
          background: `url(${sheep_index.sheep.$url.cdn(
            backgroundStyle.bgImg
          )}) no-repeat top center / 100% auto`
        };
    });
    const getTabbarCenter = (index) => {
      if (common_vendor.unref(tabbar).mode !== 2)
        return false;
      return common_vendor.unref(tabbar).items % 2 > 0 ? Math.ceil(common_vendor.unref(tabbar).items.length / 2) === index + 1 : false;
    };
    return (_ctx, _cache) => {
      var _a, _b, _c, _d;
      return common_vendor.e({
        a: ((_b = (_a = tabbar.value) == null ? void 0 : _a.items) == null ? void 0 : _b.length) > 0
      }, ((_d = (_c = tabbar.value) == null ? void 0 : _c.items) == null ? void 0 : _d.length) > 0 ? {
        b: common_vendor.f(tabbar.value.items, (item, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.activeIconUrl),
            b: common_vendor.unref(sheep_index.sheep).$url.cdn(item.iconUrl),
            c: item.text,
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.url), item.text),
            e: "78cc87eb-1-" + i0 + ",78cc87eb-0",
            f: common_vendor.p({
              text: item.text,
              name: item.url,
              badge: item.badge,
              dot: item.dot,
              badgeStyle: tabbar.value.badgeStyle,
              isCenter: getTabbarCenter(index),
              centerImage: common_vendor.unref(sheep_index.sheep).$url.cdn(item.iconUrl)
            })
          };
        }),
        c: common_vendor.p({
          value: __props.path,
          fixed: true,
          placeholder: true,
          safeAreaInsetBottom: true,
          inactiveColor: tabbar.value.style.color,
          activeColor: tabbar.value.style.activeColor,
          midTabBar: tabbar.value.mode === 2,
          customStyle: tabbarStyle.value
        })
      } : {});
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-tabbar/s-tabbar.js.map
