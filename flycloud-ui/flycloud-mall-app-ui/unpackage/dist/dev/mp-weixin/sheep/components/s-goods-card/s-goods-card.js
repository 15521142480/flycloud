"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_api_product_spu = require("../../api/product/spu.js");
const sheep_api_trade_order = require("../../api/trade/order.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
if (!Array) {
  const _easycom_s_goods_column2 = common_vendor.resolveComponent("s-goods-column");
  _easycom_s_goods_column2();
}
const _easycom_s_goods_column = () => "../s-goods-column/s-goods-column.js";
if (!Math) {
  _easycom_s_goods_column();
}
const _sfc_main = {
  __name: "s-goods-card",
  props: {
    data: {
      type: Object,
      default: () => ({})
    },
    styles: {
      type: Object,
      default: () => ({})
    }
  },
  setup(__props) {
    const LayoutTypeEnum = {
      // 单列大图
      ONE_COL_BIG_IMG: "oneColBigImg",
      // 双列
      TWO_COL: "twoCol",
      // 单列小图
      ONE_COL_SMALL_IMG: "oneColSmallImg"
    };
    const state = common_vendor.reactive({
      goodsList: [],
      leftGoodsList: [],
      rightGoodsList: []
    });
    const props = __props;
    const { layoutType, btnBuy, spuIds } = props.data || {};
    const { marginLeft, marginRight } = props.styles || {};
    const buyStyle = common_vendor.computed(() => {
      if (btnBuy.type === "text") {
        return {
          background: `linear-gradient(to right, ${btnBuy.bgBeginColor}, ${btnBuy.bgEndColor})`
        };
      }
      if (btnBuy.type === "img") {
        return {
          width: "54rpx",
          height: "54rpx",
          background: `url(${sheep_index.sheep.$url.cdn(btnBuy.imgUrl)}) no-repeat`,
          backgroundSize: "100% 100%"
        };
      }
    });
    let count = 0;
    let leftHeight = 0;
    let rightHeight = 0;
    function calculateGoodsColumn(height = 0, where = "left") {
      if (!state.goodsList[count])
        return;
      if (where === "left")
        leftHeight += height;
      if (where === "right")
        rightHeight += height;
      if (leftHeight <= rightHeight) {
        state.leftGoodsList.push(state.goodsList[count]);
      } else {
        state.rightGoodsList.push(state.goodsList[count]);
      }
      count++;
    }
    async function getGoodsListByIds(ids) {
      const { data } = await sheep_api_product_spu.SpuApi.getSpuListByIds(ids);
      return data;
    }
    common_vendor.onMounted(async () => {
      state.goodsList = await getGoodsListByIds(spuIds.join(","));
      await sheep_api_trade_order.OrderApi.getSettlementProduct(state.goodsList.map((item) => item.id).join(",")).then(
        (res) => {
          if (res.code !== 0) {
            return;
          }
          sheep_hooks_useGoods.appendSettlementProduct(state.goodsList, res.data);
        }
      );
      if (layoutType === LayoutTypeEnum.TWO_COL) {
        calculateGoodsColumn();
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_BIG_IMG && state.goodsList.length
      }, common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_BIG_IMG && state.goodsList.length ? {
        b: common_vendor.f(state.goodsList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "edfbd448-0-" + i0,
            c: common_vendor.p({
              size: "sl",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        c: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        d: common_vendor.s(buyStyle.value),
        e: common_vendor.s({
          marginBottom: __props.data.space * 2 + "rpx"
        })
      } : {}, {
        f: common_vendor.unref(layoutType) === LayoutTypeEnum.TWO_COL && state.goodsList.length
      }, common_vendor.unref(layoutType) === LayoutTypeEnum.TWO_COL && state.goodsList.length ? {
        g: common_vendor.f(state.leftGoodsList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: common_vendor.o(($event) => calculateGoodsColumn($event, "left"), item.id),
            c: "edfbd448-1-" + i0,
            d: common_vendor.p({
              size: "md",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom,
              titleWidth: 330 - common_vendor.unref(marginLeft) - common_vendor.unref(marginRight)
            }),
            e: item.id
          };
        }),
        h: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        i: common_vendor.s(buyStyle.value),
        j: common_vendor.s({
          paddingRight: __props.data.space + "rpx",
          marginBottom: __props.data.space + "px"
        }),
        k: common_vendor.f(state.rightGoodsList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: common_vendor.o(($event) => calculateGoodsColumn($event, "right"), item.id),
            c: "edfbd448-2-" + i0,
            d: common_vendor.p({
              size: "md",
              goodsFields: __props.data.fields,
              tagStyle: __props.data.badge,
              data: item,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom,
              titleWidth: 330 - common_vendor.unref(marginLeft) - common_vendor.unref(marginRight)
            }),
            e: item.id
          };
        }),
        l: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        m: common_vendor.s(buyStyle.value),
        n: common_vendor.s({
          paddingLeft: __props.data.space + "rpx",
          marginBottom: __props.data.space + "px"
        })
      } : {}, {
        o: common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_SMALL_IMG && state.goodsList.length
      }, common_vendor.unref(layoutType) === LayoutTypeEnum.ONE_COL_SMALL_IMG && state.goodsList.length ? {
        p: common_vendor.f(state.goodsList, (item, k0, i0) => {
          var _a;
          return {
            a: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/goods/index", {
              id: item.id
            }), item.id),
            b: "edfbd448-3-" + i0,
            c: common_vendor.p({
              size: "lg",
              goodsFields: __props.data.fields,
              data: item,
              tagStyle: __props.data.badge,
              titleColor: (_a = __props.data.fields.name) == null ? void 0 : _a.color,
              subTitleColor: __props.data.fields.introduction.color,
              topRadius: __props.data.borderRadiusTop,
              bottomRadius: __props.data.borderRadiusBottom
            }),
            d: item.id
          };
        }),
        q: common_vendor.t(common_vendor.unref(btnBuy).type === "text" ? common_vendor.unref(btnBuy).text : ""),
        r: common_vendor.s(buyStyle.value),
        s: common_vendor.s({
          marginBottom: __props.data.space + "px"
        })
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-edfbd448"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-goods-card/s-goods-card.js.map
