"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
const sheep_helper_const = require("../../helper/const.js");
const _sfc_main = {
  __name: "s-goods-column",
  props: {
    goodsFields: {
      type: [Array, Object],
      default() {
        return {
          // 商品价格
          price: {
            show: true
          },
          // 库存
          stock: {
            show: true
          },
          // 商品名称
          name: {
            show: true
          },
          // 商品介绍
          introduction: {
            show: true
          },
          // 市场价
          marketPrice: {
            show: true
          },
          // 销量
          salesCount: {
            show: true
          }
        };
      }
    },
    tagStyle: {
      type: Object,
      default: () => ({})
    },
    data: {
      type: Object,
      default: () => ({})
    },
    size: {
      type: String,
      default: "sl"
    },
    background: {
      type: String,
      default: ""
    },
    topRadius: {
      type: Number,
      default: 0
    },
    bottomRadius: {
      type: Number,
      default: 0
    },
    titleWidth: {
      type: Number,
      default: 0
    },
    titleColor: {
      type: String,
      default: "#333"
    },
    priceColor: {
      type: String,
      default: ""
    },
    originPriceColor: {
      type: String,
      default: "#C4C4C4"
    },
    priceUnit: {
      type: String,
      default: "￥"
    },
    subTitleColor: {
      type: String,
      default: "#999999"
    },
    subTitleBackground: {
      type: String,
      default: ""
    },
    buttonShow: {
      type: Boolean,
      default: true
    },
    seckillTag: {
      type: Boolean,
      default: false
    },
    grouponTag: {
      type: Boolean,
      default: false
    }
  },
  emits: ["click", "getHeight"],
  setup(__props, { emit: __emit }) {
    let defaultImgWidth = common_vendor.ref(0);
    const props = __props;
    const discountText = common_vendor.computed(() => {
      const promotionType = props.data.promotionType;
      if (promotionType === 4) {
        return "限时优惠";
      } else if (promotionType === 6) {
        return "会员价";
      }
      return void 0;
    });
    const elStyles = common_vendor.computed(() => {
      return {
        background: props.background,
        "border-top-left-radius": props.topRadius + "px",
        "border-top-right-radius": props.topRadius + "px",
        "border-bottom-left-radius": props.bottomRadius + "px",
        "border-bottom-right-radius": props.bottomRadius + "px"
      };
    });
    const salesAndStock = common_vendor.computed(() => {
      var _a, _b;
      let text = [];
      if ((_a = props.goodsFields.salesCount) == null ? void 0 : _a.show) {
        if (props.data.activityType && props.data.activityType === sheep_helper_const.PromotionActivityTypeEnum.POINT.type) {
          text.push(
            sheep_hooks_useGoods.formatExchange(
              props.data.sales_show_type,
              (props.data.pointTotalStock || 0) - (props.data.pointStock || 0)
            )
          );
        } else {
          text.push(sheep_hooks_useGoods.formatSales(props.data.sales_show_type, props.data.salesCount));
        }
      }
      if ((_b = props.goodsFields.stock) == null ? void 0 : _b.show) {
        if (props.data.activityType && props.data.activityType === sheep_helper_const.PromotionActivityTypeEnum.POINT.type) {
          text.push(sheep_hooks_useGoods.formatStock(props.data.stock_show_type, props.data.pointTotalStock));
        } else {
          text.push(sheep_hooks_useGoods.formatStock(props.data.stock_show_type, props.data.stock));
        }
      }
      return text.join(" | ");
    });
    const emits = __emit;
    const onClick = () => {
      emits("click");
    };
    const { proxy } = common_vendor.getCurrentInstance();
    const elId = `sheep_${Math.ceil(Math.random() * 1e6).toString(36)}`;
    function getGoodsPriceCardWH() {
      if (props.size === "md") {
        const view = common_vendor.index.createSelectorQuery().in(proxy);
        view.select(`#${elId}`).fields({
          size: true,
          scrollOffset: true
        });
        view.exec((data) => {
          common_vendor.index.__f__("log", "at sheep/components/s-goods-column/s-goods-column.vue:681", data, "data");
          let totalHeight = 0;
          const goodsPriceCard = data[0];
          defaultImgWidth.value = data[0].width;
          if (props.data.image_wh && Number(props.data.image_wh.w)) {
            totalHeight = goodsPriceCard.width / props.data.image_wh.w * props.data.image_wh.h + goodsPriceCard.height;
          } else {
            totalHeight = goodsPriceCard.width + goodsPriceCard.height;
          }
          emits("getHeight", totalHeight);
        });
      }
    }
    common_vendor.onMounted(() => {
      common_vendor.nextTick$1(() => {
        getGoodsPriceCardWH();
      });
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e, _f, _g, _h, _i, _j, _k, _l, _m, _n, _o, _p, _q, _r, _s, _t, _u, _v, _w, _x, _y, _z, _A, _B, _C, _D, _E, _F, _G, _H, _I, _J, _K, _L, _M, _N, _O, _P, _Q, _R, _S, _T, _U, _V, _W, _X, _Y, _Z, __, _$, _aa, _ba, _ca, _da, _ea, _fa, _ga, _ha, _ia, _ja, _ka, _la, _ma, _na, _oa, _pa, _qa, _ra;
      return common_vendor.e({
        a: __props.size === "xs"
      }, __props.size === "xs" ? common_vendor.e({
        b: __props.tagStyle.show
      }, __props.tagStyle.show ? {
        c: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.tagStyle.src || __props.tagStyle.imgUrl)
      } : {}, {
        d: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.image || __props.data.picUrl),
        e: ((_a = __props.goodsFields.title) == null ? void 0 : _a.show) || ((_b = __props.goodsFields.name) == null ? void 0 : _b.show) || ((_c = __props.goodsFields.price) == null ? void 0 : _c.show)
      }, ((_d = __props.goodsFields.title) == null ? void 0 : _d.show) || ((_e = __props.goodsFields.name) == null ? void 0 : _e.show) || ((_f = __props.goodsFields.price) == null ? void 0 : _f.show) ? common_vendor.e({
        f: ((_g = __props.goodsFields.title) == null ? void 0 : _g.show) || ((_h = __props.goodsFields.name) == null ? void 0 : _h.show)
      }, ((_i = __props.goodsFields.title) == null ? void 0 : _i.show) || ((_j = __props.goodsFields.name) == null ? void 0 : _j.show) ? {
        g: common_vendor.t(__props.data.title || __props.data.name),
        h: common_vendor.s({
          color: __props.titleColor,
          width: __props.titleWidth ? __props.titleWidth + "rpx" : ""
        })
      } : {}, {
        i: __props.data.promotionType > 0 || __props.data.rewardActivity
      }, __props.data.promotionType > 0 || __props.data.rewardActivity ? common_vendor.e({
        j: discountText.value
      }, discountText.value ? {
        k: common_vendor.t(discountText.value)
      } : {}, {
        l: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleItemDescriptions)(__props.data.rewardActivity).slice(0, 1), (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item
          };
        })
      }) : {}, {
        m: (_k = __props.goodsFields.price) == null ? void 0 : _k.show
      }, ((_l = __props.goodsFields.price) == null ? void 0 : _l.show) ? common_vendor.e({
        n: __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type
      }, __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? {
        o: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        p: common_vendor.t(__props.data.point),
        q: common_vendor.t(!__props.data.pointPrice || __props.data.pointPrice === 0 ? "" : `+${__props.priceUnit}${common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.pointPrice)}`)
      } : common_vendor.e({
        r: common_vendor.t(__props.priceUnit),
        s: __props.data.promotionPrice > 0
      }, __props.data.promotionPrice > 0 ? {
        t: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.promotionPrice))
      } : {
        v: common_vendor.t(common_vendor.unref(common_vendor.isArray)(__props.data.price) ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price[0]) : common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price))
      }), {
        w: common_vendor.s({
          color: __props.goodsFields.price.color
        })
      }) : {}) : {}, {
        x: common_vendor.s(elStyles.value),
        y: common_vendor.o(onClick, "b9")
      }) : {}, {
        z: __props.size === "sm"
      }, __props.size === "sm" ? common_vendor.e({
        A: __props.tagStyle.show
      }, __props.tagStyle.show ? {
        B: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.tagStyle.src || __props.tagStyle.imgUrl)
      } : {}, {
        C: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.image || __props.data.picUrl),
        D: ((_m = __props.goodsFields.title) == null ? void 0 : _m.show) || ((_n = __props.goodsFields.name) == null ? void 0 : _n.show) || ((_o = __props.goodsFields.price) == null ? void 0 : _o.show)
      }, ((_p = __props.goodsFields.title) == null ? void 0 : _p.show) || ((_q = __props.goodsFields.name) == null ? void 0 : _q.show) || ((_r = __props.goodsFields.price) == null ? void 0 : _r.show) ? common_vendor.e({
        E: ((_s = __props.goodsFields.title) == null ? void 0 : _s.show) || ((_t = __props.goodsFields.name) == null ? void 0 : _t.show)
      }, ((_u = __props.goodsFields.title) == null ? void 0 : _u.show) || ((_v = __props.goodsFields.name) == null ? void 0 : _v.show) ? {
        F: common_vendor.t(__props.data.title || __props.data.name)
      } : {}, {
        G: __props.data.promotionType > 0 || __props.data.rewardActivity
      }, __props.data.promotionType > 0 || __props.data.rewardActivity ? common_vendor.e({
        H: discountText.value
      }, discountText.value ? {
        I: common_vendor.t(discountText.value)
      } : {}, {
        J: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleItemDescriptions)(__props.data.rewardActivity).slice(0, 1), (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item
          };
        })
      }) : {}, {
        K: (_w = __props.goodsFields.price) == null ? void 0 : _w.show
      }, ((_x = __props.goodsFields.price) == null ? void 0 : _x.show) ? common_vendor.e({
        L: __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type
      }, __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? {
        M: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        N: common_vendor.t(__props.data.point),
        O: common_vendor.t(!__props.data.pointPrice || __props.data.pointPrice === 0 ? "" : `+${__props.priceUnit}${common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.pointPrice)}`)
      } : common_vendor.e({
        P: common_vendor.t(__props.priceUnit),
        Q: __props.data.promotionPrice > 0
      }, __props.data.promotionPrice > 0 ? {
        R: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.promotionPrice))
      } : {
        S: common_vendor.t(common_vendor.unref(common_vendor.isArray)(__props.data.price) ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price[0]) : common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price))
      }), {
        T: common_vendor.s({
          color: __props.goodsFields.price.color
        })
      }) : {}, {
        U: common_vendor.s({
          color: __props.titleColor,
          width: __props.titleWidth ? __props.titleWidth + "rpx" : ""
        })
      }) : {}, {
        V: common_vendor.s(elStyles.value),
        W: common_vendor.o(onClick, "12")
      }) : {}, {
        X: __props.size === "md"
      }, __props.size === "md" ? common_vendor.e({
        Y: __props.tagStyle.show
      }, __props.tagStyle.show ? {
        Z: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.tagStyle.src || __props.tagStyle.imgUrl)
      } : {}, {
        aa: __props.data.image_wh
      }, __props.data.image_wh ? {
        ab: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.image || __props.data.picUrl)
      } : {
        ac: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.image || __props.data.picUrl),
        ad: common_vendor.s({
          height: common_vendor.unref(defaultImgWidth) * 2 + "rpx"
        })
      }, {
        ae: ((_y = __props.goodsFields.title) == null ? void 0 : _y.show) || ((_z = __props.goodsFields.name) == null ? void 0 : _z.show)
      }, ((_A = __props.goodsFields.title) == null ? void 0 : _A.show) || ((_B = __props.goodsFields.name) == null ? void 0 : _B.show) ? {
        af: common_vendor.t(__props.data.title || __props.data.name),
        ag: common_vendor.s({
          color: __props.titleColor,
          width: __props.titleWidth ? __props.titleWidth + "rpx" : ""
        })
      } : {}, {
        ah: ((_C = __props.goodsFields.subtitle) == null ? void 0 : _C.show) || ((_D = __props.goodsFields.introduction) == null ? void 0 : _D.show)
      }, ((_E = __props.goodsFields.subtitle) == null ? void 0 : _E.show) || ((_F = __props.goodsFields.introduction) == null ? void 0 : _F.show) ? {
        ai: common_vendor.t(__props.data.subtitle || __props.data.introduction),
        aj: common_vendor.s({
          color: __props.subTitleColor,
          background: __props.subTitleBackground
        })
      } : {}, {
        ak: (_G = __props.data.promos) == null ? void 0 : _G.length
      }, ((_H = __props.data.promos) == null ? void 0 : _H.length) ? {
        al: common_vendor.f(__props.data.promos, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.title),
            b: item.id
          };
        })
      } : {}, {
        am: __props.data.promotionType > 0 || __props.data.rewardActivity
      }, __props.data.promotionType > 0 || __props.data.rewardActivity ? common_vendor.e({
        an: discountText.value
      }, discountText.value ? {
        ao: common_vendor.t(discountText.value)
      } : {}, {
        ap: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleItemDescriptions)(__props.data.rewardActivity).slice(0, 1), (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item
          };
        })
      }) : {}, {
        aq: (_I = __props.goodsFields.price) == null ? void 0 : _I.show
      }, ((_J = __props.goodsFields.price) == null ? void 0 : _J.show) ? common_vendor.e({
        ar: __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type
      }, __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? {
        as: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        at: common_vendor.t(__props.data.point),
        av: common_vendor.t(!__props.data.pointPrice || __props.data.pointPrice === 0 ? "" : `+${__props.priceUnit}${common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.pointPrice)}`)
      } : common_vendor.e({
        aw: common_vendor.t(__props.priceUnit),
        ax: __props.data.promotionPrice > 0
      }, __props.data.promotionPrice > 0 ? {
        ay: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.promotionPrice))
      } : {
        az: common_vendor.t(common_vendor.unref(common_vendor.isArray)(__props.data.price) ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price[0]) : common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price))
      }), {
        aA: common_vendor.s({
          color: __props.goodsFields.price.color
        })
      }) : {}, {
        aB: (((_K = __props.goodsFields.original_price) == null ? void 0 : _K.show) || ((_L = __props.goodsFields.marketPrice) == null ? void 0 : _L.show)) && (__props.data.original_price > 0 || __props.data.marketPrice > 0)
      }, (((_M = __props.goodsFields.original_price) == null ? void 0 : _M.show) || ((_N = __props.goodsFields.marketPrice) == null ? void 0 : _N.show)) && (__props.data.original_price > 0 || __props.data.marketPrice > 0) ? {
        aC: common_vendor.t(__props.priceUnit),
        aD: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.marketPrice)),
        aE: common_vendor.s({
          color: __props.originPriceColor
        })
      } : {}, {
        aF: common_vendor.t(salesAndStock.value),
        aG: elId,
        aH: common_assets._imports_0$1,
        aI: common_vendor.s(elStyles.value),
        aJ: common_vendor.o(onClick, "b2")
      }) : {}, {
        aK: __props.size === "lg"
      }, __props.size === "lg" ? common_vendor.e({
        aL: __props.tagStyle.show
      }, __props.tagStyle.show ? {
        aM: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.tagStyle.src || __props.tagStyle.imgUrl)
      } : {}, {
        aN: __props.seckillTag
      }, __props.seckillTag ? {} : {}, {
        aO: __props.grouponTag
      }, __props.grouponTag ? {} : {}, {
        aP: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.image || __props.data.picUrl),
        aQ: ((_O = __props.goodsFields.title) == null ? void 0 : _O.show) || ((_P = __props.goodsFields.name) == null ? void 0 : _P.show)
      }, ((_Q = __props.goodsFields.title) == null ? void 0 : _Q.show) || ((_R = __props.goodsFields.name) == null ? void 0 : _R.show) ? {
        aR: common_vendor.t(__props.data.title || __props.data.name),
        aS: common_vendor.s({
          color: __props.titleColor
        })
      } : {}, {
        aT: ((_S = __props.goodsFields.subtitle) == null ? void 0 : _S.show) || ((_T = __props.goodsFields.introduction) == null ? void 0 : _T.show)
      }, ((_U = __props.goodsFields.subtitle) == null ? void 0 : _U.show) || ((_V = __props.goodsFields.introduction) == null ? void 0 : _V.show) ? {
        aU: common_vendor.t(__props.data.subtitle || __props.data.introduction),
        aV: common_vendor.s({
          color: __props.subTitleColor,
          background: __props.subTitleBackground
        })
      } : {}, {
        aW: (_W = __props.data.promos) == null ? void 0 : _W.length
      }, ((_X = __props.data.promos) == null ? void 0 : _X.length) ? {
        aX: common_vendor.f(__props.data.promos, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.title),
            b: item.id
          };
        })
      } : {}, {
        aY: __props.data.promotionType > 0 || __props.data.rewardActivity
      }, __props.data.promotionType > 0 || __props.data.rewardActivity ? common_vendor.e({
        aZ: discountText.value
      }, discountText.value ? {
        ba: common_vendor.t(discountText.value)
      } : {}, {
        bb: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleItemDescriptions)(__props.data.rewardActivity).slice(0, 1), (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item
          };
        })
      }) : {}, {
        bc: (_Y = __props.goodsFields.price) == null ? void 0 : _Y.show
      }, ((_Z = __props.goodsFields.price) == null ? void 0 : _Z.show) ? common_vendor.e({
        bd: __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type
      }, __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? {
        be: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        bf: common_vendor.t(__props.data.point),
        bg: common_vendor.t(!__props.data.pointPrice || __props.data.pointPrice === 0 ? "" : `+${__props.priceUnit}${common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.pointPrice)}`)
      } : common_vendor.e({
        bh: common_vendor.t(__props.priceUnit),
        bi: __props.data.promotionPrice > 0
      }, __props.data.promotionPrice > 0 ? {
        bj: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.promotionPrice))
      } : {
        bk: common_vendor.t(common_vendor.unref(common_vendor.isArray)(__props.data.price) ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price[0]) : common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price))
      }), {
        bl: common_vendor.s({
          color: __props.goodsFields.price.color
        }),
        bm: (((__ = __props.goodsFields.original_price) == null ? void 0 : __.show) || ((_$ = __props.goodsFields.marketPrice) == null ? void 0 : _$.show)) && (__props.data.original_price > 0 || __props.data.marketPrice > 0)
      }, (((_aa = __props.goodsFields.original_price) == null ? void 0 : _aa.show) || ((_ba = __props.goodsFields.marketPrice) == null ? void 0 : _ba.show)) && (__props.data.original_price > 0 || __props.data.marketPrice > 0) ? {
        bn: common_vendor.t(__props.priceUnit),
        bo: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.marketPrice)),
        bp: common_vendor.s({
          color: __props.originPriceColor
        })
      } : {}) : {}, {
        bq: common_vendor.t(salesAndStock.value),
        br: __props.buttonShow
      }, __props.buttonShow ? {} : {}, {
        bs: common_vendor.s(elStyles.value),
        bt: common_vendor.o(onClick, "9e")
      }) : {}, {
        bv: __props.size === "sl"
      }, __props.size === "sl" ? common_vendor.e({
        bw: __props.tagStyle.show
      }, __props.tagStyle.show ? {
        bx: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.tagStyle.src || __props.tagStyle.imgUrl)
      } : {}, {
        by: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.image || __props.data.picUrl),
        bz: ((_ca = __props.goodsFields.title) == null ? void 0 : _ca.show) || ((_da = __props.goodsFields.name) == null ? void 0 : _da.show)
      }, ((_ea = __props.goodsFields.title) == null ? void 0 : _ea.show) || ((_fa = __props.goodsFields.name) == null ? void 0 : _fa.show) ? {
        bA: common_vendor.t(__props.data.title || __props.data.name),
        bB: common_vendor.s({
          color: __props.titleColor
        })
      } : {}, {
        bC: ((_ga = __props.goodsFields.subtitle) == null ? void 0 : _ga.show) || ((_ha = __props.goodsFields.introduction) == null ? void 0 : _ha.show)
      }, ((_ia = __props.goodsFields.subtitle) == null ? void 0 : _ia.show) || ((_ja = __props.goodsFields.introduction) == null ? void 0 : _ja.show) ? {
        bD: common_vendor.t(__props.data.subtitle || __props.data.introduction),
        bE: common_vendor.s({
          color: __props.subTitleColor,
          background: __props.subTitleBackground
        })
      } : {}, {
        bF: (_ka = __props.data.promos) == null ? void 0 : _ka.length
      }, ((_la = __props.data.promos) == null ? void 0 : _la.length) ? {
        bG: common_vendor.f(__props.data.promos, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.title),
            b: item.id
          };
        })
      } : {}, {
        bH: __props.data.promotionType > 0 || __props.data.rewardActivity
      }, __props.data.promotionType > 0 || __props.data.rewardActivity ? common_vendor.e({
        bI: discountText.value
      }, discountText.value ? {
        bJ: common_vendor.t(discountText.value)
      } : {}, {
        bK: common_vendor.f(common_vendor.unref(sheep_hooks_useGoods.getRewardActivityRuleItemDescriptions)(__props.data.rewardActivity).slice(0, 1), (item, k0, i0) => {
          return {
            a: common_vendor.t(item),
            b: item
          };
        })
      }) : {}, {
        bL: (_ma = __props.goodsFields.price) == null ? void 0 : _ma.show
      }, ((_na = __props.goodsFields.price) == null ? void 0 : _na.show) ? common_vendor.e({
        bM: __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type
      }, __props.data.activityType && __props.data.activityType === common_vendor.unref(sheep_helper_const.PromotionActivityTypeEnum).POINT.type ? {
        bN: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        bO: common_vendor.t(__props.data.point),
        bP: common_vendor.t(!__props.data.pointPrice || __props.data.pointPrice === 0 ? "" : `+${__props.priceUnit}${common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.pointPrice)}`)
      } : common_vendor.e({
        bQ: common_vendor.t(__props.priceUnit),
        bR: __props.data.promotionPrice > 0
      }, __props.data.promotionPrice > 0 ? {
        bS: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.promotionPrice))
      } : {
        bT: common_vendor.t(common_vendor.unref(common_vendor.isArray)(__props.data.price) ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price[0]) : common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.price))
      }), {
        bU: common_vendor.s({
          color: __props.goodsFields.price.color
        }),
        bV: (((_oa = __props.goodsFields.original_price) == null ? void 0 : _oa.show) || ((_pa = __props.goodsFields.marketPrice) == null ? void 0 : _pa.show)) && (__props.data.original_price > 0 || __props.data.marketPrice > 0)
      }, (((_qa = __props.goodsFields.original_price) == null ? void 0 : _qa.show) || ((_ra = __props.goodsFields.marketPrice) == null ? void 0 : _ra.show)) && (__props.data.original_price > 0 || __props.data.marketPrice > 0) ? {
        bW: common_vendor.t(__props.priceUnit),
        bX: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.marketPrice)),
        bY: common_vendor.s({
          color: __props.originPriceColor
        })
      } : {}) : {}, {
        bZ: common_vendor.t(salesAndStock.value),
        ca: common_vendor.s(elStyles.value),
        cb: common_vendor.o(onClick, "f9")
      }) : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1c3d9e07"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-goods-column/s-goods-column.js.map
