"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_product_favorite = require("../../sheep/api/product/favorite.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_su_fixed2 + _easycom_uni_load_more2 + _easycom_s_empty2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_su_fixed = () => "../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + _easycom_su_fixed + _easycom_uni_load_more + _easycom_s_empty + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "goods-collect",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "a2866b58": common_vendor.unref(sys_navBar)
    }));
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const state = common_vendor.reactive({
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 6
      },
      loadStatus: "",
      editMode: false,
      selectedCollectList: [],
      // 选中的 SPU 数组
      selectAll: false
    });
    async function getData() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_product_favorite.FavoriteApi.getFavoritePage({
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
    const onSelect = (spuId) => {
      if (!state.selectedCollectList.includes(spuId)) {
        state.selectedCollectList.push(spuId);
      } else {
        state.selectedCollectList.splice(state.selectedCollectList.indexOf(spuId), 1);
      }
      state.selectAll = state.selectedCollectList.length === state.pagination.list.length;
    };
    const onSelectAll = () => {
      state.selectAll = !state.selectAll;
      if (!state.selectAll) {
        state.selectedCollectList = [];
      } else {
        state.selectedCollectList = state.pagination.list.map((item) => item.spuId);
      }
    };
    async function onCancel() {
      if (!state.selectedCollectList) {
        return;
      }
      for (const spuId of state.selectedCollectList) {
        await sheep_api_product_favorite.FavoriteApi.deleteFavorite(spuId);
      }
      state.editMode = false;
      state.selectedCollectList = [];
      state.selectAll = false;
      sheep_helper_utils.resetPagination(state.pagination);
      await getData();
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getData();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    common_vendor.onLoad(() => {
      getData();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(state.pagination.total),
        b: state.editMode && state.pagination.total
      }, state.editMode && state.pagination.total ? {
        c: common_vendor.o(($event) => state.editMode = false, "94")
      } : {}, {
        d: !state.editMode && state.pagination.total
      }, !state.editMode && state.pagination.total ? {
        e: common_vendor.o(($event) => state.editMode = true, "22")
      } : {}, {
        f: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e(state.editMode ? {
            a: state.selectedCollectList.includes(item.spuId),
            b: common_vendor.o(($event) => onSelect(item.spuId), item.id),
            c: common_vendor.o(($event) => onSelect(item.spuId), item.id)
          } : {}, {
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.spuId
            }), item.id),
            e: "28ee79e9-1-" + i0 + ",28ee79e9-0",
            f: common_vendor.p({
              title: item.spuName,
              img: item.picUrl,
              price: item.price,
              priceColor: "#FF3000",
              titleWidth: 400
            }),
            g: item.id
          });
        }),
        g: state.editMode,
        h: state.selectAll,
        i: common_vendor.o(onSelectAll, "d1"),
        j: common_vendor.o(onSelectAll, "d5"),
        k: common_vendor.o(onCancel, "51"),
        l: state.editMode,
        m: common_vendor.p({
          bottom: true,
          val: 0,
          placeholder: true
        }),
        n: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        o: common_vendor.o(loadMore, "3b"),
        p: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        q: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        r: common_vendor.p({
          text: "暂无收藏",
          icon: "/static/collect-empty.png"
        })
      } : {}, {
        s: common_vendor.s(_ctx.__cssVars()),
        t: common_vendor.p({
          title: "商品收藏"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-28ee79e9"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/user/goods-collect.js.map
