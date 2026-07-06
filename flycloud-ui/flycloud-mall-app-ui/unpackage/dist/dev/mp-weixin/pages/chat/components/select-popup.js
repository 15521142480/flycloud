"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_api_trade_order = require("../../../sheep/api/trade/order.js");
const sheep_api_product_history = require("../../../sheep/api/product/history.js");
if (!Array) {
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_uni_load_more2 + _easycom_su_popup2)();
}
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_su_popup = () => "../../../sheep/ui/su-popup/su-popup.js";
if (!Math) {
  (GoodsItem + OrderItem + _easycom_uni_load_more + _easycom_su_popup)();
}
const GoodsItem = () => "./goods.js";
const OrderItem = () => "./order.js";
const _sfc_main = {
  __name: "select-popup",
  props: {
    mode: {
      type: String,
      default: "goods"
    },
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ["select", "close"],
  setup(__props, { emit: __emit }) {
    const emits = __emit;
    const props = __props;
    common_vendor.watch(
      () => props.mode,
      () => {
        state.pagination.data = [];
        if (props.mode) {
          getList(state.pagination.page);
        }
      }
    );
    const state = common_vendor.reactive({
      loadStatus: "",
      pagination: {
        data: [],
        current_page: 1,
        total: 1,
        last_page: 1
      }
    });
    async function getList(page, list_rows = 5) {
      state.loadStatus = "loading";
      const res = props.mode == "goods" ? await sheep_api_product_history.SpuHistoryApi.getBrowseHistoryPage({
        page,
        list_rows
      }) : await sheep_api_trade_order.OrderApi.getOrderPage({
        page,
        list_rows
      });
      let orderList = common_vendor.concat(state.pagination.data, res.data.list);
      state.pagination = {
        ...res.data,
        data: orderList
      };
      if (state.pagination.current_page < state.pagination.last_page) {
        state.loadStatus = "more";
      } else {
        state.loadStatus = "noMore";
      }
    }
    function loadmore() {
      if (state.loadStatus !== "noMore") {
        getList(state.pagination.current_page + 1);
      }
    }
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(__props.mode == "goods" ? "我的浏览" : "我的订单"),
        b: common_vendor.f(state.pagination.data, (item, k0, i0) => {
          return common_vendor.e(__props.mode == "goods" ? {
            a: "878522b0-1-" + i0 + ",878522b0-0",
            b: common_vendor.p({
              goodsData: item
            })
          } : {}, __props.mode == "order" ? {
            c: "878522b0-2-" + i0 + ",878522b0-0",
            d: common_vendor.p({
              orderData: item
            })
          } : {}, {
            e: item.id,
            f: common_vendor.o(($event) => emits("select", {
              type: __props.mode,
              data: item
            }), item.id)
          });
        }),
        c: __props.mode == "goods",
        d: __props.mode == "order",
        e: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        }),
        f: common_vendor.o(loadmore, "f7"),
        g: common_vendor.o(($event) => emits("close"), "ac"),
        h: common_vendor.p({
          show: __props.show,
          showClose: true,
          round: "10",
          backgroundColor: "#eee"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-878522b0"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/select-popup.js.map
