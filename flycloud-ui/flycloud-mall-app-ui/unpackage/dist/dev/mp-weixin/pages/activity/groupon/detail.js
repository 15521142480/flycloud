"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_hooks_useModal = require("../../../sheep/hooks/useModal.js");
const sheep_api_promotion_combination = require("../../../sheep/api/promotion/combination.js");
const sheep_api_product_spu = require("../../../sheep/api/product/spu.js");
const sheep_helper_const = require("../../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_s_select_groupon_sku2 = common_vendor.resolveComponent("s-select-groupon-sku");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_s_select_groupon_sku2 + _easycom_s_empty2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_s_select_groupon_sku = () => "../../../sheep/components/s-select-groupon-sku/s-select-groupon-sku.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + _easycom_s_select_groupon_sku + _easycom_s_empty + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "detail",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "37930634": common_vendor.unref(headerBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/withdraw_bg.png");
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const state = common_vendor.reactive({
      data: {},
      // 拼团详情
      goodsId: 0,
      // 商品ID
      goodsInfo: {},
      // 商品信息
      showSelectSku: false,
      // 显示规格弹框
      selectedSkuPrice: {},
      // 选中的规格价格
      activity: {},
      // 团购活动
      grouponId: 0,
      // 团购ID
      grouponNum: 0,
      // 团购人数
      grouponAction: "create",
      // 团购操作
      combinationHeadId: null,
      // 拼团团长编号
      loading: true
    });
    const shareInfo = common_vendor.computed(() => {
      var _a;
      if (common_vendor.isEmpty(state.data))
        return {};
      return sheep_index.sheep.$platform.share.getShareInfo(
        {
          title: state.data.headRecord.spuName,
          image: sheep_index.sheep.$url.cdn(state.data.headRecord.picUrl),
          desc: (_a = state.data.goods) == null ? void 0 : _a.subtitle,
          params: {
            page: sheep_helper_const.SharePageEnum.GROUPON_DETAIL.value,
            query: state.data.headRecord.id
          }
        },
        {
          type: "groupon",
          // 邀请拼团海报
          title: state.data.headRecord.spuName,
          // 商品标题
          image: sheep_index.sheep.$url.cdn(state.data.headRecord.picUrl),
          // 商品主图
          price: sheep_hooks_useGoods.fen2yuan(state.data.headRecord.combinationPrice),
          // 商品价格
          grouponNum: state.data.headRecord.userSize
          // 拼团人数
        }
      );
    });
    function onDetail(orderId) {
      sheep_index.sheep.$router.go("/pages/order/detail", {
        id: orderId
      });
    }
    function onCreateGroupon() {
      state.grouponAction = "create";
      state.grouponId = 0;
      state.showSelectSku = true;
    }
    function onSkuChange(e) {
      state.selectedSkuPrice = e;
    }
    function onJoinGroupon() {
      state.grouponAction = "join";
      state.grouponId = state.data.headRecord.activityId;
      state.combinationHeadId = state.data.headRecord.id;
      state.grouponNum = state.data.headRecord.userSize;
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
    const endTime = common_vendor.computed(() => {
      return sheep_hooks_useGoods.useDurationTime(state.data.headRecord.expireTime);
    });
    async function getGrouponDetail(id) {
      const { code, data } = await sheep_api_promotion_combination.CombinationApi.getCombinationRecordDetail(id);
      if (code === 0) {
        state.data = data;
        const remainNumber = Number(state.data.headRecord.userSize - state.data.headRecord.userCount);
        state.remainNumber = remainNumber > 0 ? remainNumber : 0;
        const { data: activity } = await sheep_api_promotion_combination.CombinationApi.getCombinationActivity(
          data.headRecord.activityId
        );
        state.activity = activity;
        state.grouponNum = activity.userSize;
        const { data: spu } = await sheep_api_product_spu.SpuApi.getSpuDetail(activity.spuId);
        state.goodsId = spu.id;
        activity.products.forEach((product) => {
          spu.price = Math.min(spu.price, product.combinationPrice);
        });
        state.goodsInfo = spu;
        spu.skus.forEach((sku) => {
          const product = activity.products.find((product2) => product2.skuId === sku.id);
          if (product) {
            sku.price = product.combinationPrice;
          } else {
            sku.stock = 0;
          }
        });
      } else {
        state.data = null;
      }
      state.loading = false;
    }
    function onShare() {
      sheep_hooks_useModal.showShareModal();
    }
    common_vendor.onLoad((options) => {
      getGrouponDetail(options.id);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.loading
      }, state.loading ? {} : {}, {
        b: state.data && !state.loading
      }, state.data && !state.loading ? common_vendor.e({
        c: state.data.headRecord
      }, state.data.headRecord ? {
        d: common_vendor.t(state.data.headRecord.userSize),
        e: common_vendor.t(state.data.headRecord.userCount),
        f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/groupon", {
          id: state.data.headRecord.activityId
        }), "4b"),
        g: common_vendor.s({
          top: Number(statusBarHeight + 108) + "rpx"
        }),
        h: common_vendor.p({
          img: state.data.headRecord.picUrl,
          title: state.data.headRecord.spuName,
          price: state.data.headRecord.combinationPrice,
          priceColor: "#E1212B"
        }),
        i: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 108) + "rpx"
        })
      } : {}, {
        j: state.data.headRecord.status === 1
      }, state.data.headRecord.status === 1 ? common_vendor.e({
        k: state.data.orderId
      }, state.data.orderId ? {} : {}) : {}, {
        l: state.data.headRecord.status === 2
      }, state.data.headRecord.status === 2 ? {
        m: common_vendor.t(state.data.orderId ? "拼团超时,已自动退款" : "该团已解散")
      } : {}, {
        n: state.data.headRecord.status === 0
      }, state.data.headRecord.status === 0 ? common_vendor.e({
        o: state.data.headRecord.expireTime <= (/* @__PURE__ */ new Date()).getTime()
      }, state.data.headRecord.expireTime <= (/* @__PURE__ */ new Date()).getTime() ? {} : {
        p: common_vendor.t(state.data.headRecord.userSize - state.data.headRecord.userCount),
        q: common_vendor.t(endTime.value.h),
        r: common_vendor.t(endTime.value.m),
        s: common_vendor.t(endTime.value.s)
      }) : {}, {
        t: common_vendor.unref(sheep_index.sheep).$url.cdn(state.data.headRecord.avatar) || common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/default_avatar.png"),
        v: common_vendor.f(state.data.memberRecords, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.avatar) || common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/default_avatar.png"),
            b: item.is_leader == "1"
          }, item.is_leader == "1" ? {} : {}, {
            c: item.id
          });
        }),
        w: common_vendor.f(state.remainNumber, (item, k0, i0) => {
          return {
            a: item
          };
        }),
        x: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/avatar/unknown.png"),
        y: state.data.headRecord.status === 1 || state.data.headRecord.status === 2
      }, state.data.headRecord.status === 1 || state.data.headRecord.status === 2 ? common_vendor.e({
        z: state.data.orderId
      }, state.data.orderId ? {
        A: common_vendor.o(($event) => onDetail(state.data.orderId), "59")
      } : {
        B: common_vendor.o(onCreateGroupon, "67")
      }) : {}, {
        C: state.data.headRecord.status === 0
      }, state.data.headRecord.status === 0 ? common_vendor.e({
        D: state.data.headRecord.expireTime <= (/* @__PURE__ */ new Date()).getTime()
      }, state.data.headRecord.expireTime <= (/* @__PURE__ */ new Date()).getTime() ? common_vendor.e({
        E: state.data.orderId
      }, state.data.orderId ? {
        F: common_vendor.o(($event) => onDetail(state.data.orderId), "fd")
      } : {
        G: common_vendor.o(($event) => onDetail(state.data.orderId), "dd")
      }) : common_vendor.e({
        H: state.data.orderId
      }, state.data.orderId ? {
        I: endTime.value.ms <= 0,
        J: common_vendor.o(onShare, "5f")
      } : {
        K: endTime.value.ms <= 0,
        L: common_vendor.o(($event) => onJoinGroupon(), "c0")
      })) : {}, {
        M: !common_vendor.unref(common_vendor.isEmpty)(state.goodsInfo)
      }, !common_vendor.unref(common_vendor.isEmpty)(state.goodsInfo) ? {
        N: common_vendor.o(onBuy, "2f"),
        O: common_vendor.o(onSkuChange, "c1"),
        P: common_vendor.o(($event) => state.showSelectSku = false, "cd"),
        Q: common_vendor.p({
          show: state.showSelectSku,
          goodsInfo: state.goodsInfo,
          grouponAction: state.grouponAction,
          grouponNum: state.grouponNum
        })
      } : {}) : {}, {
        R: !state.data && !state.loading
      }, !state.data && !state.loading ? {
        S: common_vendor.p({
          icon: "/static/goods-empty.png"
        })
      } : {}, {
        T: common_vendor.s(_ctx.__cssVars()),
        U: common_vendor.p({
          title: "拼团详情",
          navbar: state.data && !state.loading ? "inner" : "normal",
          onShareAppMessage: shareInfo.value
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-f4b0e588"]]);
_sfc_main.__runtimeHooks = 2;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/activity/groupon/detail.js.map
