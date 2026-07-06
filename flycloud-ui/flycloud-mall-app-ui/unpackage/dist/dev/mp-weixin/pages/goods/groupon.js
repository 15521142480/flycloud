"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_promotion_combination = require("../../sheep/api/promotion/combination.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_su_swiper2 = common_vendor.resolveComponent("su-swiper");
  const _easycom_s_select_groupon_sku2 = common_vendor.resolveComponent("s-select-groupon-sku");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_empty2 + _easycom_su_swiper2 + _easycom_s_select_groupon_sku2 + _easycom_s_layout2)();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_su_swiper = () => "../../sheep/ui/su-swiper/su-swiper.js";
const _easycom_s_select_groupon_sku = () => "../../sheep/components/s-select-groupon-sku/s-select-groupon-sku.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (detailNavbar + detailSkeleton + _easycom_s_empty + _easycom_su_swiper + detailCellSku + grouponCardList + _easycom_s_select_groupon_sku + detailCommentCard + detailContentCard + detailTabbar + _easycom_s_layout)();
}
const detailNavbar = () => "./components/detail/detail-navbar.js";
const detailCellSku = () => "./components/detail/detail-cell-sku.js";
const detailTabbar = () => "./components/detail/detail-tabbar.js";
const detailSkeleton = () => "./components/detail/detail-skeleton.js";
const detailCommentCard = () => "./components/detail/detail-comment-card.js";
const detailContentCard = () => "./components/detail/detail-content-card.js";
const grouponCardList = () => "./components/groupon/groupon-card-list.js";
const _sfc_main = {
  __name: "groupon",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "9d308a56": common_vendor.unref(headerBg),
      "54d82b7e": common_vendor.unref(btnBg),
      "1431bd3c": common_vendor.unref(disabledBtnBg),
      "2fee5ec0": common_vendor.unref(grouponBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-bg.png");
    const btnBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-btn.png");
    const disabledBtnBg = sheep_index.sheep.$url.css("/static/img/shop/goods/activity-btn-disabled.png");
    const grouponBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-tip-bg.png");
    common_vendor.onPageScroll(() => {
    });
    const state = common_vendor.reactive({
      skeletonLoading: true,
      // 骨架屏
      goodsId: 0,
      // 商品ID
      goodsInfo: {},
      // 商品信息
      goodsSwiper: [],
      // 商品轮播图
      showSelectSku: false,
      // 显示规格弹框
      selectedSku: {},
      // 选中的规格属性
      activity: {},
      // 团购活动
      grouponId: 0,
      // 团购ID
      grouponNum: 0,
      // 团购人数
      grouponAction: "create",
      // 团购操作
      combinationHeadId: null
      // 拼团团长编号
    });
    const endTime = common_vendor.computed(() => {
      return sheep_hooks_useGoods.useDurationTime(state.activity.endTime);
    });
    function onSkuChange(e) {
      state.selectedSku = e;
    }
    function onSkuClose() {
      state.showSelectSku = false;
    }
    function onCreateGroupon() {
      state.grouponAction = "create";
      state.grouponId = 0;
      state.showSelectSku = true;
    }
    function onJoinGroupon(record) {
      state.grouponAction = "join";
      state.grouponId = record.activityId;
      state.combinationHeadId = record.id;
      state.grouponNum = record.userSize;
      state.showSelectSku = true;
    }
    function onBuy(sku) {
      sheep_index.sheep.$router.go("/pages/order/confirm", {
        data: JSON.stringify({
          order_type: "goods",
          combinationActivityId: state.activity.id,
          combinationHeadId: state.combinationHeadId,
          items: [
            {
              skuId: sku.id,
              count: sku.count
            }
          ]
        })
      });
    }
    const shareInfo = common_vendor.computed(() => {
      if (common_vendor.isEmpty(state.activity))
        return {};
      return sheep_index.sheep.$platform.share.getShareInfo(
        {
          title: state.activity.name,
          image: sheep_index.sheep.$url.cdn(state.goodsInfo.picUrl),
          params: {
            page: sheep_helper_const.SharePageEnum.GROUPON.value,
            query: state.activity.id
          }
        },
        {
          type: "goods",
          // 商品海报
          title: state.activity.name,
          // 商品标题
          image: sheep_index.sheep.$url.cdn(state.goodsInfo.picUrl),
          // 商品主图
          price: sheep_hooks_useGoods.fen2yuan(state.goodsInfo.price),
          // 商品价格
          marketPrice: sheep_hooks_useGoods.fen2yuan(state.goodsInfo.marketPrice)
          // 商品原价
        }
      );
    });
    common_vendor.onLoad(async (options) => {
      if (!options.id) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      state.grouponId = options.id;
      const { code, data: activity } = await sheep_api_promotion_combination.CombinationApi.getCombinationActivity(state.grouponId);
      if (code !== 0) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      state.activity = activity;
      const { data: spu } = await sheep_api_product_spu.SpuApi.getSpuDetail(activity.spuId);
      if (code !== 0) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      state.goodsId = spu.id;
      spu.price = activity.products.reduce((min, product) => {
        return Math.min(min, product.combinationPrice || Infinity);
      }, Infinity);
      spu.skus.forEach((sku) => {
        const product = activity.products.find((product2) => product2.skuId === sku.id);
        if (product) {
          sku.price = product.combinationPrice;
        } else {
          sku.stock = 0;
        }
      });
      state.skeletonLoading = false;
      if (code === 0) {
        state.goodsInfo = spu;
        state.grouponNum = activity.userSize;
        state.goodsSwiper = sheep_hooks_useGoods.formatGoodsSwiper(state.goodsInfo.sliderPicUrls);
      } else {
        state.goodsInfo = null;
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.skeletonLoading
      }, state.skeletonLoading ? {} : state.goodsInfo === null || state.activity.status !== 0 || state.activity.endTime < (/* @__PURE__ */ new Date()).getTime() ? {
        c: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.back(), "44"),
        d: common_vendor.p({
          text: "活动不存在或已结束",
          icon: "/static/soldout-empty.png",
          showAction: true,
          actionText: "返回上一页"
        })
      } : common_vendor.e({
        e: common_vendor.p({
          isPreview: true,
          list: state.goodsSwiper,
          dotStyle: "tag",
          imageMode: "widthFix",
          dotCur: "bg-mask-40",
          seizeHeight: 750
        }),
        f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.activity.price || state.goodsInfo.price)),
        g: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/groupon-tag.png"),
        h: state.goodsInfo.price
      }, state.goodsInfo.price ? {
        i: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.goodsInfo.marketPrice))
      } : {}, {
        j: endTime.value.ms > 0
      }, endTime.value.ms > 0 ? {
        k: common_vendor.t(endTime.value.h),
        l: common_vendor.t(endTime.value.m),
        m: common_vendor.t(endTime.value.s)
      } : {}, {
        n: common_vendor.t(state.goodsInfo.name),
        o: common_vendor.t(state.goodsInfo.introduction),
        p: common_vendor.o(($event) => state.showSelectSku = true, "d0"),
        q: common_vendor.p({
          sku: state.selectedSku
        }),
        r: common_vendor.o(onJoinGroupon, "7c"),
        s: common_vendor.o(($event) => state.activity = $event, "99"),
        t: common_vendor.p({
          modelValue: state.activity
        }),
        v: common_vendor.o(onBuy, "b7"),
        w: common_vendor.o(onSkuChange, "31"),
        x: common_vendor.o(onSkuClose, "d8"),
        y: common_vendor.p({
          show: state.showSelectSku,
          goodsInfo: state.goodsInfo,
          grouponAction: state.grouponAction,
          grouponNum: state.grouponNum
        }),
        z: common_vendor.p({
          goodsId: state.goodsId
        }),
        A: common_vendor.p({
          content: state.goodsInfo.description
        }),
        B: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.goodsInfo.marketPrice)),
        C: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
          id: state.goodsInfo.id
        }), "3d"),
        D: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.price * state.selectedSku.count || state.activity.price * state.selectedSku.count || state.goodsInfo.price * state.selectedSku.count || state.goodsInfo.price)),
        E: state.activity.startTime > (/* @__PURE__ */ new Date()).getTime()
      }, state.activity.startTime > (/* @__PURE__ */ new Date()).getTime() ? {} : state.activity.endTime <= (/* @__PURE__ */ new Date()).getTime() ? {} : common_vendor.e({
        G: state.goodsInfo.stock === 0
      }, state.goodsInfo.stock === 0 ? {} : {}), {
        F: state.activity.endTime <= (/* @__PURE__ */ new Date()).getTime(),
        H: common_vendor.o(onCreateGroupon, "b3"),
        I: common_vendor.n(state.activity.status === 0 && state.goodsInfo.stock !== 0 ? "check-btn-box" : "disabled-btn-box"),
        J: state.goodsInfo.stock === 0 || state.activity.status !== 0,
        K: common_vendor.o(($event) => state.goodsInfo = $event, "dd"),
        L: common_vendor.p({
          modelValue: state.goodsInfo
        })
      }), {
        b: state.goodsInfo === null || state.activity.status !== 0 || state.activity.endTime < (/* @__PURE__ */ new Date()).getTime(),
        M: common_vendor.s(_ctx.__cssVars()),
        N: common_vendor.p({
          onShareAppMessage: shareInfo.value,
          navbar: "goods"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-efe71281"]]);
_sfc_main.__runtimeHooks = 3;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/goods/groupon.js.map
