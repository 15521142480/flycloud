"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_api_trade_afterSale = require("../../../sheep/api/trade/afterSale.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
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
  __name: "list",
  setup(__props) {
    const state = common_vendor.reactive({
      currentTab: 0,
      showApply: false,
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      loadStatus: ""
    });
    const tabMaps = [
      {
        name: "全部",
        value: []
      },
      {
        name: "申请中",
        value: [10]
      },
      {
        name: "处理中",
        value: [20, 30, 40]
      },
      {
        name: "已完成",
        value: [50]
      },
      {
        name: "已拒绝",
        value: [61, 62, 63]
      }
    ];
    function onTabsChange(e) {
      sheep_helper_utils.resetPagination(state.pagination);
      state.currentTab = e.index;
      getOrderList();
    }
    async function getOrderList() {
      state.loadStatus = "loading";
      let { data, code } = await sheep_api_trade_afterSale.AfterSaleApi.getAfterSalePage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        statuses: tabMaps[state.currentTab].value.join(",")
      });
      if (code !== 0) {
        return;
      }
      data.list.forEach((order) => sheep_hooks_useGoods.handleAfterSaleButtons(order));
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function onApply(orderId) {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要取消此申请吗？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_afterSale.AfterSaleApi.cancelAfterSale(orderId);
          if (code === 0) {
            sheep_helper_utils.resetPagination(state.pagination);
            await getOrderList();
          }
        }
      });
    }
    common_vendor.onLoad(async (options) => {
      if (options.type) {
        state.currentTab = options.type;
      }
      await getOrderList();
    });
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getOrderList();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(onTabsChange, "10"),
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
          icon: "/static/data-empty.png",
          text: "暂无数据"
        })
      } : {}, {
        f: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        g: common_vendor.f(state.pagination.list, (order, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(order.no),
            b: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatAfterSaleStatus)(order)),
            c: "c5e81116-4-" + i0 + ",c5e81116-0",
            d: common_vendor.p({
              img: order.picUrl,
              title: order.spuName,
              skuText: order.properties.map((property) => property.valueName).join(" "),
              price: order.refundPrice
            }),
            e: common_vendor.t(order.way === 10 ? "仅退款" : "退款退货"),
            f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatAfterSaleStatusDescription)(order)),
            g: order == null ? void 0 : order.buttons.includes("cancel")
          }, (order == null ? void 0 : order.buttons.includes("cancel")) ? {
            h: common_vendor.o(($event) => onApply(order.id), order.id)
          } : {}, {
            i: order.id,
            j: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/aftersale/detail", {
              id: order.id
            }), order.id)
          });
        })
      } : {}, {
        h: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        i: common_vendor.o(loadMore, "3b"),
        j: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        k: common_vendor.p({
          title: "售后列表"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c5e81116"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/aftersale/list.js.map
