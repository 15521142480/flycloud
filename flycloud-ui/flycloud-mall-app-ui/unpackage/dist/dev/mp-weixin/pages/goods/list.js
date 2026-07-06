"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_su_popup2 + _easycom_s_goods_column2 + _easycom_uni_load_more2 + _easycom_s_empty2 + _easycom_s_layout2)();
}
const _easycom_su_tabs = () => "../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_su_popup = () => "../../sheep/ui/su-popup/su-popup.js";
const _easycom_s_goods_column = () => "../../sheep/components/s-goods-column/s-goods-column.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_tabs + _easycom_su_sticky + _easycom_su_popup + _easycom_s_goods_column + _easycom_uni_load_more + _easycom_s_empty + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "list",
  emits: ["close", "change"],
  setup(__props, { emit: __emit }) {
    const sys_navBar = sheep_index.sheep.$platform.navbar;
    const state = common_vendor.reactive({
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 6
      },
      currentSort: void 0,
      currentOrder: void 0,
      currentTab: 0,
      // 当前选中的 tab
      curFilter: 0,
      // 当前选中的 list 筛选项
      showFilter: false,
      iconStatus: false,
      // true - 单列布局；false - 双列布局
      keyword: "",
      categoryId: 0,
      tabList: [
        {
          name: "综合推荐",
          list: [
            {
              label: "综合推荐"
            },
            {
              label: "价格升序",
              sort: "price",
              order: true
            },
            {
              label: "价格降序",
              sort: "price",
              order: false
            }
          ]
        },
        {
          name: "销量",
          sort: "salesCount",
          order: false
        },
        {
          name: "新品优先",
          value: "createTime",
          order: false
        }
      ],
      loadStatus: "",
      leftGoodsList: [],
      // 双列布局 - 左侧商品
      rightGoodsList: []
      // 双列布局 - 右侧商品
    });
    let count = 0;
    let leftHeight = 0;
    let rightHeight = 0;
    function mountMasonry(height = 0, where = "left") {
      if (where === "left") {
        leftHeight += height;
      } else {
        rightHeight += height;
      }
      if (!state.pagination.list[count]) {
        return;
      }
      if (leftHeight <= rightHeight) {
        state.leftGoodsList.push(state.pagination.list[count]);
      } else {
        state.rightGoodsList.push(state.pagination.list[count]);
      }
      count++;
    }
    function emptyList() {
      sheep_helper_utils.resetPagination(state.pagination);
      state.leftGoodsList = [];
      state.rightGoodsList = [];
      count = 0;
      leftHeight = 0;
      rightHeight = 0;
    }
    function onSearch(e) {
      state.keyword = e;
      emptyList();
      getList(state.currentSort, state.currentOrder);
    }
    function onTabsChange(e) {
      if (state.tabList[e.index].list) {
        state.currentTab = e.index;
        state.showFilter = !state.showFilter;
        return;
      }
      state.showFilter = false;
      if (e.index === state.currentTab) {
        return;
      }
      state.currentTab = e.index;
      state.currentSort = e.sort;
      state.currentOrder = e.order;
      emptyList();
      getList(e.sort, e.order);
    }
    const onFilterItem = (val) => {
      if (state.currentSort === state.tabList[0].list[val].sort && state.currentOrder === state.tabList[0].list[val].order) {
        state.showFilter = false;
        return;
      }
      state.showFilter = false;
      state.curFilter = val;
      state.tabList[0].name = state.tabList[0].list[val].label;
      state.currentSort = state.tabList[0].list[val].sort;
      state.currentOrder = state.tabList[0].list[val].order;
      emptyList();
      getList();
    };
    async function getList() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_product_spu.SpuApi.getSpuPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        sortField: state.currentSort,
        sortAsc: state.currentOrder,
        categoryId: state.categoryId,
        keyword: state.keyword
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
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getList(state.currentSort, state.currentOrder);
    }
    common_vendor.onLoad((options) => {
      state.categoryId = options.categoryId;
      state.keyword = options.keyword;
      getList(state.currentSort, state.currentOrder);
    });
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(onTabsChange, "ac"),
        b: common_vendor.p({
          list: state.tabList,
          scrollable: false,
          current: state.currentTab
        }),
        c: state.iconStatus
      }, state.iconStatus ? {} : {}, {
        d: common_vendor.o(($event) => state.iconStatus = !state.iconStatus, "72"),
        e: common_vendor.p({
          bgColor: "#fff"
        }),
        f: common_vendor.f(state.tabList[state.currentTab].list, (item, index, i0) => {
          return {
            a: common_vendor.t(item.label),
            b: item.value,
            c: common_vendor.n({
              "filter-item-active": index === state.curFilter
            }),
            d: common_vendor.o(($event) => onFilterItem(index), item.value)
          };
        }),
        g: common_vendor.o(($event) => state.showFilter = false, "00"),
        h: common_vendor.p({
          show: state.showFilter,
          type: "top",
          round: "10",
          space: common_vendor.unref(sys_navBar) + 38,
          backgroundColor: "#F6F6F6",
          zIndex: 10
        }),
        i: state.iconStatus && state.pagination.total > 0
      }, state.iconStatus && state.pagination.total > 0 ? {
        j: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "7f2f18c6-4-" + i0 + ",7f2f18c6-0",
            c: common_vendor.p({
              size: "lg",
              data: item,
              topRadius: 10,
              bottomRadius: 10
            }),
            d: item.id
          };
        })
      } : {}, {
        k: !state.iconStatus && state.pagination.total > 0
      }, !state.iconStatus && state.pagination.total > 0 ? {
        l: common_vendor.f(state.leftGoodsList, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: common_vendor.o(($event) => mountMasonry($event, "left"), item.id),
            c: "7f2f18c6-5-" + i0 + ",7f2f18c6-0",
            d: common_vendor.p({
              size: "md",
              data: item,
              topRadius: 10,
              bottomRadius: 10
            }),
            e: item.id
          };
        }),
        m: common_vendor.f(state.rightGoodsList, (item, k0, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: common_vendor.o(($event) => mountMasonry($event, "right"), item.id),
            c: "7f2f18c6-6-" + i0 + ",7f2f18c6-0",
            d: common_vendor.p({
              size: "md",
              topRadius: 10,
              bottomRadius: 10,
              data: item
            }),
            e: item.id
          };
        })
      } : {}, {
        n: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        o: common_vendor.o(loadMore, "28"),
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
          icon: "/static/soldout-empty.png",
          text: "暂无商品"
        })
      } : {}, {
        s: common_vendor.o(onSearch, "7f"),
        t: common_vendor.p({
          navbar: "normal",
          leftWidth: 0,
          rightWidth: 0,
          tools: "search",
          defaultSearch: state.keyword
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7f2f18c6"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/goods/list.js.map
