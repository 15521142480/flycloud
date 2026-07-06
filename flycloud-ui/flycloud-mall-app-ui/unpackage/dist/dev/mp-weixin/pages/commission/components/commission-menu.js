"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const _sfc_main = {
  __name: "commission-menu",
  setup(__props) {
    const state = common_vendor.reactive({
      menuList: [
        {
          img: "/static/img/shop/commission/commission_icon1.png",
          title: "我的团队",
          path: "/pages/commission/team"
        },
        {
          img: "/static/img/shop/commission/commission_icon2.png",
          title: "佣金明细",
          path: "/pages/commission/wallet"
        },
        {
          img: "/static/img/shop/commission/commission_icon3.png",
          title: "分销订单",
          path: "/pages/commission/order"
        },
        {
          img: "/static/img/shop/commission/commission_icon4.png",
          title: "推广商品",
          path: "/pages/commission/goods"
        },
        // {
        //   img: '/static/img/shop/commission/commission_icon5.png',
        //   title: '我的资料',
        //   path: '/pages/commission/apply',
        //   isAgentFrom: true,
        // },
        {
          img: "/static/img/shop/commission/commission_icon7.png",
          title: "邀请海报",
          path: "action:showShareModal"
        },
        {
          img: "/static/img/shop/commission/commission_icon8.png",
          title: "推广排行",
          path: "/pages/commission/promoter"
        },
        {
          img: "/static/img/shop/commission/commission_icon9.png",
          title: "佣金排行",
          path: "/pages/commission/commission-ranking"
        }
      ]
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/commission/title1.png"),
        b: common_vendor.f(state.menuList, (item, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.static(item.img),
            b: common_vendor.t(item.title),
            c: index,
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(item.path), index)
          };
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-a53c0bfa"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/commission/components/commission-menu.js.map
