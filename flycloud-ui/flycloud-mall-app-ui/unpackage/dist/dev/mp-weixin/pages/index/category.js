"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_product_category = require("../../sheep/api/product/category.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (firstOne + firstTwo + secondOne + _easycom_uni_load_more + _easycom_s_layout)();
}
const secondOne = () => "./components/second-one.js";
const firstOne = () => "./components/first-one.js";
const firstTwo = () => "./components/first-two.js";
const _sfc_main = {
  __name: "category",
  setup(__props) {
    const state = common_vendor.reactive({
      style: "second_one",
      // first_one（一级 - 样式一）, first_two（二级 - 样式二）, second_one（二级）
      categoryList: [],
      // 商品分类树
      activeMenu: 0,
      // 选中的一级菜单，在 categoryList 的下标
      pagination: {
        // 商品分页
        list: [],
        // 商品列表
        total: [],
        // 商品总数
        pageNum: 1,
        pageSize: 6
      },
      loadStatus: ""
    });
    const { safeArea } = sheep_index.sheep.$platform.device;
    const pageHeight = common_vendor.computed(() => safeArea.height - 44 - 50);
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    async function getList() {
      const { code, data } = await sheep_api_product_category.CategoryApi.getCategoryList();
      if (code !== 0) {
        return;
      }
      state.categoryList = sheep_helper_utils.handleTree(data);
    }
    const onMenu = (val) => {
      state.activeMenu = val;
      if (state.style === "first_one" || state.style === "first_two") {
        state.pagination.pageNum = 1;
        state.pagination.list = [];
        state.pagination.total = 0;
        getGoodsList();
      }
    };
    async function getGoodsList() {
      state.loadStatus = "loading";
      const res = await sheep_api_product_spu.SpuApi.getSpuPage({
        categoryId: state.categoryList[state.activeMenu].id,
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize
      });
      if (res.code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, res.data.list);
      state.pagination.total = res.data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getGoodsList();
    }
    function initMenuIndex() {
      const appStore = sheep_index.sheep.$store("app");
      const tabbarParams = appStore.paramsForTabbar || {};
      const id = tabbarParams.id;
      appStore.clearParamsForTabbar();
      const foundCategory = state.categoryList.find((category) => category.id === Number(id));
      onMenu(foundCategory ? state.categoryList.indexOf(foundCategory) : 0);
    }
    common_vendor.onShow(async () => {
      await getList();
      initMenuIndex();
    });
    return (_ctx, _cache) => {
      var _a, _b;
      return common_vendor.e({
        a: common_vendor.f(state.categoryList, (item, index, i0) => {
          return {
            a: common_vendor.t(item.name),
            b: item.id,
            c: common_vendor.n({
              "menu-item-active": index === state.activeMenu
            }),
            d: common_vendor.o(($event) => onMenu(index), item.id)
          };
        }),
        b: common_vendor.s({
          height: pageHeight.value + "px"
        }),
        c: common_vendor.s({
          top: Number(statusBarHeight + 88) + "rpx"
        }),
        d: (_a = state.categoryList) == null ? void 0 : _a.length
      }, ((_b = state.categoryList) == null ? void 0 : _b.length) ? common_vendor.e({
        e: state.categoryList[state.activeMenu].picUrl
      }, state.categoryList[state.activeMenu].picUrl ? {
        f: common_vendor.unref(sheep_index.sheep).$url.cdn(state.categoryList[state.activeMenu].picUrl)
      } : {}, {
        g: state.style === "first_one"
      }, state.style === "first_one" ? {
        h: common_vendor.p({
          pagination: state.pagination
        })
      } : {}, {
        i: state.style === "first_two"
      }, state.style === "first_two" ? {
        j: common_vendor.p({
          pagination: state.pagination
        })
      } : {}, {
        k: state.style === "second_one"
      }, state.style === "second_one" ? {
        l: common_vendor.p({
          data: state.categoryList,
          activeMenu: state.activeMenu
        })
      } : {}, {
        m: (state.style === "first_one" || state.style === "first_two") && state.pagination.total > 0
      }, (state.style === "first_one" || state.style === "first_two") && state.pagination.total > 0 ? {
        n: common_vendor.o(loadMore, "ea"),
        o: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "点击查看更多"
          }
        })
      } : {}, {
        p: common_vendor.s({
          height: pageHeight.value + "px"
        })
      }) : {}, {
        q: common_vendor.p({
          bgStyle: {
            color: "#fff"
          },
          tabbar: "/pages/index/category",
          title: "分类"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1031f1cf"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/category.js.map
