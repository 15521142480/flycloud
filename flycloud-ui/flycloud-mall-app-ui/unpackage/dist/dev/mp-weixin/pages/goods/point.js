"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
const sheep_api_promotion_point = require("../../sheep/api/promotion/point.js");
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
  (detailNavbar + detailSkeleton + _easycom_s_empty + _easycom_su_swiper + detailCellSku + _easycom_s_select_seckill_sku + detailCommentCard + detailContentCard + detailTabbar + _easycom_s_layout)();
}
const detailNavbar = () => "./components/detail/detail-navbar.js";
const detailCellSku = () => "./components/detail/detail-cell-sku.js";
const detailTabbar = () => "./components/detail/detail-tabbar.js";
const detailSkeleton = () => "./components/detail/detail-skeleton.js";
const detailCommentCard = () => "./components/detail/detail-comment-card.js";
const detailContentCard = () => "./components/detail/detail-content-card.js";
const _sfc_main = {
  __name: "point",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "e32d7726": common_vendor.unref(headerBg),
      "58366e34": common_vendor.unref(btnBg),
      "4658526c": common_vendor.unref(disabledBtnBg),
      "13a4ae79": common_vendor.unref(seckillBg),
      "19c3efb0": common_vendor.unref(grouponBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/goods/score-bg.png");
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
      price: ""
    });
    function onSkuChange(e) {
      state.selectedSku = e;
    }
    function onBuy(sku) {
      sheep_index.sheep.$router.go("/pages/order/confirm", {
        data: JSON.stringify({
          order_type: "goods",
          buy_type: "point",
          pointActivityId: activity.value.id,
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
            page: sheep_helper_const.SharePageEnum.POINT.value,
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
          price: (getShowPrice.value.price || 0) + ` + ${getShowPrice.value.point} 积分`,
          // 积分价格
          marketPrice: sheep_hooks_useGoods.fen2yuan(state.goodsInfo.marketPrice)
          // 商品原价
        }
      );
    });
    const activity = common_vendor.ref();
    const getShowPrice = common_vendor.computed(() => {
      if (!common_vendor.isEmpty(state.selectedSku)) {
        const sku = state.selectedSku;
        return {
          point: sku.point,
          price: !sku.pointPrice ? "" : sheep_hooks_useGoods.fen2yuan(sku.pointPrice)
        };
      }
      return {
        point: activity.value.point,
        price: !activity.value.price ? "" : sheep_hooks_useGoods.fen2yuan(activity.value.price)
      };
    });
    const getActivity = async (id) => {
      const { data } = await sheep_api_promotion_point.PointApi.getPointActivity(id);
      if (!data) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      activity.value = data;
      await getSpu(data.spuId);
    };
    const getSpu = async (id) => {
      const { data } = await sheep_api_product_spu.SpuApi.getSpuDetail(id);
      if (!data) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      data.activity_type = sheep_helper_const.PromotionActivityTypeEnum.POINT.type;
      state.goodsInfo = data;
      state.goodsInfo.stock = Math.min(data.stock, activity.value.stock);
      state.goodsSwiper = sheep_hooks_useGoods.formatGoodsSwiper(state.goodsInfo.sliderPicUrls);
      data.skus.forEach((sku) => {
        const product = activity.value.products.find((product2) => product2.skuId === sku.id);
        if (product) {
          sku.point = product.point;
          sku.pointPrice = product.price;
          sku.stock = Math.min(sku.stock, product.stock);
          sku.limitCount = product.count;
        } else {
          sku.stock = 0;
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
      }, state.skeletonLoading ? {} : state.goodsInfo === null || state.goodsInfo.activity_type !== common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? {
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
        e: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        f: common_vendor.t(getShowPrice.value.point),
        g: common_vendor.t(!getShowPrice.value.price || getShowPrice.value.price === 0 ? "" : `+￥${getShowPrice.value.price}`),
        h: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatExchange)(state.goodsInfo.sales_show_type, state.goodsInfo.sales)),
        i: state.goodsInfo.marketPrice
      }, state.goodsInfo.marketPrice ? {
        j: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.selectedSku.marketPrice || state.goodsInfo.marketPrice))
      } : {}, {
        k: common_vendor.t(state.goodsInfo.name || ""),
        l: common_vendor.t(state.goodsInfo.introduction),
        m: common_vendor.o(($event) => state.showSelectSku = true, "12"),
        n: common_vendor.p({
          sku: state.selectedSku
        }),
        o: common_vendor.o(onBuy, "1a"),
        p: common_vendor.o(onSkuChange, "cb"),
        q: common_vendor.o(($event) => state.showSelectSku = false, "0b"),
        r: common_vendor.o(($event) => state.goodsInfo = $event, "ef"),
        s: common_vendor.p({
          show: state.showSelectSku,
          ["single-limit-count"]: activity.value.singleLimitCount,
          modelValue: state.goodsInfo
        }),
        t: common_vendor.p({
          goodsId: state.goodsInfo.id
        }),
        v: common_vendor.p({
          content: state.goodsInfo.description
        }),
        w: state.goodsInfo.marketPrice
      }, state.goodsInfo.marketPrice ? {
        x: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.goodsInfo.marketPrice)),
        y: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
          id: state.goodsInfo.id
        }), "84")
      } : {}, {
        z: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        A: common_vendor.t(getShowPrice.value.point),
        B: common_vendor.t(!getShowPrice.value.price || getShowPrice.value.price === 0 ? "" : `+￥${getShowPrice.value.price}`),
        C: state.goodsInfo.stock === 0
      }, state.goodsInfo.stock === 0 ? {} : {}, {
        D: common_vendor.o(($event) => state.showSelectSku = true, "93"),
        E: common_vendor.n(state.goodsInfo.stock != 0 ? "check-btn-box" : "disabled-btn-box"),
        F: state.goodsInfo.stock === 0,
        G: common_vendor.o(($event) => state.goodsInfo = $event, "37"),
        H: common_vendor.p({
          modelValue: state.goodsInfo
        })
      }), {
        b: state.goodsInfo === null || state.goodsInfo.activity_type !== common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type,
        I: common_vendor.s(_ctx.__cssVars()),
        J: common_vendor.p({
          onShareAppMessage: shareInfo.value,
          navbar: "goods"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-eca7c103"]]);
_sfc_main.__runtimeHooks = 3;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/goods/point.js.map
