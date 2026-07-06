"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_api_product_spu = require("../../api/product/spu.js");
const sheep_helper_const = require("../../helper/const.js");
const sheep_helper_utils = require("../../helper/utils.js");
if (!Array) {
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  _easycom_s_goods_column2();
}
const _easycom_s_goods_column = () => "../s-goods-column/s-goods-column.js";
if (!Math) {
  _easycom_s_goods_column();
}
const _sfc_main = {
  __name: "s-point-card",
  props: {
    property: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props, { expose: __expose }) {
    const LayoutTypeEnum = {
      // 单列大图
      ONE_COL_BIG_IMG: "oneColBigImg",
      // 双列
      TWO_COL: "twoCol",
      // 单列小图
      ONE_COL_SMALL_IMG: "oneColSmallImg"
    };
    const state = common_vendor.reactive({
      spuList: [],
      leftSpuList: [],
      rightSpuList: [],
      property: {
        layoutType: "oneColBigImg",
        fields: {
          name: {
            show: true,
            color: "#000"
          },
          introduction: {
            show: true,
            color: "#999"
          },
          price: {
            show: true,
            color: "#ff3000"
          },
          marketPrice: {
            show: true,
            color: "#c4c4c4"
          },
          salesCount: {
            show: true,
            color: "#c4c4c4"
          },
          stock: {
            show: true,
            color: "#c4c4c4"
          }
        },
        badge: {
          show: false,
          imgUrl: ""
        },
        btnBuy: {
          type: "text",
          text: "立即兑换",
          bgBeginColor: "#FF6000",
          bgEndColor: "#FE832A",
          imgUrl: ""
        },
        borderRadiusTop: 8,
        borderRadiusBottom: 8,
        space: 8,
        style: {
          bgType: "color",
          bgColor: "",
          marginLeft: 8,
          marginRight: 8,
          marginBottom: 8
        }
      }
    });
    const props = __props;
    common_vendor.watch(
      () => props.property,
      (newVal) => {
        state.property = { ...state.property, ...newVal };
      },
      { immediate: true, deep: true }
    );
    const { marginLeft, marginRight } = state.property.styles || {};
    const buyStyle = common_vendor.computed(() => {
      if (state.property.btnBuy.type === "text") {
        return {
          background: `linear-gradient(to right, ${state.property.btnBuy.bgBeginColor}, ${state.property.btnBuy.bgEndColor})`
        };
      }
      if (state.property.btnBuy.type === "img") {
        return {
          width: "54rpx",
          height: "54rpx",
          background: `url(${sheep_index.sheep.$url.cdn(state.property.btnBuy.imgUrl)}) no-repeat`,
          backgroundSize: "100% 100%"
        };
      }
    });
    let count = 0;
    let leftHeight = 0;
    let rightHeight = 0;
    function calculateGoodsColumn(height = 0, where = "left") {
      if (!state.spuList[count])
        return;
      if (where === "left")
        leftHeight += height;
      if (where === "right")
        rightHeight += height;
      if (leftHeight <= rightHeight) {
        state.leftSpuList.push(state.spuList[count]);
      } else {
        state.rightSpuList.push(state.spuList[count]);
      }
      count++;
    }
    async function getSpuDetail(ids) {
      const { data: spu } = await sheep_api_product_spu.SpuApi.getSpuDetail(ids);
      return spu;
    }
    async function concatActivity(list) {
      if (sheep_helper_utils.isEmpty(list)) {
        return;
      }
      for (const activity of list) {
        state.spuList.push(await getSpuDetail(activity.spuId));
      }
      list.forEach((activity) => {
        const spu = state.spuList.find((spu2) => activity.spuId === spu2.id);
        if (spu) {
          spu.pointStock = activity.stock;
          spu.pointTotalStock = activity.totalStock;
          spu.point = activity.point;
          spu.pointPrice = activity.price;
          spu.activityId = activity.id;
          spu.activityType = sheep_helper_const.PromotionActivityTypeEnum.POINT.type;
        }
      });
      if (state.property.layoutType === LayoutTypeEnum.TWO_COL) {
        calculateGoodsColumn();
      }
    }
    function getActivityCount() {
      return state.spuList.length;
    }
    __expose({ concatActivity, getActivityCount, calculateGoodsColumn });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.property.layoutType === LayoutTypeEnum.ONE_COL_BIG_IMG && state.spuList.length
      }, state.property.layoutType === LayoutTypeEnum.ONE_COL_BIG_IMG && state.spuList.length ? {
        b: common_vendor.f(state.spuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: "6f3826f8-0-" + i0,
            c: common_vendor.p({
              size: "sl",
              goodsFields: state.property.fields,
              tagStyle: state.property.badge,
              data: item,
              titleColor: (_a = state.property.fields.name) == null ? void 0 : _a.color,
              subTitleColor: state.property.fields.introduction.color,
              topRadius: state.property.borderRadiusTop,
              bottomRadius: state.property.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        c: common_vendor.t(state.property.btnBuy.type === "text" ? state.property.btnBuy.text : ""),
        d: common_vendor.s(buyStyle.value),
        e: common_vendor.s({
          marginBottom: state.property.space * 2 + "rpx"
        })
      } : {}, {
        f: state.property.layoutType === LayoutTypeEnum.ONE_COL_SMALL_IMG && state.spuList.length
      }, state.property.layoutType === LayoutTypeEnum.ONE_COL_SMALL_IMG && state.spuList.length ? {
        g: common_vendor.f(state.spuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: "6f3826f8-1-" + i0,
            c: common_vendor.p({
              size: "lg",
              goodsFields: state.property.fields,
              data: item,
              tagStyle: state.property.badge,
              titleColor: (_a = state.property.fields.name) == null ? void 0 : _a.color,
              subTitleColor: state.property.fields.introduction.color,
              topRadius: state.property.borderRadiusTop,
              bottomRadius: state.property.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        h: common_vendor.t(state.property.btnBuy.type === "text" ? state.property.btnBuy.text : ""),
        i: common_vendor.s(buyStyle.value),
        j: common_vendor.s({
          marginBottom: state.property.space + "px"
        })
      } : {}, {
        k: state.property.layoutType === LayoutTypeEnum.TWO_COL && state.spuList.length
      }, state.property.layoutType === LayoutTypeEnum.TWO_COL && state.spuList.length ? {
        l: common_vendor.f(state.leftSpuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: common_vendor.o(($event) => calculateGoodsColumn($event, "left"), item.id),
            c: "6f3826f8-2-" + i0,
            d: common_vendor.p({
              size: "md",
              goodsFields: state.property.fields,
              tagStyle: state.property.badge,
              data: item,
              titleColor: (_a = state.property.fields.name) == null ? void 0 : _a.color,
              subTitleColor: state.property.fields.introduction.color,
              topRadius: state.property.borderRadiusTop,
              bottomRadius: state.property.borderRadiusBottom,
              titleWidth: 330 - common_vendor.unref(marginLeft) - common_vendor.unref(marginRight)
            }),
            e: item.id
          };
        }),
        m: common_vendor.t(state.property.btnBuy.type === "text" ? state.property.btnBuy.text : ""),
        n: common_vendor.s(buyStyle.value),
        o: common_vendor.s({
          paddingRight: state.property.space + "rpx",
          marginBottom: state.property.space + "px"
        }),
        p: common_vendor.f(state.rightSpuList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/point", {
              id: item.activityId
            }), item.id),
            b: common_vendor.o(($event) => calculateGoodsColumn($event, "right"), item.id),
            c: "6f3826f8-3-" + i0,
            d: common_vendor.p({
              size: "md",
              goodsFields: state.property.fields,
              tagStyle: state.property.badge,
              data: item,
              titleColor: (_a = state.property.fields.name) == null ? void 0 : _a.color,
              subTitleColor: state.property.fields.introduction.color,
              topRadius: state.property.borderRadiusTop,
              bottomRadius: state.property.borderRadiusBottom,
              titleWidth: 330 - common_vendor.unref(marginLeft) - common_vendor.unref(marginRight)
            }),
            e: item.id
          };
        }),
        q: common_vendor.t(state.property.btnBuy.type === "text" ? state.property.btnBuy.text : ""),
        r: common_vendor.s(buyStyle.value),
        s: common_vendor.s({
          paddingLeft: state.property.space + "rpx",
          marginBottom: state.property.space + "px"
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-6f3826f8"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-point-card/s-point-card.js.map
