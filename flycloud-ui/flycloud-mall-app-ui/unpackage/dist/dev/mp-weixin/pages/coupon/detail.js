"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_promotion_coupon = require("../../sheep/api/promotion/coupon.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_api_product_category = require("../../sheep/api/product/category.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_uni_collapse_item2 = common_vendor.resolveComponent("uni-collapse-item");
  const _easycom_uni_collapse2 = common_vendor.resolveComponent("uni-collapse");
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_collapse_item2 + _easycom_uni_collapse2 + _easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_goods_column2 + _easycom_uni_load_more2 + _easycom_s_empty2 + _easycom_s_layout2)();
}
const _easycom_uni_collapse_item = () => "../../uni_modules/uni-collapse/components/uni-collapse-item/uni-collapse-item.js";
const _easycom_uni_collapse = () => "../../uni_modules/uni-collapse/components/uni-collapse/uni-collapse.js";
const _easycom_su_tabs = () => "../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_goods_column = () => "../../sheep/components/s-goods-column/s-goods-column.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_collapse_item + _easycom_uni_collapse + _easycom_su_tabs + _easycom_su_sticky + _easycom_s_goods_column + _easycom_uni_load_more + _easycom_s_empty + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "detail",
  setup(__props) {
    const state = common_vendor.reactive({
      id: 0,
      // 优惠劵模版编号 templateId
      couponId: 0,
      // 用户优惠劵编号 couponId
      coupon: {},
      // 优惠劵信息
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      },
      categoryId: 0,
      // 选中的商品分类编号
      tabMaps: [],
      // 指定分类时，每个分类构成一个 tab
      currentTab: 0,
      // 选中的 tabMaps 下标
      loadStatus: ""
    });
    function onTabsChange(e) {
      sheep_helper_utils.resetPagination(state.pagination);
      state.currentTab = e.index;
      state.categoryId = e.value;
      getGoodsListByCategory();
    }
    async function getGoodsListByCategory() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_product_spu.SpuApi.getSpuPage({
        categoryId: state.categoryId,
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
    async function getGoodsListById() {
      const { data, code } = await sheep_api_product_spu.SpuApi.getSpuListByIds(state.coupon.productScopeValues.join(","));
      if (code !== 0) {
        return;
      }
      state.pagination.list = data;
    }
    async function getCategoryList() {
      const { data, code } = await sheep_api_product_category.CategoryApi.getCategoryListByIds(
        state.coupon.productScopeValues.join(",")
      );
      if (code !== 0) {
        return;
      }
      state.tabMaps = data.map((category) => ({ name: category.name, value: category.id }));
      if (state.tabMaps.length > 0) {
        state.categoryId = state.tabMaps[0].value;
        await getGoodsListByCategory();
      }
    }
    async function getCoupon() {
      const { code } = await sheep_api_promotion_coupon.CouponApi.takeCoupon(state.id);
      if (code !== 0) {
        return;
      }
      common_vendor.index.showToast({
        title: "领取成功"
      });
      setTimeout(() => {
        getCouponContent();
      }, 1e3);
    }
    async function getCouponContent() {
      const { code, data } = state.id > 0 ? await sheep_api_promotion_coupon.CouponApi.getCouponTemplate(state.id) : await sheep_api_promotion_coupon.CouponApi.getCoupon(state.couponId);
      if (code !== 0) {
        return;
      }
      state.coupon = data;
      if (state.coupon.productScope === 2) {
        await getGoodsListById();
      } else if (state.coupon.productScope === 3) {
        await getCategoryList();
      }
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getGoodsListByCategory();
    }
    common_vendor.onLoad((options) => {
      state.id = options.id;
      state.couponId = options.couponId;
      getCouponContent(state.id, state.couponId);
    });
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/app/coupon_icon.png"),
        b: common_vendor.t(state.coupon.name),
        c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.coupon.usePrice)),
        d: common_vendor.t(state.coupon.discountType === 1 ? "减 " + common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.coupon.discountPrice) + " 元" : "打 " + state.coupon.discountPercent / 10 + " 折"),
        e: state.id > 0
      }, state.id > 0 ? {
        f: common_vendor.t(state.coupon.canTake ? "立即领取" : "已领取")
      } : {
        g: common_vendor.t(state.coupon.status === 1 ? "可使用" : state.coupon.status === 2 ? "已使用" : "已过期")
      }, {
        h: common_vendor.n(state.coupon.canTake || state.coupon.status === 1 ? "use-btn" : "disable-btn"),
        i: !state.coupon.canTake,
        j: common_vendor.o(getCoupon, "a1"),
        k: state.coupon.validityType === 2
      }, state.coupon.validityType === 2 ? {
        l: common_vendor.t(state.coupon.fixedEndTerm)
      } : {
        m: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.coupon.validStartTime, "yyyy-mm-dd")),
        n: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.coupon.validEndTime, "yyyy-mm-dd"))
      }, {
        o: common_vendor.t(state.coupon.discountType === 1 ? "满减券" : "折扣券"),
        p: state.coupon.description
      }, state.coupon.description ? {
        q: common_vendor.t(state.coupon.description),
        r: common_vendor.p({
          title: "优惠券说明"
        })
      } : {}, {
        s: state.coupon.productScope === 1
      }, state.coupon.productScope === 1 ? {} : common_vendor.e({
        t: common_vendor.t(state.coupon.productScope === 2 ? "指定商品可用" : "指定分类可用"),
        v: state.coupon.productScope === 3
      }, state.coupon.productScope === 3 ? {
        w: common_vendor.o(onTabsChange, "69"),
        x: common_vendor.p({
          scrollable: true,
          list: state.tabMaps,
          current: state.currentTab
        })
      } : {}, {
        y: common_vendor.p({
          bgColor: "#fff"
        })
      }), {
        z: state.coupon.productScope === 2
      }, state.coupon.productScope === 2 ? {
        A: common_vendor.f(state.pagination.list, (item, index, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), index),
            b: "2d1fcfdf-5-" + i0 + ",2d1fcfdf-0",
            c: common_vendor.p({
              size: "lg",
              data: item,
              goodsFields: {
                title: {
                  show: true
                },
                subtitle: {
                  show: true
                },
                price: {
                  show: true
                },
                original_price: {
                  show: true
                },
                sales: {
                  show: true
                },
                stock: {
                  show: false
                }
              }
            }),
            d: index
          };
        })
      } : {}, {
        B: state.coupon.productScope === 3
      }, state.coupon.productScope === 3 ? {
        C: common_vendor.f(state.pagination.list, (item, index, i0) => {
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), index),
            b: "2d1fcfdf-6-" + i0 + ",2d1fcfdf-0",
            c: common_vendor.p({
              size: "lg",
              data: item,
              goodsFields: {
                title: {
                  show: true
                },
                subtitle: {
                  show: true
                },
                price: {
                  show: true
                },
                original_price: {
                  show: true
                },
                sales: {
                  show: true
                },
                stock: {
                  show: false
                }
              }
            }),
            d: index
          };
        })
      } : {}, {
        D: state.pagination.total > 0 && state.coupon.productScope === 3
      }, state.pagination.total > 0 && state.coupon.productScope === 3 ? {
        E: common_vendor.o(loadMore, "4f"),
        F: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        G: state.coupon.productScope === 3 && state.pagination.total === 0
      }, state.coupon.productScope === 3 && state.pagination.total === 0 ? {
        H: common_vendor.p({
          paddingTop: "0",
          icon: "/static/soldout-empty.png",
          text: "暂无商品"
        })
      } : {}, {
        I: common_vendor.p({
          title: "优惠券详情"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2d1fcfdf"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/coupon/detail.js.map
