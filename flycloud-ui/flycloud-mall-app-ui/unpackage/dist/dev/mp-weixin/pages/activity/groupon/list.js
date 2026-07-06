"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_promotion_combination = require("../../../sheep/api/promotion/combination.js");
if (!Array) {
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_column2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_s_goods_column = () => "../../../sheep/components/s-goods-column/s-goods-column.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_column + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "list",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "958cfdc2": common_vendor.unref(headerBg)
    }));
    const { safeAreaInsets, safeArea } = sheep_index.sheep.$platform.device;
    const sysNavBar = sheep_index.sheep.$platform.navbar;
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const pageHeight = (safeArea.height + safeAreaInsets.bottom) * 2 + statusBarHeight - sysNavBar - 350;
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-header.png");
    const state = common_vendor.reactive({
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      loadStatus: "",
      summaryData: {}
    });
    const getSummary = async () => {
      const { data } = await sheep_api_promotion_combination.CombinationApi.getCombinationRecordSummary();
      state.summaryData = data;
    };
    async function getList() {
      state.loadStatus = "loading";
      const { data } = await sheep_api_promotion_combination.CombinationApi.getCombinationActivityPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize
      });
      data.list.forEach((activity) => {
        state.pagination.list.push({ ...activity, price: activity.combinationPrice });
      });
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getList();
    }
    common_vendor.onReachBottom(() => loadMore());
    common_vendor.onLoad(() => {
      getSummary();
      getList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx"
        }),
        b: common_vendor.f(state.summaryData.avatars, (item, index, i0) => {
          return common_vendor.e({
            a: common_vendor.s(`background-image: url(${item})`),
            b: index === 6 && state.summaryData.avatars.length > 3
          }, index === 6 && state.summaryData.avatars.length > 3 ? {} : {}, {
            c: index,
            d: common_vendor.s(index === 6 ? "position: relative" : "position: static")
          });
        }),
        c: common_vendor.t(state.summaryData.userCount || 0),
        d: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/groupon", {
              id: item.id
            }), item.id),
            b: "628ec892-1-" + i0 + ",628ec892-0",
            c: common_vendor.p({
              size: "lg",
              data: item,
              grouponTag: true
            }),
            d: item.id
          };
        }),
        e: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        f: common_vendor.o(loadMore, "dc"),
        g: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        h: pageHeight + "rpx",
        i: common_vendor.s(_ctx.__cssVars()),
        j: common_vendor.p({
          bgStyle: {
            color: "#FE832A"
          },
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-628ec892"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/activity/groupon/list.js.map
