"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Array) {
  const _easycom_su_status_bar2 = common_vendor.resolveComponent("su-status-bar");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_su_status_bar2 + _easycom_su_popup2)();
}
const _easycom_su_status_bar = () => "../../ui/su-status-bar/su-status-bar.js";
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  (_easycom_su_status_bar + _easycom_su_popup)();
}
const _sfc_main = {
  __name: "s-menu-tools",
  setup(__props) {
    const show = common_vendor.computed(() => sheep_index.sheep.$store("modal").menu);
    function onClick(item) {
      sheep_hooks_useModal.closeMenuTools();
      if (item.url)
        sheep_index.sheep.$router.go(item.url);
    }
    const list = [
      {
        url: "/pages/index/index",
        icon: "/static/img/shop/tools/home.png",
        title: "首页"
      },
      {
        url: "/pages/index/search",
        icon: "/static/img/shop/tools/search.png",
        title: "搜索"
      },
      {
        url: "/pages/index/user",
        icon: "/static/img/shop/tools/user.png",
        title: "个人中心"
      },
      {
        url: "/pages/index/cart",
        icon: "/static/img/shop/tools/cart.png",
        title: "购物车"
      },
      {
        url: "/pages/user/goods-log",
        icon: "/static/img/shop/tools/browse.png",
        title: "浏览记录"
      },
      {
        url: "/pages/user/goods-collect",
        icon: "/static/img/shop/tools/collect.png",
        title: "我的收藏"
      },
      {
        url: "/pages/chat/index",
        icon: "/static/img/shop/tools/service.png",
        title: "客服"
      }
    ];
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(list, (item, k0, i0) => {
          return common_vendor.e(show.value ? {
            a: common_vendor.unref(sheep_index.sheep).$url.static(item.icon)
          } : {}, {
            b: common_vendor.o(($event) => onClick(item), item.title),
            c: common_vendor.t(item.title),
            d: item.title
          });
        }),
        b: show.value,
        c: common_vendor.o(common_vendor.unref(sheep_hooks_useModal.closeMenuTools), "86"),
        d: common_vendor.p({
          show: show.value,
          type: "top",
          round: "20",
          backgroundColor: "#F0F0F0"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-bbb37e35"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-menu-tools/s-menu-tools.js.map
