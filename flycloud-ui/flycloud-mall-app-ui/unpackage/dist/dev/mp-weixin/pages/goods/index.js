"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_promotion_coupon = require("../../sheep/api/promotion/coupon.js");
const sheep_api_promotion_activity = require("../../sheep/api/promotion/activity.js");
const sheep_api_product_favorite = require("../../sheep/api/product/favorite.js");
const sheep_api_promotion_rewardActivity = require("../../sheep/api/promotion/rewardActivity.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_product_spu = require("../../sheep/api/product/spu.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_su_swiper2 = common_vendor.resolveComponent("su-swiper");
  const _easycom_s_count_down2 = common_vendor.resolveComponent("s-count-down");
  const _easycom_s_select_sku2 = common_vendor.resolveComponent("s-select-sku");
  const _easycom_s_activity_pop2 = common_vendor.resolveComponent("s-activity-pop");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_empty2 + _easycom_su_swiper2 + _easycom_s_count_down2 + _easycom_s_select_sku2 + _easycom_s_activity_pop2 + _easycom_s_layout2)();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_su_swiper = () => "../../sheep/ui/su-swiper/su-swiper.js";
const _easycom_s_count_down = () => "../../sheep/components/s-count-down/s-count-down.js";
const _easycom_s_select_sku = () => "../../sheep/components/s-select-sku/s-select-sku.js";
const _easycom_s_activity_pop = () => "../../sheep/components/s-activity-pop/s-activity-pop.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (detailNavbar + detailSkeleton + _easycom_s_empty + _easycom_su_swiper + _easycom_s_count_down + detailCellSku + _easycom_s_select_sku + detailCommentCard + detailContentCard + detailActivityTip + detailTabbar + _easycom_s_activity_pop + _easycom_s_layout)();
}
const detailNavbar = () => "./components/detail/detail-navbar.js";
const detailCellSku = () => "./components/detail/detail-cell-sku.js";
const detailTabbar = () => "./components/detail/detail-tabbar.js";
const detailSkeleton = () => "./components/detail/detail-skeleton.js";
const detailCommentCard = () => "./components/detail/detail-comment-card.js";
const detailContentCard = () => "./components/detail/detail-content-card.js";
const detailActivityTip = () => "./components/detail/detail-activity-tip.js";
const _sfc_main = {
  __name: "index",
  setup(__props) {
    common_vendor.onPageScroll(() => {
    });
    const bgColor = {
      bgColor: "#E93323",
      Color: "#fff",
      width: "44rpx",
      timeTxtwidth: "16rpx",
      isDay: true
    };
    const isLogin = common_vendor.computed(() => sheep_index.sheep.$store("user").isLogin);
    let state = common_vendor.reactive({
      goodsId: 0,
      skeletonLoading: true,
      // SPU 加载中
      goodsInfo: {},
      // SPU 信息
      showSelectSku: false,
      // 是否展示 SKU 选择弹窗
      selectedSku: {},
      // 选中的 SKU
      settlementSku: {},
      // 结算的 SKU：由于 selectedSku 不进行默认选中，所以初始使用结算价格最低的 SKU 作为基础展示
      showModel: false,
      // 是否展示 Coupon 优惠劵的弹窗
      couponInfo: [],
      // 可领取的 Coupon 优惠劵的列表
      showActivityModel: false,
      // 【满减送/限时折扣】是否展示 Activity 营销活动的弹窗
      rewardActivity: {},
      // 【满减送】活动
      activityList: []
      // 【秒杀/拼团/砍价】可参与的 Activity 营销活动的列表
    });
    function onSkuChange(e) {
      state.selectedSku = e;
      state.settlementSku = e;
    }
    function onAddCart(e) {
      if (!e.id) {
        sheep_index.sheep.$helper.toast("请选择商品规格");
        return;
      }
      sheep_index.sheep.$store("cart").add(e);
    }
    function onBuy(e) {
      if (!e.id) {
        sheep_index.sheep.$helper.toast("请选择商品规格");
        return;
      }
      sheep_index.sheep.$router.go("/pages/order/confirm", {
        data: JSON.stringify({
          items: [
            {
              skuId: e.id,
              count: e.goods_num,
              categoryId: state.goodsInfo.categoryId
            }
          ]
        })
      });
    }
    function onOpenActivity() {
      state.showActivityModel = true;
    }
    async function onTakeCoupon(id) {
      const { code } = await sheep_api_promotion_coupon.CouponApi.takeCoupon(id);
      if (code !== 0) {
        return;
      }
      common_vendor.index.showToast({
        title: "领取成功"
      });
      setTimeout(() => {
        getCoupon();
      }, 1e3);
    }
    const shareInfo = common_vendor.computed(() => {
      if (common_vendor.isEmpty(state.goodsInfo))
        return {};
      return sheep_index.sheep.$platform.share.getShareInfo(
        {
          title: state.goodsInfo.name,
          image: sheep_index.sheep.$url.cdn(state.goodsInfo.picUrl),
          desc: state.goodsInfo.introduction,
          params: {
            page: sheep_helper_const.SharePageEnum.GOODS.value,
            query: state.goodsInfo.id
          }
        },
        {
          type: "goods",
          // 商品海报
          title: state.goodsInfo.name,
          // 商品名称
          image: sheep_index.sheep.$url.cdn(state.goodsInfo.picUrl),
          // 商品主图
          price: sheep_hooks_useGoods.fen2yuan(state.goodsInfo.price),
          // 商品价格
          original_price: sheep_hooks_useGoods.fen2yuan(state.goodsInfo.marketPrice)
          // 商品原价
        }
      );
    });
    async function getCoupon() {
      const { code, data } = await sheep_api_promotion_coupon.CouponApi.getCouponTemplateList(state.goodsId, 2, 10);
      if (code === 0) {
        state.couponInfo = data;
      }
    }
    async function getSettlementByIds(ids) {
      let { data, code } = await sheep_api_trade_order.OrderApi.getSettlementProduct(ids);
      if (code !== 0 || data.length !== 1) {
        return;
      }
      data = data[0];
      state.goodsInfo.skus.forEach((sku) => {
        data.skus.forEach((item) => {
          if (sku.id === item.id) {
            sku.promotionType = item.promotionType;
            sku.promotionPrice = item.promotionPrice;
            sku.promotionId = item.promotionId;
            sku.promotionEndTime = item.promotionEndTime;
          }
        });
      });
      state.settlementSku = state.goodsInfo.skus.filter((sku) => sku.stock > 0 && sku.promotionPrice > 0).reduce((prev, curr) => prev.promotionPrice < curr.promotionPrice ? prev : curr, []);
      if (data.rewardActivity) {
        state.rewardActivity = data.rewardActivity;
        getActivityTime(state.rewardActivity.id);
      }
    }
    async function getActivityTime(id) {
      const { code, data } = await sheep_api_promotion_rewardActivity.RewardActivityApi.getRewardActivity(id);
      if (code === 0) {
        state.rewardActivity.startTime = data.startTime;
        state.rewardActivity.endTime = data.endTime;
      }
    }
    common_vendor.onLoad((options) => {
      if (!options.id) {
        state.goodsInfo = null;
        state.skeletonLoading = false;
        return;
      }
      state.goodsId = options.id;
      sheep_api_product_spu.SpuApi.getSpuDetail(state.goodsId).then((res) => {
        if (res.code !== 0 || !res.data) {
          state.goodsInfo = null;
          state.skeletonLoading = false;
          return;
        }
        state.skeletonLoading = false;
        state.goodsInfo = res.data;
        getSettlementByIds(state.goodsId);
        if (isLogin.value) {
          sheep_api_product_favorite.FavoriteApi.isFavoriteExists(state.goodsId, "goods").then((res2) => {
            if (res2.code !== 0) {
              return;
            }
            state.goodsInfo.favorite = res2.data;
          });
        }
      });
      getCoupon();
      sheep_api_promotion_activity.ActivityApi.getActivityListBySpuId(state.goodsId).then((res) => {
        if (res.code !== 0) {
          return;
        }
        state.activityList = res.data;
      });
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(state).skeletonLoading
      }, common_vendor.unref(state).skeletonLoading ? {} : common_vendor.unref(state).goodsInfo === null ? {
        c: common_vendor.p({
          text: "商品不存在或已下架",
          icon: "/static/soldout-empty.png",
          showAction: true,
          actionText: "再逛逛",
          actionUrl: "/pages/goods/list"
        })
      } : common_vendor.e({
        d: common_vendor.p({
          isPreview: true,
          list: common_vendor.unref(sheep_hooks_useGoods.formatGoodsSwiper)(common_vendor.unref(state).goodsInfo.sliderPicUrls),
          otStyle: "tag",
          imageMode: "widthFix",
          dotCur: "bg-mask-40",
          seizeHeight: 750
        }),
        e: common_vendor.unref(state).settlementSku && common_vendor.unref(state).settlementSku.id && common_vendor.unref(state).settlementSku.promotionPrice
      }, common_vendor.unref(state).settlementSku && common_vendor.unref(state).settlementSku.id && common_vendor.unref(state).settlementSku.promotionPrice ? common_vendor.e({
        f: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/dis.png"),
        g: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(common_vendor.unref(state).settlementSku.promotionPrice)),
        h: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(common_vendor.unref(state).settlementSku.price - common_vendor.unref(state).settlementSku.promotionPrice)),
        i: common_vendor.unref(state).settlementSku.promotionType === 4
      }, common_vendor.unref(state).settlementSku.promotionType === 4 ? {} : common_vendor.unref(state).settlementSku.promotionType === 6 ? {} : {}, {
        j: common_vendor.unref(state).settlementSku.promotionType === 6,
        k: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(common_vendor.unref(state).settlementSku.price)),
        l: common_vendor.t(common_vendor.unref(state).settlementSku.stock),
        m: common_vendor.unref(state).settlementSku.promotionEndTime > 0
      }, common_vendor.unref(state).settlementSku.promotionEndTime > 0 ? {
        n: common_vendor.p({
          tipText: " ",
          bgColor,
          dayText: ":",
          hourText: ":",
          minuteText: ":",
          secondText: " ",
          datatime: common_vendor.unref(state).settlementSku.promotionEndTime / 1e3,
          isDay: false
        })
      } : {}) : {}, {
        o: !common_vendor.unref(state).settlementSku.promotionPrice
      }, !common_vendor.unref(state).settlementSku.promotionPrice ? common_vendor.e({
        p: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(common_vendor.unref(state).selectedSku.price || common_vendor.unref(state).goodsInfo.price)),
        q: common_vendor.unref(state).goodsInfo.marketPrice > common_vendor.unref(state).goodsInfo.price
      }, common_vendor.unref(state).goodsInfo.marketPrice > common_vendor.unref(state).goodsInfo.price ? {
        r: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(common_vendor.unref(state).selectedSku.marketPrice || common_vendor.unref(state).goodsInfo.marketPrice))
      } : {}, {
        s: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatSales)("exact", common_vendor.unref(state).goodsInfo.salesCount))
      }) : {}, {
        t: common_vendor.f(common_vendor.unref(state).couponInfo.slice(0, 1), (coupon, k0, i0) => {
          return {
            a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuanSimple)(coupon.usePrice)),
            b: common_vendor.t(coupon.discountType === 1 ? "减" + common_vendor.unref(sheep_hooks_useGoods.fen2yuanSimple)(coupon.discountPrice) + "元" : "打" + common_vendor.unref(sheep_hooks_useGoods.formatDiscountPercent)(coupon.discountPercent) + "折"),
            c: coupon.id,
            d: common_vendor.o(onOpenActivity, coupon.id)
          };
        }),
        v: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleItemDescriptions)(common_vendor.unref(state).rewardActivity).slice(0, 3 - common_vendor.unref(state).couponInfo.slice(0, 1).length), (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item,
            c: common_vendor.o(onOpenActivity, item)
          };
        }),
        w: common_vendor.unref(state).couponInfo.length
      }, common_vendor.unref(state).couponInfo.length ? {
        x: common_vendor.o(onOpenActivity, "2f")
      } : {}, {
        y: common_vendor.t(common_vendor.unref(state).goodsInfo.name),
        z: common_vendor.t(common_vendor.unref(state).goodsInfo.introduction),
        A: common_vendor.o(($event) => common_vendor.unref(state).showSelectSku = true, "1e"),
        B: common_vendor.o(($event) => common_vendor.unref(state).selectedSku.goods_sku_text = $event, "96"),
        C: common_vendor.p({
          sku: common_vendor.unref(state).selectedSku,
          modelValue: common_vendor.unref(state).selectedSku.goods_sku_text
        }),
        D: common_vendor.o(onAddCart, "d8"),
        E: common_vendor.o(onBuy, "a0"),
        F: common_vendor.o(onSkuChange, "4b"),
        G: common_vendor.o(($event) => common_vendor.unref(state).showSelectSku = false, "62"),
        H: common_vendor.p({
          goodsInfo: common_vendor.unref(state).goodsInfo,
          show: common_vendor.unref(state).showSelectSku
        }),
        I: common_vendor.p({
          goodsId: common_vendor.unref(state).goodsId
        }),
        J: common_vendor.p({
          content: common_vendor.unref(state).goodsInfo.description
        }),
        K: common_vendor.unref(state).activityList.length > 0
      }, common_vendor.unref(state).activityList.length > 0 ? {
        L: common_vendor.p({
          ["activity-list"]: common_vendor.unref(state).activityList
        })
      } : {}, {
        M: common_vendor.unref(state).goodsInfo.stock > 0
      }, common_vendor.unref(state).goodsInfo.stock > 0 ? {
        N: common_vendor.o(($event) => common_vendor.unref(state).showSelectSku = true, "e2"),
        O: common_vendor.o(($event) => common_vendor.unref(state).showSelectSku = true, "89")
      } : {}, {
        P: common_vendor.o(($event) => common_vendor.unref(state).goodsInfo = $event, "0c"),
        Q: common_vendor.p({
          modelValue: common_vendor.unref(state).goodsInfo
        }),
        R: common_vendor.o(($event) => common_vendor.unref(state).showActivityModel = false, "40"),
        S: common_vendor.o(onTakeCoupon, "90"),
        T: common_vendor.o(($event) => common_vendor.isRef(state) ? state.value = $event : state = $event, "05"),
        U: common_vendor.p({
          show: common_vendor.unref(state).showActivityModel,
          modelValue: common_vendor.unref(state)
        })
      }), {
        b: common_vendor.unref(state).goodsInfo === null,
        V: common_vendor.p({
          onShareAppMessage: shareInfo.value,
          navbar: "goods"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-24a31e85"]]);
_sfc_main.__runtimeHooks = 3;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/goods/index.js.map
