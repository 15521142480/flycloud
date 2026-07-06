"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_s_goods_item2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_tabs = () => "../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_s_goods_item = () => "../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_s_goods_item + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "list",
  setup(__props) {
    const state = common_vendor.reactive({
      currentTab: 0,
      // 选中的 tabMaps 下标
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
        name: "全部"
      },
      {
        name: "待付款",
        value: 0
      },
      {
        name: "待发货",
        value: 10
      },
      {
        name: "待收货",
        value: 20
      },
      {
        name: "待评价",
        value: 30
      }
    ];
    function onTabsChange(e) {
      if (state.currentTab === e.index) {
        return;
      }
      sheep_helper_utils.resetPagination(state.pagination);
      state.currentTab = e.index;
      getOrderList();
    }
    function onOrderDetail(id) {
      sheep_index.sheep.$router.go("/pages/order/detail", {
        id
      });
    }
    function onOrderGroupon(order) {
      sheep_index.sheep.$router.go("/pages/activity/groupon/detail", {
        id: order.combinationRecordId
      });
    }
    function onPay(payOrderId) {
      sheep_index.sheep.$router.go("/pages/pay/index", {
        id: payOrderId
      });
    }
    function onComment(id) {
      sheep_index.sheep.$router.go("/pages/goods/comment/add", {
        id
      });
    }
    async function onConfirm(order, ignore = false) {
      let isOpenBusinessView = true;
      if (sheep_index.sheep.$platform.name === "WechatMiniProgram" && !common_vendor.isEmpty(order.wechat_extra_data) && isOpenBusinessView && !ignore) {
        mpConfirm(order);
        return;
      }
      common_vendor.index.showModal({
        title: "提示",
        content: "确认收货吗？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_order.OrderApi.receiveOrder(order.id);
          if (code === 0) {
            sheep_helper_utils.resetPagination(state.pagination);
            await getOrderList();
          }
        }
      });
    }
    function mpConfirm(order) {
      if (!common_vendor.wx$1.openBusinessView) {
        sheep_index.sheep.$helper.toast(`请升级微信版本`);
        return;
      }
      common_vendor.wx$1.openBusinessView({
        businessType: "weappOrderConfirm",
        extraData: {
          merchant_id: "1481069012",
          merchant_trade_no: order.wechat_extra_data.merchant_trade_no,
          transaction_id: order.wechat_extra_data.transaction_id
        },
        success(response) {
          common_vendor.index.__f__("log", "at pages/order/list.vue:257", "success:", response);
          if (response.errMsg === "openBusinessView:ok") {
            if (response.extraData.status === "success") {
              onConfirm(order, true);
            }
          }
        },
        fail(error) {
          common_vendor.index.__f__("log", "at pages/order/list.vue:265", "error:", error);
        },
        complete(result) {
          common_vendor.index.__f__("log", "at pages/order/list.vue:268", "result:", result);
        }
      });
    }
    async function onExpress(id) {
      sheep_index.sheep.$router.go("/pages/order/express/log", {
        id
      });
    }
    async function onCancel(orderId) {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要取消订单吗?",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_order.OrderApi.cancelOrder(orderId);
          if (code === 0) {
            let index = state.pagination.list.findIndex((order) => order.id === orderId);
            const orderInfo = state.pagination.list[index];
            orderInfo.status = 40;
            sheep_hooks_useGoods.handleOrderButtons(orderInfo);
          }
        }
      });
    }
    function onDelete(orderId) {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要删除订单吗?",
        success: async function(res) {
          if (res.confirm) {
            const { code } = await sheep_api_trade_order.OrderApi.deleteOrder(orderId);
            if (code === 0) {
              let index = state.pagination.list.findIndex((order) => order.id === orderId);
              state.pagination.list.splice(index, 1);
            }
          }
        }
      });
    }
    async function getOrderList() {
      state.loadStatus = "loading";
      let { code, data } = await sheep_api_trade_order.OrderApi.getOrderPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        status: tabMaps[state.currentTab].value,
        commentStatus: tabMaps[state.currentTab].value === 30 ? false : null
      });
      if (code !== 0) {
        return;
      }
      data.list.forEach((order) => sheep_hooks_useGoods.handleOrderButtons(order));
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
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
    common_vendor.onPullDownRefresh(() => {
      sheep_helper_utils.resetPagination(state.pagination);
      getOrderList();
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
          icon: "/static/order-empty.png",
          text: "暂无订单"
        })
      } : {}, {
        f: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        g: common_vendor.f(state.pagination.list, (order, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(order.no),
            b: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatOrderStatus)(order)),
            c: common_vendor.n(common_vendor.unref(sheep_hooks_useGoods.formatOrderColor)(order)),
            d: common_vendor.f(order.items, (item, k1, i1) => {
              return {
                a: "456ecf67-4-" + i0 + "-" + i1 + ",456ecf67-0",
                b: common_vendor.p({
                  img: item.picUrl,
                  title: item.spuName,
                  skuText: item.properties.map((property) => property.valueName).join(" "),
                  price: item.price,
                  num: item.count
                }),
                c: item.id
              };
            }),
            e: common_vendor.t(order.productCount),
            f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(order.payPrice)),
            g: order.buttons.includes("combination")
          }, order.buttons.includes("combination") ? {
            h: common_vendor.o(($event) => onOrderGroupon(order), order.id)
          } : {}, {
            i: order.buttons.length === 0
          }, order.buttons.length === 0 ? {
            j: common_vendor.o(($event) => onOrderDetail(order.id), order.id)
          } : {}, {
            k: order.buttons.includes("confirm")
          }, order.buttons.includes("confirm") ? {
            l: common_vendor.o(($event) => onConfirm(order), order.id)
          } : {}, {
            m: order.buttons.includes("express")
          }, order.buttons.includes("express") ? {
            n: common_vendor.o(($event) => onExpress(order.id), order.id)
          } : {}, {
            o: order.buttons.includes("cancel")
          }, order.buttons.includes("cancel") ? {
            p: common_vendor.o(($event) => onCancel(order.id), order.id)
          } : {}, {
            q: order.buttons.includes("comment")
          }, order.buttons.includes("comment") ? {
            r: common_vendor.o(($event) => onComment(order.id), order.id)
          } : {}, {
            s: order.buttons.includes("delete")
          }, order.buttons.includes("delete") ? {
            t: common_vendor.o(($event) => onDelete(order.id), order.id)
          } : {}, {
            v: order.buttons.includes("pay")
          }, order.buttons.includes("pay") ? {
            w: common_vendor.o(($event) => onPay(order.payOrderId), order.id)
          } : {}, {
            x: common_vendor.n(order.buttons.length > 3 ? "ss-row-between" : "ss-row-right"),
            y: order.id,
            z: common_vendor.o(($event) => onOrderDetail(order.id), order.id)
          });
        })
      } : {}, {
        h: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        i: common_vendor.o(loadMore, "40"),
        j: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        k: common_vendor.p({
          title: "我的订单"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-456ecf67"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/list.js.map
