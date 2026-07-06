"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
const sheep_api_promotion_combination = require("../../../sheep/api/promotion/combination.js");
if (!Array) {
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_s_goods_item2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_tabs = () => "../../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_s_goods_item + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "order",
  setup(__props) {
    const state = common_vendor.reactive({
      currentTab: 0,
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 5
      },
      loadStatus: "",
      deleteOrderId: 0
    });
    const tabMaps = [
      {
        name: "全部"
      },
      {
        name: "进行中",
        value: 0
      },
      {
        name: "拼团成功",
        value: 1
      },
      {
        name: "拼团失败",
        value: 2
      }
    ];
    function onTabsChange(e) {
      sheep_helper_utils.resetPagination(state.pagination);
      state.currentTab = e.index;
      getGrouponList();
    }
    async function getGrouponList() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_promotion_combination.CombinationApi.getCombinationRecordPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        status: tabMaps[state.currentTab].value
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    common_vendor.onLoad((options) => {
      if (options.type) {
        state.currentTab = options.type;
      }
      getGrouponList();
    });
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getGrouponList();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    common_vendor.onPullDownRefresh(() => {
      sheep_helper_utils.resetPagination(state.pagination);
      getGrouponList();
      setTimeout(function() {
        common_vendor.index.stopPullDownRefresh();
      }, 800);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(onTabsChange, "f8"),
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
          icon: "/static/goods-empty.png"
        })
      } : {}, {
        f: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        g: common_vendor.f(state.pagination.list, (record, k0, i0) => {
          return {
            a: common_vendor.t(record.id),
            b: common_vendor.t(tabMaps.find((item) => item.value === record.status).name),
            c: common_vendor.n(common_vendor.unref(sheep_hooks_useGoods.formatOrderColor)(record)),
            d: common_vendor.t(record.userSize),
            e: "5be50f68-4-" + i0 + ",5be50f68-0",
            f: common_vendor.p({
              img: record.picUrl,
              title: record.spuName,
              price: record.combinationPrice
            }),
            g: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/detail", {
              id: record.orderId
            }), record.id),
            h: common_vendor.t(record.status === 0 ? "邀请拼团" : "拼团详情"),
            i: record.status === 0 ? 1 : "",
            j: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/activity/groupon/detail", {
              id: record.id
            }), record.id),
            k: record.id
          };
        })
      } : {}, {
        h: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        i: common_vendor.o(loadMore, "5d"),
        j: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        k: common_vendor.p({
          title: "我的拼团"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5be50f68"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/activity/groupon/order.js.map
