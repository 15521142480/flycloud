"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_promotion_rewardActivity = require("../../sheep/api/promotion/rewardActivity.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
if (!Array) {
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_sticky2 + _easycom_s_goods_column2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_goods_column = () => "../../sheep/components/s-goods-column/s-goods-column.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_sticky + _easycom_s_goods_column + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const state = common_vendor.reactive({
      activityId: 0,
      // 获得编号
      activityInfo: {},
      // 获得信息
      pagination: {
        list: [],
        total: 1,
        pageNum: 1,
        pageSize: 8
      },
      loadStatus: "",
      leftGoodsList: [],
      rightGoodsList: []
    });
    let count = 0;
    let leftHeight = 0;
    let rightHeight = 0;
    function mountMasonry(height = 0, where = "left") {
      if (!state.pagination.list[count])
        return;
      if (where === "left") {
        leftHeight += height;
      } else {
        rightHeight += height;
      }
      if (leftHeight <= rightHeight) {
        state.leftGoodsList.push(state.pagination.list[count]);
      } else {
        state.rightGoodsList.push(state.pagination.list[count]);
      }
      count++;
    }
    async function getList() {
      const params = {};
      if (state.activityInfo.productScope === 2) {
        params.ids = state.activityInfo.productSpuIds.join(",");
      } else if (state.activityInfo.productScope === 3) {
        params.categoryIds = state.activityInfo.productSpuIds.join(",");
      }
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_product_spu.SpuApi.getSpuPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        ...params
      });
      if (code !== 0) {
        return;
      }
      await sheep_api_trade_order.OrderApi.getSettlementProduct(data.list.map((item) => item.id).join(",")).then((res) => {
        if (res.code !== 0) {
          return;
        }
        sheep_hooks_useGoods.appendSettlementProduct(data.list, res.data);
      });
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
      mountMasonry();
    }
    async function getActivity(id) {
      const { code, data } = await sheep_api_promotion_rewardActivity.RewardActivityApi.getRewardActivity(id);
      if (code === 0) {
        state.activityInfo = data;
      }
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getList();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    common_vendor.onLoad(async (options) => {
      state.activityId = options.activityId;
      await getActivity(state.activityId);
      await getList(state.activityId);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(state.activityInfo.rules, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.description),
            b: item
          };
        }),
        b: common_assets._imports_0,
        c: common_assets._imports_1,
        d: common_vendor.p({
          bgColor: "#fff"
        }),
        e: common_vendor.f(state.leftGoodsList, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: common_vendor.o(($event) => mountMasonry($event, "left"), item.id),
            c: "2c61ebca-2-" + i0 + ",2c61ebca-0",
            d: common_vendor.p({
              size: "md",
              data: item
            }),
            e: item.id
          };
        }),
        f: common_vendor.f(state.rightGoodsList, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: common_vendor.o(($event) => mountMasonry($event, "right"), item.id),
            c: "2c61ebca-3-" + i0 + ",2c61ebca-0",
            d: common_vendor.p({
              size: "md",
              data: item
            }),
            e: item.id
          };
        }),
        g: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        h: common_vendor.o(loadMore, "a5"),
        i: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        j: common_vendor.p({
          title: state.activityInfo.title
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2c61ebca"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/activity/index.js.map
