"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
const sheep_api_promotion_coupon = require("../../sheep/api/promotion/coupon.js");
if (!Array) {
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_coupon_list2 = common_vendor.resolveComponent("s-coupon-list");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_s_coupon_list2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_tabs = () => "../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_s_coupon_list = () => "../../sheep/components/s-coupon-list/s-coupon-list.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_s_coupon_list + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "list",
  setup(__props) {
    const state = common_vendor.reactive({
      currentTab: 0,
      // 当前 tab
      type: "1",
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 5
      },
      loadStatus: ""
    });
    const tabMaps = [
      {
        name: "领券中心",
        value: "all"
      },
      {
        name: "已领取",
        value: "1"
      },
      {
        name: "已使用",
        value: "2"
      },
      {
        name: "已失效",
        value: "3"
      }
    ];
    function onTabsChange(e) {
      state.currentTab = e.index;
      state.type = e.value;
      sheep_helper_utils.resetPagination(state.pagination);
      if (state.currentTab === 0) {
        getData();
      } else {
        getCoupon();
      }
    }
    async function getData() {
      state.loadStatus = "loading";
      const { data, code } = await sheep_api_promotion_coupon.CouponApi.getCouponTemplatePage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    async function getCoupon() {
      state.loadStatus = "loading";
      const { data, code } = await sheep_api_promotion_coupon.CouponApi.getCouponPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        status: state.type
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    async function getBuy(id) {
      const { code } = await sheep_api_promotion_coupon.CouponApi.takeCoupon(id);
      if (code !== 0) {
        return;
      }
      common_vendor.index.showToast({
        title: "领取成功"
      });
      setTimeout(() => {
        sheep_helper_utils.resetPagination(state.pagination);
        getData();
      }, 1e3);
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      if (state.currentTab === 0) {
        getData();
      } else {
        getCoupon();
      }
    }
    common_vendor.onLoad((Option) => {
      if (Option.type === "all" || !Option.type) {
        getData();
      } else {
        Option.type === "geted" ? state.currentTab = 1 : Option.type === "used" ? state.currentTab = 2 : state.currentTab = 3;
        state.type = state.currentTab;
        getCoupon();
      }
    });
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(onTabsChange, "69"),
        b: common_vendor.p({
          list: tabMaps,
          scrollable: false,
          current: state.currentTab
        }),
        c: common_vendor.p({
          bgColor: "#fff"
        }),
        d: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        e: common_vendor.p({
          icon: "/static/coupon-empty.png",
          text: "暂无优惠券"
        })
      } : {}, {
        f: state.currentTab === 0
      }, state.currentTab === 0 ? {
        g: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.canTake ? "立即领取" : "已领取"),
            b: common_vendor.n(!item.canTake ? "border-btn" : ""),
            c: common_vendor.o(($event) => getBuy(item.id), item.id),
            d: !item.canTake,
            e: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/detail", {
              id: item.id
            }), item.id),
            f: "019d569f-4-" + i0 + ",019d569f-0",
            g: common_vendor.p({
              data: item
            }),
            h: item.id
          };
        })
      } : {
        h: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.status === 1 ? "立即使用" : item.status === 2 ? "已使用" : "已过期"),
            b: common_vendor.n(item.status !== 1 ? "disabled-btn" : ""),
            c: item.status !== 1,
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/detail", {
              couponId: item.id
            }), item.id),
            e: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/coupon/detail", {
              couponId: item.id
            }), item.id),
            f: "019d569f-5-" + i0 + ",019d569f-0",
            g: common_vendor.p({
              data: item,
              type: "user"
            }),
            h: item.id
          };
        })
      }, {
        i: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        j: common_vendor.o(loadMore, "1e"),
        k: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        l: common_vendor.p({
          bgStyle: {
            color: "#f2f2f2"
          },
          title: "优惠券"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-019d569f"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/coupon/list.js.map
