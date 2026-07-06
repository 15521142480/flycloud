"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_product_history = require("../../sheep/api/product/history.js");
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
  __name: "goods-log",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "1ab964d1": common_vendor.unref(sys_navBar)
    }));
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const pagination = {
      list: [],
      pageNum: 1,
      total: 1,
      pageSize: 10
    };
    const state = common_vendor.reactive({
      pagination: sheep_helper_utils.cloneDeep(pagination),
      loadStatus: "",
      editMode: false,
      selectedSpuIdList: [],
      selectAll: false
    });
    async function getList() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_product_history.SpuHistoryApi.getBrowseHistoryPage({
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
    const onSelect = (id) => {
      if (!state.selectedSpuIdList.includes(id)) {
        state.selectedSpuIdList.push(id);
      } else {
        state.selectedSpuIdList.splice(state.selectedSpuIdList.indexOf(id), 1);
      }
      state.selectAll = state.selectedSpuIdList.length === state.pagination.list.length;
    };
    const onSelectAll = () => {
      state.selectAll = !state.selectAll;
      if (!state.selectAll) {
        state.selectedSpuIdList = [];
      } else {
        state.pagination.list.forEach((item) => {
          if (state.selectedSpuIdList.includes(item.spuId)) {
            state.selectedSpuIdList.splice(state.selectedSpuIdList.indexOf(item.spuId), 1);
          }
          state.selectedSpuIdList.push(item.spuId);
        });
      }
    };
    async function onDelete() {
      if (state.selectedSpuIdList.length <= 0) {
        return;
      }
      const { code } = await sheep_api_product_history.SpuHistoryApi.deleteBrowseHistory(state.selectedSpuIdList);
      if (code === 0) {
        reload();
      }
    }
    async function onClean() {
      const { code } = await sheep_api_product_history.SpuHistoryApi.cleanBrowseHistory();
      if (code === 0) {
        reload();
      }
    }
    function reload() {
      state.editMode = false;
      state.selectedSpuIdList = [];
      state.selectAll = false;
      state.pagination = pagination;
      getList();
    }
    function loadMore() {
      if (state.loadStatus !== "noMore") {
        state.pagination.pageNum += 1;
        getList();
      }
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    common_vendor.onLoad(() => {
      getList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(state.pagination.total),
        b: state.editMode && state.pagination.total
      }, state.editMode && state.pagination.total ? {
        c: common_vendor.o(($event) => state.editMode = false, "11")
      } : {}, {
        d: !state.editMode && state.pagination.total
      }, !state.editMode && state.pagination.total ? {
        e: common_vendor.o(($event) => state.editMode = true, "79")
      } : {}, {
        f: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e(state.editMode ? {
            a: state.selectedSpuIdList.includes(item.spuId),
            b: common_vendor.o(($event) => onSelect(item.spuId), item.id),
            c: common_vendor.o(($event) => onSelect(item.spuId), item.id)
          } : {}, {
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.spuId
            }), item.id),
            e: "e113b195-1-" + i0 + ",e113b195-0",
            f: common_vendor.p({
              title: item.spuName,
              img: item.picUrl,
              price: item.price,
              skuText: item.introduction,
              priceColor: "#FF3000",
              titleWidth: 400
            }),
            g: item.id
          });
        }),
        g: state.editMode,
        h: state.selectAll,
        i: common_vendor.o(onSelectAll, "75"),
        j: common_vendor.o(onSelectAll, "75"),
        k: common_vendor.n({
          "ui-BG-Main-Gradient": state.selectedSpuIdList.length > 0,
          "ui-Shadow-Main": state.selectedSpuIdList.length > 0
        }),
        l: common_vendor.o(onDelete, "2f"),
        m: common_vendor.o(onClean, "82"),
        n: state.editMode,
        o: common_vendor.p({
          bottom: true,
          val: 0,
          placeholder: true
        }),
        p: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        q: common_vendor.o(loadMore, "62"),
        r: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        s: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        t: common_vendor.p({
          text: "暂无浏览记录",
          icon: "/static/collect-empty.png"
        })
      } : {}, {
        v: common_vendor.s(_ctx.__cssVars()),
        w: common_vendor.p({
          bgStyle: {
            color: "#f2f2f2"
          },
          title: "我的足迹"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e113b195"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/user/goods-log.js.map
