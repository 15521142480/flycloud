"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_promotion_seckill = require("../../sheep/api/promotion/seckill.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_su_swiper2 = common_vendor.resolveComponent("su-swiper");
  const _easycom_s_select_seckill_sku2 = common_vendor.resolveComponent("s-select-seckill-sku");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_empty2 + _easycom_su_swiper2 + _easycom_s_select_seckill_sku2 + _easycom_s_layout2)();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_su_swiper = () => "../../sheep/ui/su-swiper/su-swiper.js";
const _easycom_s_select_seckill_sku = () => "../../sheep/components/s-select-seckill-sku/s-select-seckill-sku.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (detailNavbar + detailSkeleton + _easycom_s_empty + _easycom_su_swiper + detailProgress + detailCellSku + _easycom_s_select_seckill_sku + detailCommentCard + detailContentCard + detailTabbar + _easycom_s_layout)();
}
const detailNavbar = () => "./components/detail/detail-navbar.js";
const detailCellSku = () => "./components/detail/detail-cell-sku.js";
const detailTabbar = () => "./components/detail/detail-tabbar.js";
const detailSkeleton = () => "./components/detail/detail-skeleton.js";
const detailCommentCard = () => "./components/detail/detail-comment-card.js";
const detailContentCard = () => "./components/detail/detail-content-card.js";
const detailProgress = () => "./components/detail/detail-progress.js";
const _sfc_main = {
  __name: "seckill",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "4f732cf8": common_vendor.unref(headerBg),
      "52c6d9a2": common_vendor.unref(btnBg),
      "7fa54813": common_vendor.unref(disabledBtnBg),
      "056c2c42": common_vendor.unref(seckillBg),
      "3634f41e": common_vendor.unref(grouponBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/goods/seckill-bg.png");
    const btnBg = sheep_index.sheep.$url.css("/static/img/shop/goods/seckill-btn.png");
    const disabledBtnBg = sheep_index.sheep.$url.css("/static/img/shop/goods/activity-btn-disabled.png");
    const seckillBg = sheep_index.sheep.$url.css("/static/img/shop/goods/seckill-tip-bg.png");
    const grouponBg = sheep_index.sheep.$url.css("/static/img/shop/goods/groupon-tip-bg.png");
    common_vendor.onPageScroll(() => {
    });
    const state = common_vendor.reactive({
      skeletonLoading: true,
      goodsInfo: {},
      showSelectSku: false,
      goodsSwiper: [],
      selectedSku: {},
      showModel: false,
      total: 0,
      percent: 0,
      price: ""
    });
    const endTime = common_vendor.computed(() => {
      return sheep_hooks_useGoods.useDurationTime(activity.value.endTime);
    });
    function onSkuChange(e) {
      state.selectedSku = e;
    }
    function onBuy(sku) {
      sheep_index.sheep.$router.go("/pages/order/confirm", {
        data: JSON.stringify({
          order_type: "goods",
          buy_type: "seckill",
          seckillActivityId: activity.value.id,
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
      if (common_vendor.isEmpty(common_vendor.unref(activity)))
        return {};
      return sheep_index.sheep.$platform.share.getShareInfo(
        {
          title: activity.value.name,
          image: sheep_index.sheep.$url.cdn(state.goodsInfo.picUrl),
          params: {
            page: sheep_helper_const.SharePageEnum.SECKILL.value,
            query: activity.value.id
          }
        },
        {
          type: "goods",
          // 商品海报
          title: activity.value.name,
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
    const activity = common_vendor.ref();
    const timeStatusEnum = common_vendor.ref("");
    const getActivity = async (id) => {
      const { data } = await sheep_api_promotion_seckill.SeckillApi.getSeckillActivity(id);
      if (!data) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      activity.value = data;
      timeStatusEnum.value = sheep_helper_const.getTimeStatusEnum(activity.value.startTime, activity.value.endTime);
      state.percent = 100 - data.stock / data.totalStock * 100;
      await getSpu(data.spuId);
    };
    const getSpu = async (id) => {
      const { data } = await sheep_api_product_spu.SpuApi.getSpuDetail(id);
      if (!data) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      data.activity_type = "seckill";
      state.goodsInfo = data;
      state.goodsSwiper = sheep_hooks_useGoods.formatGoodsSwiper(state.goodsInfo.sliderPicUrls);
      state.goodsInfo.price = common_vendor.min([
        state.goodsInfo.price,
        ...activity.value.products.map((spu) => spu.seckillPrice)
      ]);
      data.skus.forEach((sku) => {
        const product = activity.value.products.find((product2) => product2.skuId === sku.id);
        if (product) {
          sku.price = product.seckillPrice;
          sku.stock = Math.min(sku.stock, product.stock);
        } else {
          sku.stock = 0;
        }
        if (activity.value.totalLimitCount > 0 && activity.value.singleLimitCount > 0) {
          sku.limitCount = Math.min(activity.value.totalLimitCount, activity.value.singleLimitCount);
        } else if (activity.value.totalLimitCount > 0) {
          sku.limitCount = activity.value.totalLimitCount;
        } else if (activity.value.singleLimitCount > 0) {
          sku.limitCount = activity.value.singleLimitCount;
        }
      });
      state.skeletonLoading = false;
    };
    common_vendor.onLoad((options) => {
      if (!options.id) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      getActivity(options.id);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.skeletonLoading
      }, state.skeletonLoading ? {} : state.goodsInfo === null || state.goodsInfo.activity_type !== "seckill" || endTime.value.ms <= 0 ? {
        c: common_vendor.p({
          text: "活动不存在或已结束",
          icon: "/static/soldout-empty.png",
          showAction: true,
          actionText: "再逛逛",
          actionUrl: "/pages/goods/list"
        })
      } : common_vendor.e({
        d: common_vendor.p({
          isPreview: true,
          list: state.goodsSwiper,
          dotStyle: "tag",
          imageMode: "widthFix",
          dotCur: "bg-mask-40",
          seizeHeight: 750
        }),
        e: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.price || state.goodsInfo.price)),
        f: endTime.value.ms > 0
      }, endTime.value.ms > 0 ? {
        g: common_vendor.t(endTime.value.h),
        h: common_vendor.t(endTime.value.m),
        i: common_vendor.t(endTime.value.s)
      } : {}, {
        j: state.goodsInfo.marketPrice
      }, state.goodsInfo.marketPrice ? {
        k: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.marketPrice || state.goodsInfo.marketPrice))
      } : {}, {
        l: common_vendor.p({
          percent: state.percent
        }),
        m: common_vendor.t(state.goodsInfo.name || ""),
        n: common_vendor.t(state.goodsInfo.introduction),
        o: common_vendor.o(($event) => state.showSelectSku = true, "9c"),
        p: common_vendor.p({
          sku: state.selectedSku
        }),
        q: common_vendor.o(onBuy, "66"),
        r: common_vendor.o(onSkuChange, "a0"),
        s: common_vendor.o(($event) => state.showSelectSku = false, "e0"),
        t: common_vendor.o(($event) => state.goodsInfo = $event, "1c"),
        v: common_vendor.p({
          show: state.showSelectSku,
          ["single-limit-count"]: activity.value.singleLimitCount,
          modelValue: state.goodsInfo
        }),
        w: common_vendor.p({
          goodsId: state.goodsInfo.id
        }),
        x: common_vendor.p({
          content: state.goodsInfo.description
        }),
        y: state.goodsInfo.marketPrice
      }, state.goodsInfo.marketPrice ? {
        z: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.goodsInfo.marketPrice)),
        A: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
          id: state.goodsInfo.id
        }), "d3")
      } : {
        B: common_vendor.n(state.goodsInfo.stock === 0 || timeStatusEnum.value !== common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED ? "" : "")
      }, {
        C: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.goodsInfo.price)),
        D: timeStatusEnum.value === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED
      }, timeStatusEnum.value === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED ? common_vendor.e({
        E: state.goodsInfo.stock === 0
      }, state.goodsInfo.stock === 0 ? {} : {}) : {
        F: common_vendor.t(timeStatusEnum.value)
      }, {
        G: common_vendor.o(($event) => state.showSelectSku = true, "86"),
        H: common_vendor.n(timeStatusEnum.value === common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED && state.goodsInfo.stock != 0 ? "check-btn-box" : "disabled-btn-box"),
        I: state.goodsInfo.stock === 0 || timeStatusEnum.value !== common_vendor.unref(sheep_helper_const.TimeStatusEnum).STARTED,
        J: common_vendor.o(($event) => state.goodsInfo = $event, "08"),
        K: common_vendor.p({
          modelValue: state.goodsInfo
        })
      }), {
        b: state.goodsInfo === null || state.goodsInfo.activity_type !== "seckill" || endTime.value.ms <= 0,
        L: common_vendor.s(_ctx.__cssVars()),
        M: common_vendor.p({
          onShareAppMessage: shareInfo.value,
          navbar: "goods"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-960ccb8e"]]);
_sfc_main.__runtimeHooks = 3;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/goods/seckill.js.map
